package com.lhz.spring.aop.demo2.config;

import com.lhz.spring.aop.demo2.aop.advice.BeforeAdvice;
import com.lhz.spring.aop.demo2.service.UserService;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
@Configuration
public class AspectConfig {

    @Order(1)
    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Order(2)
    @Bean
    public BeforeAdvice beforeAdvice() {
        return new BeforeAdvice();
    }

    @Order(3)
    @Bean("advisor1")
    public NameMatchMethodPointcutAdvisor advisor(BeforeAdvice advice) {
        NameMatchMethodPointcutAdvisor a = new NameMatchMethodPointcutAdvisor();
        a.setAdvice(advice);
        a.setMappedName("login");
        return a;
    }


    @Order(4)
    @Bean("userServiceProxy")
    public ProxyFactoryBean setProxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTargetName("userService");
        proxyFactoryBean.setInterceptorNames("advisor1");
        return proxyFactoryBean;
    }


}
