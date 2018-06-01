package cn.emac.demo.petstore.spring;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * 扩展{@code org.springframework.beans.BeanUtils}支持{@code Optional}类型和非{@code Optional}类型属性之间的复制。
 *
 * @author Emac
 * @since 2017-05-26
 */
public class Beans {

    /**
     * 首先调用{@code org.springframework.beans.BeanUtils#copyProperties}复制兼容类型属性，然后尝试{@code Optional}类型和非{@code Optional}类型属性之间的复制，规则如下：
     * <p>
     * 1 {@code Optional}类型到非{@code Optional}类型的转换规则（不检查泛型类型）：
     * <ul>
     * <li>如果源属性为null或者为空，则目标属性 = null</li>
     * <li>如果源属性不为空，则目标属性 = 源属性.get()</li>
     * </ul>
     * 2 非{@code Optional}类型到{@code Optional}类型的转换规则（不检查泛型类型）：
     * <ul>
     * <li>目标属性 = Optional.ofNullable(源属性)</li>
     * </ul>
     *
     * @see org.springframework.beans.BeanUtils#copyProperties
     */
    public static void copyProperties(final Object source, final Object target) {
        // 1 兼容类型属性拷贝
        BeanUtils.copyProperties(source, target);
        // 2 Optional和非Optional类型属性拷贝
        PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        boolean writeOptional = Optional.class.equals(writeMethod.getParameterTypes()[0]);
                        boolean readOptional = Optional.class.equals(readMethod.getReturnType());
                        if (writeOptional != readOptional) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                if (readOptional && !writeOptional) {
                                    // source.optional -> target.non-optional
                                    Optional optValue = (Optional) value;
                                    if (optValue == null || !optValue.isPresent()) {
                                        writeMethod.invoke(target, new Object[]{null});
                                    } else {
                                        writeMethod.invoke(target, optValue.get());
                                    }
                                } else if (!readOptional && writeOptional) {
                                    // source.non-optional -> target.optional
                                    writeMethod.invoke(target, Optional.ofNullable(value));
                                }
                            } catch (Throwable ex) {
                                throw new FatalBeanException(
                                        "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                            }
                        }
                    }
                }
            }
        }
    }
}
