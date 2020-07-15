package com.lhz.spring.bean.lifecycle.demo.metainfo;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;

/**
 * 、基于注解的bean definition 解析
 * @author lhzlhz
 * @create 2020/7/14
 */
public class AnnotationBeanDefinitionDemo {
	public static void main(String[] args) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		//基于注解的AnnotatedBeanDefinitionReader实现
		AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
		int beanDefinitionCounter = beanFactory.getBeanDefinitionCount();
		System.out.println(beanDefinitionCounter);

		//注册非component类注册
		//注册到ioc容器 默认beanname为annotationBeanDefinitionDemo
		reader.register(AnnotationBeanDefinitionDemo.class);
		int beanDefinitionCounter2 = beanFactory.getBeanDefinitionCount();
		System.out.println(beanDefinitionCounter2);
		//bean name来自于BeanNameGenerator，注解实现来自于AnnotationBeanNameGenerator
		AnnotationBeanDefinitionDemo demo = beanFactory.getBean("annotationBeanDefinitionDemo", AnnotationBeanDefinitionDemo.class);
		System.out.println(demo);
	}

}
