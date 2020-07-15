package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.SuperUser;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * 父子BeanDefinitionDemo合并实例
 * @author: lhz
 * @date: 2020/7/15
 *
 * @see ConfigurableBeanFactory#getMergedBeanDefinition(String)
 **/
public class MergeBeanDefinitionDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        String location = "META-INF/dependency-look-up.xml";
        Resource resource =new ClassPathResource(location);
        EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
        int number = reader.loadBeanDefinitions(encodedResource);

        System.out.println("加载beandefinition数量 : "+number);
        //父
        User user = factory.getBean("user", User.class);
        //子
        User superUser = factory.getBean("superUser", SuperUser.class);
        System.out.println(user);
        System.out.println(superUser);
    }
}
