package com.lhz.spring.aop.demo2;


import com.lhz.spring.aop.demo2.config.AspectConfig;
import com.lhz.spring.aop.demo2.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class MainTest2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AspectConfig.class);
        applicationContext.refresh();
        UserService userService = (UserService) applicationContext.getBean("userServiceProxy");
        userService.login();


    }
}
