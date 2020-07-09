package com.lhz.spring.di.demo2;

import com.lhz.spring.di.demo.UserHolder;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 *
 * 基于java注解的【字段】注入实现
 * @author lhzlhz
 * @see AutowiredAnnotationBeanPostProcessor
 * @create 2020/7/9
 */
public class AnnotationFieldDependencyInjectionDemo {
	@Autowired
	private
	//static Autowired会忽略静态字段
	UserHolder userHolder;

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		//解析xml
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
		String resourcesLocation = "classpath:META-INF\\dependency-look-up.xml";
		reader.loadBeanDefinitions(resourcesLocation);

		//注册当前类为配置 bean
		applicationContext.register(AnnotationFieldDependencyInjectionDemo.class);
		//applicationContext.register(UserHolder.class);

		applicationContext.refresh();

		//依赖查找当前的bean
		AnnotationFieldDependencyInjectionDemo demo = applicationContext.getBean(AnnotationFieldDependencyInjectionDemo.class);

		//autowired自动关联
		System.out.println(demo.userHolder);


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
