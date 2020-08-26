package com.lhz.spring.aop.demo;

import com.lhz.spring.aop.demo.customerAop.Index;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/20
 **/
public class MathCalculator{


    public int div(int i,int j,boolean self){
        System.out.println("MathCalculator...div...");
        if (self) {
            MathCalculator m = new MathCalculator();
            AspectJProxyFactory proxyFactory = new AspectJProxyFactory(m);
            proxyFactory.addAspect(LogAspects.class);
            //proxyFactory.ad
            MathCalculator proxy = (MathCalculator)proxyFactory.getProxy();
            proxy.print();
        }else {
            print();
        }
        return i/j;
    }

    public void print() {
        System.out.println("hello !!!!");
    }

//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
//        beanFactory.register(LogAspects.class,MainConfigOfAOP.class,LogAspects2.class);
//
//        //beanFactory.getBeanFactory().addBeanPostProcessor(new MyAnnotationAwareAspectJAutoProxyCreator());
//
//        //refresh方法里面的registerBeanPostProcessors(beanFactory);获取带代理
//        //finishBeanFactoryInitialization(beanFactory);获得自己定义的bean，并且产生代理对象
//        beanFactory.refresh();
//        MathCalculator bean = beanFactory.getBean(MathCalculator.class);
//        bean.div(2, 1);
//        //bean.print();
//        beanFactory.close();
//    }

    /**
     * aop api的一般使用
     * @param args
     */
    public static void main(String[] args) {
        MathCalculator m = new MathCalculator();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(m);
        proxyFactory.addAspect(LogAspects.class);
        //proxyFactory.ad
        MathCalculator proxy = (MathCalculator)proxyFactory.getProxy();
        proxy.div(1,2,true);
    }


}