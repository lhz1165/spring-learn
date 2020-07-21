package com.lhz.spring.aop.demo.customerAop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: lhz
 * @date: 2020/7/21
 **/
public class MyInvocationHandler implements InvocationHandler {
    Object bean;

    public MyInvocationHandler(Object bean) {
        this.bean = bean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before --------------");
        return method.invoke(bean,args);
    }
}
