package com.lhz.spring.ioc.overview;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lhzlhz
 * @create 2020/6/15
 *  依赖查找的 demo
 */
public class DependencyLookupDemo {
	public static void main(String[] args) {
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/dependency-look-up.xml");
		User user = (User) beanFactory.getBean("user");
		System.out.println(user.toString());
	}
}
