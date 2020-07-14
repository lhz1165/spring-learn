package com.lhz.spring.di.demo;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * Aware接口 回调的依赖注入
 * @author lhzlhz
 * @create 2020/7/9
 */
public class AwareDependencyInjectionDemo implements BeanFactoryAware, ApplicationContextAware {


	private static BeanFactory beanFactory;

	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		//注册为bean
		context.register(AwareDependencyInjectionDemo.class);


		context.refresh();

		System.out.println(beanFactory == context.getBeanFactory());
		System.out.println(applicationContext == context);

		context.close();


	}

	/**
	 * 注入bean
	 * @param beanFactory
	 * @throws BeansException
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		AwareDependencyInjectionDemo.beanFactory = beanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AwareDependencyInjectionDemo.applicationContext = applicationContext;
	}
}
