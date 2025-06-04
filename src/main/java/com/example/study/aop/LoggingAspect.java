package com.example.study.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    // 定义切入点(拦截service包下所有方法)
    @Pointcut("execution(* com.example.study.aop.*.*(..))")
    public void serviceLayer() {}

    // 前置通知
    @Before("serviceLayer()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("方法执行前: " + joinPoint.getSignature().getName());
        System.out.println("参数: " + Arrays.toString(joinPoint.getArgs()));
    }

    // 后置通知(无论是否抛出异常都会执行)
    @After("serviceLayer()")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("方法执行完成: " + joinPoint.getSignature().getName());
    }

    // 返回后通知
    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        System.out.println("方法返回: " + joinPoint.getSignature().getName());
        System.out.println("返回值: " + result);
    }

    // 异常通知
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
        System.out.println("方法抛出异常: " + joinPoint.getSignature().getName());
        System.out.println("异常信息: " + ex.getMessage());
    }

    // 环绕通知
    @Around("serviceLayer()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("环绕通知 - 方法执行前: " + pjp.getSignature().getName());
        long startTime = System.currentTimeMillis();

        Object result = pjp.proceed(); // 执行目标方法

        long endTime = System.currentTimeMillis();
        System.out.println("环绕通知 - 方法执行后: " + pjp.getSignature().getName());
        System.out.println("执行耗时: " + (endTime - startTime) + "ms");

        return result;
    }
}