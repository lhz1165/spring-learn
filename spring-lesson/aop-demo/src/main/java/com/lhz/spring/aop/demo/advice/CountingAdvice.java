package com.lhz.spring.aop.demo.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author: lhz
 * @date: 2020/8/26
 **/
public class CountingAdvice extends MethodCounter implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        count(method);
    }
}
