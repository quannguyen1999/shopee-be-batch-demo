package com.shopee.ecommer.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//Print Log whenever Controller Api is call
@Component
@Aspect
@Slf4j
@Profile("dev")
public class LoggingAspectConfig {

    //    @Before("logPointcut()")
    @Before("execution(* com.shopee.ecommer..*(..))")
    public void logAllMethodBeforeCallsAdvice(JoinPoint joinPoint) {
        log.info("Call: " + joinPoint.getSignature().toShortString() + " start");
    }

    //    @After("logPointcut()")
    @Before("execution(* com.shopee.ecommer..*(..))")
    public void logAllMethodAfterCallsAdvice(JoinPoint joinPoint) {
        log.info(joinPoint.getSignature().toShortString() + " end");
    }
}
