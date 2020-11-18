package com.lhz.spring.aop.demo2;


import com.lhz.spring.aop.demo2.config.AspectConfig;
import com.lhz.spring.aop.demo2.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class MainTest {
    public static void main(String[] args) {
        ApplicationContext application = new ClassPathXmlApplicationContext("classpath:aop-test3.xml");
        UserService userService = (UserService)application.getBean("userServiceProxy");
        userService.login();
    }
}
