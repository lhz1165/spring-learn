package com.lhz.spring.di.demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author: lhz
 * @date: 2020/7/13
 **/
@Component("aa")
public class A {
    @Autowired
    B b;

    //使用构造器注入会导致报错
//    public A(B b) {
//        this.b = b;
//    }




    public void setB(B b) {
        this.b = b;
    }
}
