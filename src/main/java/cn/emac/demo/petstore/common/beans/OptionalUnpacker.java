package cn.emac.demo.petstore.common.beans;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 通过cglib代理添加从{@code Optional}类型到非{@code Optional}类型的Bean属性转换支持。转换规则：
 * <ul>
 * <li>如果不为空，则先取出{@code Optional}对象中的值，然后调用{@code convertToType}进行转换</li>
 * <li>如果为空，则返回{@code null}</li>
 * </ul>
 *
 * @author Emac
 * @since 2017-05-25
 */
public class OptionalUnpacker implements MethodInterceptor {

    private static final Method BEANUTILSBEAN_CONVERT;

    static {
        Method m = null;
        try {
            m = BeanUtilsBean.class.getDeclaredMethod("convert", Object.class, Class.class);
        } catch (NoSuchMethodException e) {
            // ignore
        }
        BEANUTILSBEAN_CONVERT = m;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (BEANUTILSBEAN_CONVERT.equals(method)) {
            Object value = args[0];
            Class type = (Class) args[1];
            // Optional -> Non-Optional
            if (value instanceof Optional && !Optional.class.equals(type)) {
                Optional optValue = (Optional) value;
                args[0] = optValue.isPresent() ? optValue.get() : null;
            }
        }
        return proxy.invokeSuper(obj, args);
    }
}
