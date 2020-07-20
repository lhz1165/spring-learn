package com.lhz.spring.di.demo2;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author: lhz
 * @date: 2020/7/17
 **/
public class CycleDependencyDemo3 {
    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();


        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        String[] locations = {"META-INF/cycle-dependency-injection.xml"};
        int number = reader.loadBeanDefinitions(locations);
        System.out.println("加载beandefinition数量 : "+number);



    }
}
