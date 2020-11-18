package com.lhz.spring.aop.demo3;

import com.lhz.spring.aop.demo3.config.AopConfig;
import com.lhz.spring.aop.demo3.service.UserService;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class MainTest {
//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
//        applicationContext.register(AopConfig.class, UserService.class);
//        applicationContext.refresh();
//        UserService userService = applicationContext.getBean(UserService.class);
//        userService.login();
//        applicationContext.close();
//    }


}
