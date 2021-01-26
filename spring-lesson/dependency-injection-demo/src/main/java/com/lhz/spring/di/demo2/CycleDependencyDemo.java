package com.lhz.spring.di.demo2;

import com.lhz.spring.di.demo3.config.AopConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/13
 * 使用autowired进行循环依赖
 **/
public class CycleDependencyDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AopConfig.class);
        applicationContext.register(A.class, B.class);
        applicationContext.refresh();


        A a = applicationContext.getBean(A.class);
        System.out.println("a的引用b为" + a.b);
        a.p();

        applicationContext.close();
    }
}
