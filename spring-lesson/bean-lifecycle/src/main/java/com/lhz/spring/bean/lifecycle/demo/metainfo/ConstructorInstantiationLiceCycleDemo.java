package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.SuperUser;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * @author: lhz
 * @date: 2020/7/16
 **/
public class ConstructorInstantiationLiceCycleDemo {
    //
    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //添加BeanPostProcessor
       // factory.addBeanPostProcessor(new InstantiationLifeCycleDemo.MyInstantiationAwareBeanPostProcessor());

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        String[] locations = {"META-INF/constructor-dependency-inject.xml", "META-INF/dependency-look-up.xml"};
//        Resource resource =new ClassPathResource(locations);
//        EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
        int number = reader.loadBeanDefinitions(locations);

        System.out.println("加载beandefinition数量 : "+number);
        //父
        User user = factory.getBean("user", User.class);
        System.out.println(user);

        //子
        User superUser = factory.getBean("superUser", SuperUser.class);
        System.out.println(superUser);

        //holder
        UserHolder userHolder = factory.getBean("userHolder", UserHolder.class);
        System.out.println(userHolder);
    }
}
