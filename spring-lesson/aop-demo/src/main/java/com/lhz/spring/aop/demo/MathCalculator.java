package com.lhz.spring.aop.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/20
 * 使用xml自定义切面编程
 **/
public class MathCalculator {


    public int div(int i,int j){
        System.out.println("MathCalculator...div...");
        return i/j;
    }

    public void print() {
        System.out.println("hello !!!!");
    }

    public static void main(String[] args) {
        ApplicationContext beanFactory = new ClassPathXmlApplicationContext("classpath:aop-test.xml");


        MathCalculator bean = (MathCalculator)beanFactory.getBean("mathCalculator");

        bean.div(2, 1);

    }



}