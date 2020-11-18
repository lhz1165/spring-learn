package com.lhz.spring.aop.demo3.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
@Aspect
public class LoginAspect {
    @Pointcut("execution(public * com.lhz.spring.aop.demo3.service.UserService.login(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void beforeAdvice() {
        System.out.println("proxyFactory-----记录登录日志.......");
    }

//    @After("pointCut()")
//    public void afterAdvice(JoinPoint joinPoint) {
//        System.out.println("proxyFactory-----记录登录日志 after.......");
//    }
}
