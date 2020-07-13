package com.lhz.spring.di.demo2;

import com.lhz.spring.di.demo.UserHolder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/13
 * 使用setter方法进行循环依赖
 **/
public class CycleDependencyDemo2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.registerBeanDefinition("aa",createBeanDefinitionA());
        applicationContext.registerBeanDefinition("bb",createBeanDefinitionB());

        applicationContext.refresh();
        A a = applicationContext.getBean(A.class);

        B b = applicationContext.getBean(B.class);
        System.out.println("bean A  "+a);
        System.out.println("bean B  "+b);
        System.out.println("bean A.b  "+a.b);
        System.out.println("bean B.a  "+b.a);
        applicationContext.close();
    }
    public static BeanDefinition createBeanDefinitionB() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(B.class);
        //注入superUser
        beanDefinitionBuilder.addPropertyReference("a","aa");
        return beanDefinitionBuilder.getBeanDefinition();
    }
    public static BeanDefinition createBeanDefinitionA() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(A.class);
        //注入superUser
        beanDefinitionBuilder.addPropertyReference("b","bb");
        return beanDefinitionBuilder.getBeanDefinition();
    }
}
