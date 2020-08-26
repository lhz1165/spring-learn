package com.lhz.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author lhzlhz
 * @create 2020/8/26
 * 基于接口的事件
 */
public class ApplicationListenerDemo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		//方法一  基于addApplicationListener去注册监听器
		applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
			@Override
			public void onApplicationEvent(ApplicationEvent event) {
				System.out.println("ApplicationListener接口监听器 spring 事件 ");
			}
		});
		//方法二  基于ApplicationListener接口注册为bean
		applicationContext.register(MyApplicationListener.class);
		applicationContext.refresh();
		applicationContext.close();
	}
	static class MyApplicationListener implements ApplicationListener<ApplicationEvent>{
		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			System.out.println("MyApplicationListener 监听器触发事件！！！！");
		}
	}
}
