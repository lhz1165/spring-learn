package com.lhz.spring.bean;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 *
 * //1通过@bean定义
 * //2通过component
 * //3通过import导入
 * //4通过api注册实现
 *
 * 如果把bean变成可使用的bean，下个类会实例化bean
 * @see BeanInstantiationDemo
 *
 * @author lhzlhz
 * @create 2020/6/23
 */
//3import  ！！！他和component 都是生成ben 如果两个都试用那么不会出现重复的Config bean
@Import(AnnotationBeanDefinitionDemo.Config.class)//3通过import导入
public class AnnotationBeanDefinitionDemo {
	public static void main(String[] args) {
		//创建BeanFactory容器
		AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();


		//注册配置类 如果通过这个api  不需要使用component或import注解
		beanFactory.register(Config.class);

		//4
		registerBeanDefinition(beanFactory, "lhz11", User.class);
		registerBeanDefinition(beanFactory,  User.class);
		//启动应用上下文
		beanFactory.refresh();

		//依赖查找
		//Map<String, Config> configBeans = beanFactory.getBeansOfType(Config.class);
		System.out.println("User 类型的bean "+beanFactory.getBeansOfType(User.class));
		System.out.println("Config 类型的bean "+beanFactory.getBeansOfType(Config.class));

		//关闭上下文
		beanFactory.close();

	}

	/**
	 * 命名bean的注册方式
	 * @param registry
	 * @param beanName
	 * @param beanClass
	 */
	public static void registerBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class<?> beanClass) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
		beanDefinitionBuilder.addPropertyValue("id", 1);
		beanDefinitionBuilder.addPropertyValue("name", "lhz");
		//判断beanName
		if (StringUtils.hasText(beanName)) {
			//注册
			registry.registerBeanDefinition(beanName,beanDefinitionBuilder.getBeanDefinition());
		}else {
			//非命名的方式注册ben
			BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(),registry);
		}


	}

	public static void registerBeanDefinition(BeanDefinitionRegistry registry, Class<?> beanClass) {
		registerBeanDefinition(registry, null, beanClass);
	}


	@Component//2通过component
	static class Config{
		@Bean({"user","lhzuser"})//1通过@bean定义
		public User getUser() {
			User user = new User();
			user.setId(1);
			user.setName("lhz @bean");
			return user;
		}
	}

}
