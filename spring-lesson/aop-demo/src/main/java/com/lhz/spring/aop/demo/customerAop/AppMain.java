package com.lhz.spring.aop.demo.customerAop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/21
 **/
public class AppMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getBeanFactory().addBeanPostProcessor(new MyAnnotationAwareAspectJAutoProxyCreator());
        applicationContext.register(IndexImpl.class);
        applicationContext.refresh();

        Index bean = applicationContext.getBean(Index.class);
        bean.div(1, 2);

        applicationContext.close();

    }
}
