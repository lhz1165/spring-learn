package com.lhz.spring.ioc.overview.dependency;

import com.lhz.spring.ioc.annotation.Super;
import com.lhz.spring.ioc.domain.User;
import com.lhz.spring.ioc.overview.repository.UserRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * @author lhzlhz
 * @create 2020/6/15
 *  依赖查找的 demo
 *  1.名称（延时和实时）
 *  2.类型（单个和多个）
 *  3.@bean注解
 *
 */
public class DependencyLookupDemo {
	public static void main(String[] args) {
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF\\dependency-look-up.xml");
//		lookupLazy(beanFactory);
//		lookupRealTime(beanFactory);
		//类型查找
		lookupByType(beanFactory);
		//多个实例
		lookupCollectionByType(beanFactory);
		//注解多实例
		lookupCollectionByAnnotation(beanFactory);
	}

	/**
	 *通过名字的依赖查找延迟加载
	 * @param beanFactory
	 */
	private static void lookupLazy(BeanFactory beanFactory) {
		ObjectFactory<User> bean = (ObjectFactory<User>) beanFactory.getBean("myFactory");
		User user = bean.getObject();
		System.out.println("延迟查找"+user);
	}

	/**
	 * 通过名字的依赖查找 实时加载
	 * @param beanFactory
	 */
	private static void lookupRealTime(BeanFactory beanFactory) {
		User user = (User) beanFactory.getBean("user");
		System.out.println("实时查找"+user);
	}

	/**
	 * 类型查找
	 * @param beanFactory
	 */
	private static void lookupByType(BeanFactory beanFactory) {
		User user = (User) beanFactory.getBean(User.class);
		System.out.println("实时查找"+user);
	}


	/**
	 * 复合类型查找
	 * @param beanFactory
	 */
	private static void lookupCollectionByType(BeanFactory beanFactory) {
		if (beanFactory instanceof ListableBeanFactory) {
			ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
			Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);
			System.out.println(users);
		}
	}

	private static void lookupCollectionByAnnotation(BeanFactory beanFactory) {
		if (beanFactory instanceof ListableBeanFactory) {
			ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
			Map<String, User> users = (Map) listableBeanFactory.getBeansWithAnnotation(Super.class);
			System.out.println("@super用户的"+users);
		}
	}





}
