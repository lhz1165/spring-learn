package com.lhz.spring.di.demo3.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
@Aspect
public class LoginAspect {
    @Pointcut("execution(public * com.lhz.spring.di.demo2.A.p(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void beforeAdvice() {
        System.out.println("-------------------");
    }

//    @After("pointCut()")
//    public void afterAdvice(JoinPoint joinPoint) {
//        System.out.println("proxyFactory-----记录登录日志 after.......");
//    }
}
