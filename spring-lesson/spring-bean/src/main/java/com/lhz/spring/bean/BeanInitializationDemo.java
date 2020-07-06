package com.lhz.spring.bean;

import com.lhz.spring.bean.factory.UserFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author lhzlhz
 * @create 2020/7/6
 * Bean的初始化1 2 3种方式  同样的销毁一样对应这3种
 */
public class BeanInitializationDemo {
	//1基于postConstruct
	public static void main(String[] args) {
		AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
		//注册
		beanFactory.register(BeanInitializationDemo.class);

		//启动
		beanFactory.refresh();

		//依赖查找
		UserFactory userFactory=beanFactory.getBean(UserFactory.class);//会自动回调postConstruct

		//关闭
		beanFactory.close();

	}

	//2注解实现初始化
	//3UserFactory实现接口
	@Bean(initMethod = "init2") //会自动回调init2（）
	@Lazy(value = true)//应用上下文启动之后才初始化
	UserFactory userFactory() {
		return new UserFactory();
	}
}
