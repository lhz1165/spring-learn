package com.lhz.spring.di.demo;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * 基于api的依赖注入
 * @author lhzlhz
 * @create 2020/7/9
 */
public class ApiDependencyInjectionDemo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		//生成BeanDefinition
		BeanDefinition userBeanDefinition = createBeanDefinition();
		//注册userHolder Beandefinition
		applicationContext.registerBeanDefinition("userHolder",userBeanDefinition);


		//读取xml
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
		String resourcesLocation = "classpath:META-INF\\api-dependency-injection.xml";
		reader.loadBeanDefinitions(resourcesLocation);

		applicationContext.refresh();

		UserHolder u = applicationContext.getBean(UserHolder.class);
		System.out.println(u);

		applicationContext.close();


	}

	public static BeanDefinition createBeanDefinition() {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(UserHolder.class);
		//注入superUser
		beanDefinitionBuilder.addPropertyReference("user","superUser");
		return beanDefinitionBuilder.getBeanDefinition();
	}
}
