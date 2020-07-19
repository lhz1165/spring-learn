package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.SuperUser;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.ObjectUtils;

/**
 *
 * 实例化生命周期
 * @author: lhz
 * @date: 2020/7/16
 **/
public class InstantiationLifeCycleDemo2 {
    public static void main(String[] args) {
        executeBeanFactory();
        System.out.println("----------------------");
        executeApplicationContext();
    }

    public static void executeBeanFactory() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //添加BeanPostProcessor
        // 1使用api添加
        factory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        //2 使用xml 配置bean的方式
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        String location = "META-INF/i-dependency-look-up.xml";
        Resource resource =new ClassPathResource(location);
        EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
        int number = reader.loadBeanDefinitions(encodedResource);

        System.out.println("加载beandefinition数量 : "+number);
        //父
        User user = factory.getBean("user2", User.class);
        //子
        User superUser = factory.getBean("superUser2", SuperUser.class);

        UserHolder userHolder = factory.getBean("userHolder2", UserHolder.class);
        System.out.println(userHolder);
        System.out.println(user);
        System.out.println(superUser);
    }


    public static void executeApplicationContext() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();

        applicationContext.setConfigLocations("META-INF/i-dependency-look-up.xml");
        applicationContext.refresh();
        //父
        User user = applicationContext.getBean("user2", User.class);
        //子
        User superUser = applicationContext.getBean("superUser2", SuperUser.class);

        UserHolder userHolder = applicationContext.getBean("userHolder2", UserHolder.class);

        System.out.println(userHolder);
        System.out.println(user);
        System.out.println(superUser);

        applicationContext.close();
    }


}

