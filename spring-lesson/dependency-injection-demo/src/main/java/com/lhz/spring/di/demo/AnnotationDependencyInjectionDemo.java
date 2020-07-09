package com.lhz.spring.di.demo;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 *
 * 基于java注解的依赖注入实现
 * @author lhzlhz
 * @create 2020/7/9
 */
public class AnnotationDependencyInjectionDemo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
		String resourcesLocation = "classpath:META-INF\\dependency-look-up.xml";
		//解析xml
		reader.loadBeanDefinitions(resourcesLocation);

		applicationContext.register(AnnotationDependencyInjectionDemo.class);

		applicationContext.refresh();
		UserHolder u = applicationContext.getBean(UserHolder.class);
		System.out.println(u);


		applicationContext.close();
	}

	/**
	 * 通过构造器注入 user对象
	 * primary="true" 优先级注册（super or superuser）
	 * @param user
	 * @return
	 */
	@Bean
	public UserHolder getUserHolder(User user) {

		return new UserHolder(user);

	}

}
