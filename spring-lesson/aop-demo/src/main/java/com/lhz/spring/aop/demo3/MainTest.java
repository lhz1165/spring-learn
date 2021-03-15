package com.lhz.spring.aop.demo3;

import com.lhz.spring.aop.demo3.config.AopConfig;
import com.lhz.spring.aop.demo3.service.AaService;
import com.lhz.spring.aop.demo3.service.Aproxy;
import com.lhz.spring.aop.demo3.service.UserService;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class MainTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AopConfig.class, UserService.class, Aproxy.class);
        applicationContext.refresh();
        ProxyConfig config = applicationContext.getBean(ProxyConfig.class);
        config.setProxyTargetClass(false);
        UserService userService2 = applicationContext.getBean(UserService.class);
        AaService userService = applicationContext.getBean(AaService.class);
        userService.a();
        applicationContext.close();
    }


}
