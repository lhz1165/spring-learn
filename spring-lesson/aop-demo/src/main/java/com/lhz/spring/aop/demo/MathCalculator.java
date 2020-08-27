package com.lhz.spring.aop.demo;

import com.lhz.spring.aop.demo.advice.CountingAdvice;
import com.lhz.spring.aop.demo.advice.MyAdviosr;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
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

    /**
     * ProxyFactory来创建代理对象 以编程的方式
     * @param args
     */
    public static void main(String[] args) {
        ProxyFactory pf = new ProxyFactory(new MathCalculator());
        NameMatchMethodPointcutAdvisor myAdviosr = new NameMatchMethodPointcutAdvisor();
        myAdviosr.setAdvice(new CountingAdvice());
        myAdviosr.setMappedName("div");
        pf.addAdvisor(myAdviosr);
        MathCalculator proxy = (MathCalculator)pf.getProxy();
        proxy.div(1, 2);


    }
    /**
     * ProxyFactory来创建代理对象 通过配置文件创建代理对象
     * @param args
     */
    //
//    public static void main(String[] args) {
//        ApplicationContext beanFactory = new ClassPathXmlApplicationContext("classpath:aop-test.xml");
//
//
//        MathCalculator bean = (MathCalculator)beanFactory.getBean("mathCalculator");
//
//        bean.div(2, 1);
//
//    }



}