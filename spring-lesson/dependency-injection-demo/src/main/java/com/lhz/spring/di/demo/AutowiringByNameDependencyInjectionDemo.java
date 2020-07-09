package com.lhz.spring.di.demo;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 *
 * 自动绑定setter方法注入示例
 * @author lhzlhz
 * @create 2020/7/9
 */
public class AutowiringByNameDependencyInjectionDemo {
	public static void main(String[] args) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions("classpath:META-INF\\autowiring-dependency-injection.xml");
		//依赖查找 创建bean
		UserHolder userHolder = beanFactory.getBean(UserHolder.class);
		System.out.println(userHolder);
	}
}
