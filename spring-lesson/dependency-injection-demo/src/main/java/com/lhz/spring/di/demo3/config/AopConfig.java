package com.lhz.spring.di.demo3.config;

import com.lhz.spring.di.demo3.aop.LoginAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author: lhz
 * @date: 2020/11/18
 **/
@EnableAspectJAutoProxy
@Configuration
public class AopConfig {
    @Bean
    public LoginAspect aspect() {
        return new LoginAspect();
    }

}
