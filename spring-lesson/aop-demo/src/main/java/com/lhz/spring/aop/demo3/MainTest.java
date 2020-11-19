package com.lhz.spring.aop.demo3;

import com.lhz.spring.aop.demo3.config.AopConfig;
import com.lhz.spring.aop.demo3.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class MainTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AopConfig.class, UserService.class);
        applicationContext.refresh();

        UserService userService = applicationContext.getBean(UserService.class);
        userService.login();
        applicationContext.close();
    }


}
