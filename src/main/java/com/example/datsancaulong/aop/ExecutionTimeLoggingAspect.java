package com.example.datsancaulong.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeLoggingAspect.class);

    @Around("(@within(org.springframework.stereotype.Service) || @within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Repository)) && execution(* *(..))")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            String className = pjp.getSignature().getDeclaringTypeName();
            String methodName = pjp.getSignature().getName();
            logger.info("[EXEC_TIME] {}.{} executed in {} ms", className, methodName, durationMs);
        }
    }
}

