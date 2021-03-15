package com.lhz.spring.aop.demo3.service;

import com.lhz.spring.aop.demo3.aop.Aop;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class UserService {
    @Autowired
    UserService userService;

    @Aop
    @PostConstruct
    public void init() {
       sout();
    }
    @Aop
    public void sout() {
        System.out.println(111111111);
    }
    @Aop
    public void login() {
        System.out.println("-----登录----");
    }

    public void login2() {
        System.out.println("-----登录22----");
    }
}
