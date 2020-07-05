package com.lhz.spring.bean;

import com.lhz.spring.bean.factory.UserFactory;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lhzlhz
 * @create 2020/7/6
 */
public class SpecialBeanInstantiationDemo {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF\\bean-creation-context.xml");
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		//创建user对象
		UserFactory userFactory = beanFactory.createBean(UserFactory.class);
		System.out.println(userFactory.createUserByFactory());
	}

}
