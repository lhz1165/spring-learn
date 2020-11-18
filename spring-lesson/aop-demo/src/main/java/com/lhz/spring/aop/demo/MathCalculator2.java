package com.lhz.spring.aop.demo;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/20
 * 基于注解的aop编程
 **/
public class MathCalculator2 {


    public Integer div(int i,int j){
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

    public static void main(String[] args) {
        AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
        beanFactory.register(LogAspects.class,MainConfigOfAOP.class);
        //beanFactory.register(MyNameMatchMethodPointcutAdvisor.class);
        //beanFactory.getBeanFactory().addBeanPostProcessor(new MyAnnotationAwareAspectJAutoProxyCreator());

        //refresh方法里面的registerBeanPostProcessors(beanFactory);获取带代理
        //finishBeanFactoryInitialization(beanFactory);获得自己定义的bean，并且产生代理对象
        beanFactory.refresh();
        MathCalculator2 bean = beanFactory.getBean(MathCalculator2.class);
       // NameMatchMethodPointcutAdvisor advisor = beanFactory.getBean(MyNameMatchMethodPointcutAdvisor.class);
       // advisor.setAdvice(new CountingAdvice());
        //advisor.setMappedName("div");
        Integer div = bean.div(2, 1);
        //bean.print();
        beanFactory.close();
    }

    /**
     * aop api的一般使用
     * @param args
     */
//    public static void main(String[] args) {
//        MathCalculator m = new MathCalculator();
//        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(m);
//        proxyFactory.addAspect(LogAspects.class);
//        //proxyFactory.ad
//        MathCalculator proxy = (MathCalculator)proxyFactory.getProxy();
//        proxy.div(1,2,true);
//    }




}