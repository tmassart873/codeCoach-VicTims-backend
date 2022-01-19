package com.victims.codecoachvictimsbackend.pointcutters;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceAspect {
    private final Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    @Pointcut("execution(* com.victims.codecoachvictimsbackend.*.service.*.*(..))")
    public void allServices() {}

    @Before("allServices()")
    public void logInfoBeforeAllServiceMethods(JoinPoint joinPoint) {
        log(joinPoint,"Calling");
    }

    @After("allServices()")
    public void logInfoAfterAllServiceMethods(JoinPoint joinPoint) {
        log(joinPoint,"Finished");
    }

    private void log(JoinPoint joinPoint, String when){
        String method = joinPoint.getSignature().toShortString();
        String currentClass = joinPoint.getTarget().getClass().getSimpleName();
        logger.info(currentClass + ": " + when + " method: " + method);
    }
}
