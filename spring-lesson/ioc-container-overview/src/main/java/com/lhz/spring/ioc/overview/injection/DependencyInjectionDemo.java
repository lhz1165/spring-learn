package com.lhz.spring.ioc.overview.injection;

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
 * 依赖注入
 * 根据11和22 可以看出 依赖查找和依赖注入不属于同一个源
 *
 *
 * 实时注入
 * 延时注入
 *
 */
public class DependencyInjectionDemo {
	public static void main(String[] args) {
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/dependency-injection.xml");
		UserRepository userRepository = beanFactory.getBean("userRepository",UserRepository.class);
		//System.out.println("实时查找"+userRepository);

		//11依赖注入DefaultListableBeanFactory
		System.out.println(userRepository.getBeanFactory());
		//false
		//System.out.println(userRepository.getBeanFactory()==beanFactory);


		//22依赖查找 exception
		//System.out.println(beanFactory.getBean(BeanFactory.class));

		System.out.println(userRepository.getObjectFactory().getObject());

		//true说明 注入的就是当前上下文对象
		System.out.println(userRepository.getObjectFactory().getObject()==beanFactory);

	}

}
