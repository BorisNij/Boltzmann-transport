package net.bnijik.schooldbcli.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class ServiceLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Pointcut("@annotation(org.springframework.stereotype.Service) || execution(* net.bnijik.schooldbcli.service.*.*(..))")
    public void serviceMethodsPointcut() {
    }

    @Around("serviceMethodsPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("Entered: {}.{}() with argument[s] = {}",
                         joinPoint.getSignature().getDeclaringTypeName(),
                         joinPoint.getSignature().getName(),
                         Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (logger.isDebugEnabled()) {
                logger.debug("Exited: {}.{}() with result = {}",
                             joinPoint.getSignature().getDeclaringTypeName(),
                             joinPoint.getSignature().getName(),
                             result);
            }
            return result;
        } catch (Exception e) {
            logger.error("Exception: {} in {}.{}()",
                         Arrays.toString(joinPoint.getArgs()),
                         joinPoint.getSignature().getDeclaringTypeName(),
                         joinPoint.getSignature().getName());
            throw e;
        }
    }
}