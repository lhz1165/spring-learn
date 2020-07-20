package com.lhz.spring.aop.demo;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/20
 **/
public class MathCalculator {

    public int div(int i,int j){
        System.out.println("MathCalculator...div...");
        return i/j;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
        beanFactory.register(LogAspects.class,MainConfigOfAOP.class);
        //refresh方法里面的registerBeanPostProcessors(beanFactory);获取带代理
        //finishBeanFactoryInitialization(beanFactory);获得自己定义的bean，并且产生代理对象
        beanFactory.refresh();
        MathCalculator bean = beanFactory.getBean(MathCalculator.class);
        bean.div(2, 1);
        beanFactory.close();


    }

}