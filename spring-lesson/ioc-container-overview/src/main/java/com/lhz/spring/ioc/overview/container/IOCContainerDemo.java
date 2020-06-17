package com.lhz.spring.ioc.overview.container;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import java.util.Map;

/**
 * @author lhzlhz
 * @create 2020/6/17
 *
 *不使用ApplicationContext来创建ioc容器
 *通过普通的DefaultListableBeanFactory来配置ioc容器
 *IOCContainerAnnotationDemo
 */
public class IOCContainerDemo {
	public static void main(String[] args) {
		//创建BeanFactory容器
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		//加载配置
		String location = "classpath:META-INF\\dependency-look-up.xml";
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		int i = reader.loadBeanDefinitions(location);
		System.out.println("bean number "+i);//3
		lookupCollectionByType(beanFactory);
	}

	private static void lookupCollectionByType(BeanFactory beanFactory) {
		if (beanFactory instanceof ListableBeanFactory) {
			ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
			Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);
			System.out.println(users);
		}
	}
}
