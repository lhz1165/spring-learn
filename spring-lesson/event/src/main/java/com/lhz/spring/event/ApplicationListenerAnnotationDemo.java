package com.lhz.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author lhzlhz
 * @create 2020/8/26
 * 基于注解的事件
 */

public class ApplicationListenerAnnotationDemo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		applicationContext.register(ApplicationListenerAnnotationDemo.class);
		applicationContext.refresh();
		applicationContext.close();
	}

	@EventListener
	public void onApplicationEvent(ApplicationEvent event) {
		System.out.println("@EventListener spring 事件 "+event);
	}
}
