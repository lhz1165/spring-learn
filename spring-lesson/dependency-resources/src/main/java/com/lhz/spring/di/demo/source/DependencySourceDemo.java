package com.lhz.spring.di.demo.source;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;

/**
 * @author lhzlhz
 * @create 2020/7/13
 */
public class DependencySourceDemo {
	//注入再postProcessProperties方法执行，早于setter，也早于@postConstruct
	@Autowired
	private BeanFactory beanFactory;
	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 依赖注入多一个非spring管理对象的来源
	 */
	@PostConstruct
	public void init() {
		System.out.println(beanFactory == applicationContext);
		System.out.println(beanFactory == applicationContext.getAutowireCapableBeanFactory());
		System.out.println(resourceLoader == applicationContext);
		System.out.println(applicationEventPublisher == applicationContext);
	}

	/**
	 * 依赖查找不可以通过这样得到对象
	 */
	@PostConstruct
	public void dependencyLook() {
		getBean(ResourceLoader.class);
		getBean(ApplicationContext.class);
		getBean(ApplicationEventPublisher.class);
		getBean(BeanFactory.class);
	}

	public <T>T getBean(Class<T> type){
		try {
			return beanFactory.getBean(type);
		} catch (Exception e) {
			System.out.println(type.getName()+" 依赖查找失败");
		}
		return null;

	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		applicationContext.register(DependencySourceDemo.class);

		applicationContext.refresh();
		applicationContext.close();

	}
}
