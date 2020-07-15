package com.lhz.spring.bean;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lhzlhz
 * @create 2020/7/5
 *
 * 实例化bean
 * 1静态方法
 * 2工厂方法
 * 3factoryBean的方法
 *
 * 特殊方式
 * ServiceLoaderFactoryBean
 * AutowireCapableBeanFactory#CreateBean(Class,boolean)
 * BeanDefinitionRegister#registerBeanDefinition(String,BeanDefinition)
 * @see SpecialBeanInstantiationDemo
 *
 * 接下来是看如何初始化Bean
 * @see BeanInitializationDemo
 *
 */
public class BeanInstantiationDemo {
	public static void main(String[] args) {
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF\\bean-creation-context.xml");
		//1静态方法
//		User user =  beanFactory.getBean("user-by-static-method",User.class);
//		//2工厂方法
//		User user2 = beanFactory.getBean("user-by-factory",User.class);
		//3factoryBean
		User user3 = beanFactory.getBean("user-by-factoryBean", User.class);
//		System.out.println(user);
//		System.out.println(user2);
		System.out.println(user3);
	}
}
