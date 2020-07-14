package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/**
 * 基于配置文件的beanDefinition解析
 * @author lhzlhz
 * @create 2020/7/14
 */
public class BeanMetaConfigurationDemo {
	public static void main(String[] args) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		PropertiesBeanDefinitionReader definitionReader = new PropertiesBeanDefinitionReader(beanFactory);
		String location = "classpath:/META-INF/user.properties";
		int i = definitionReader.loadBeanDefinitions(location);
		System.out.println("加载 "+i+"个");

		User bean = beanFactory.getBean(User.class);
		System.out.println(bean);
	}
}
