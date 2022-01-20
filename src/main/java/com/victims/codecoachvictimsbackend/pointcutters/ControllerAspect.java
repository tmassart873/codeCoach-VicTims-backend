package com.victims.codecoachvictimsbackend.pointcutters;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* com.victims.codecoachvictimsbackend.*.api.*.*(..))")
    public void allControllers() {}

    @Before("allControllers()")
    public void logInfoBeforeAllControllerMethods(JoinPoint joinPoint) {
       log(joinPoint, "Calling");
    }

    @After("allControllers()")
    public void logInfoAfterAllControllerMethods(JoinPoint joinPoint) {
        log(joinPoint, "Finished");
    }

    private void log(JoinPoint joinPoint, String when){
        String method = joinPoint.getSignature().getName();
        String currentClass = joinPoint.getTarget().getClass().getSimpleName();
        logger.info(currentClass + ": " + when + " method: " + method);
    }
}
