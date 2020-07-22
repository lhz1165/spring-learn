package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.SuperUser;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 *
 * 初始化的生命周期
 * @author: lhz
 * @date: 2020/7/16
 **/
public class InitializationLifeCycleDemo {
    public static void main(String[] args) {
        executeBeanFactory();

    }

    public static void executeBeanFactory() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //添加BeanPostProcessor
        // 1使用api添加
        factory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());

        //添加CommonAnnotationBeanPostProcessor  解决@postConstruct回调的问题
        factory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());

        //2 使用xml 配置bean的方式
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        String location = "META-INF/i-dependency-look-up.xml";
//        Resource resource =new ClassPathResource(location);
//        EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
        //int number = reader.loadBeanDefinitions(encodedResource);
        int number = reader.loadBeanDefinitions(location);

        System.out.println("加载beandefinition数量 : "+number);

        //显示的调用SmartInitializingSingleton的afterSingletonsInstantiated方法
        //将已注册的BeanDefinition初始化spring bean
        //当所有的bean都创建好了之后 再执行SmartInitializingSingleton的回调方法afterSingletonsInstantiated()
        //这样就不用再getBean方法里面再区创建bean
        factory.preInstantiateSingletons();



        //        //父
        User user = factory.getBean("user2", User.class);
//        System.out.println(user);

//        //子
//        User superUser = factory.getBean("superUser2", SuperUser.class);
//        System.out.println(superUser);
        UserHolder userHolder = factory.getBean("userHolder2", UserHolder.class);
        System.out.println(userHolder);



    }




}

