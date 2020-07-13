package com.lhz.spring.di.demo2;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/13
 * 使用autowired进行循环依赖
 **/
public class CycleDependencyDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(A.class,B.class);
        applicationContext.refresh();

        A a = applicationContext.getBean(A.class);
        B b = applicationContext.getBean(B.class);
        System.out.println(a);
        System.out.println(b);
        System.out.println(a.b);
        System.out.println(b.a);
        applicationContext.close();
    }
}
