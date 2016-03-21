package cn.emac.demo.petstore.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Emac
 * @since 2016-03-21
 */
@Aspect
@Component
public class PaginationAspect {

    @AfterReturning(
            pointcut = "execution(* cn.emac..*.*ByPage(..))",
            returning = "result")
    public void afterByPage(JoinPoint joinPoint, Object result) {
        if (!(result instanceof PageBuilder)) {
            return;
        }

        PageBuilder pageBuilder = (PageBuilder) result;
        Integer count = pageBuilder.getDsl().selectCount().from("(" + pageBuilder.getSql() + ") as `rawtable`").fetchOneInto(Integer.class);
        pageBuilder.setTotal(count);
    }
}
