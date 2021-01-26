package com.lhz.spring.di.demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: lhz
 * @date: 2020/7/13
 **/
@Component("bb")
public class B {
    @Autowired
    A a;


    public void bbb() {
        System.out.println("执行b.bbb()");
    }
    public void setA(A a) {
        this.a = a;
    }

}
