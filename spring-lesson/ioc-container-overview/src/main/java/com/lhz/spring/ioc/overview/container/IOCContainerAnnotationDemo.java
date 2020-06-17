package com.lhz.spring.ioc.overview.container;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * @author lhzlhz
 * @create 2020/6/17
 * 使用注解的方式来配置bean，加载到ioc容器
 *
 */
public class IOCContainerAnnotationDemo {
	public static void main(String[] args) {
		//创建BeanFactory容器
		AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
		//加载配置
		beanFactory.register(IOCContainerAnnotationDemo.class);
		//启动应用上下文
		beanFactory.refresh();
		lookupCollectionByType(beanFactory);
	}

	@Bean
	public User getUser() {
		User user = new User();
		user.setId(1);
		user.setName("lhz @bean");
		return user;
	}



	private static void lookupCollectionByType(BeanFactory beanFactory) {
		if (beanFactory instanceof ListableBeanFactory) {
			ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
			Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);
			System.out.println(users);
		}
	}
}
