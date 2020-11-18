package com.lhz.spring.aop.demo2.aop.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class BeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("proxyFactoryBean-----记录登录日志......");
    }
}
