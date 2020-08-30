package com.lhz.spring.aop.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/20
 * 基于注解的aop编程
 **/
public class MathCalculator23 {


    public int div(int i,int j,boolean self){
        System.out.println("MathCalculator...div...");
//        if (self) {
//            MathCalculator m = new MathCalculator();
//            AspectJProxyFactory proxyFactory = new AspectJProxyFactory(m);
//            proxyFactory.addAspect(LogAspects.class);
//            //proxyFactory.ad
//            MathCalculator proxy = (MathCalculator)proxyFactory.getProxy();
//            proxy.print();
//        }else {
//            print();
//        }
        return i/j;
    }

    public void print() {
        System.out.println("hello !!!!");
    }


    /**
     * aop api的一般使用
     * @param args
     */
    public static void main(String[] args) {

    }


}