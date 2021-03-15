package com.lhz.spring.aop.demo3.service;


import com.lhz.spring.aop.demo3.aop.Aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: lhz
 * @date: 2021/3/11
 **/
public class Aproxy implements AaService {

    @Aop
    @Override
    public void a() {
        System.out.println("aservice");
    }
}
