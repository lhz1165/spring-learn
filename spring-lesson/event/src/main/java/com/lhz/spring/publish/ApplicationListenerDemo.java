package com.lhz.spring.publish;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lhzlhz
 * @create 2020/8/26
 * 基于接口的事件
 */
@EnableAsync
public class ApplicationListenerDemo implements ApplicationEventPublisherAware {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(ApplicationListenerDemo.class);

		//方法一  基于addApplicationListener去注册监听器
		applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
			@Override
			public void onApplicationEvent(ApplicationEvent event) {
				System.out.println("ApplicationListener接口监听器 spring 事件 "+event);
			}
		});
		//方法二  基于ApplicationListener接口注册为bean
		applicationContext.register(MyApplicationListener.class);
		applicationContext.refresh();
		applicationContext.close();
	}

	/**
	 * 注入一个ApplicationEventPublisher
	 *
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		//发布ApplicationEvent
		applicationEventPublisher.publishEvent(new ApplicationEvent("hello world "){

		});
		//发布任意事件
		applicationEventPublisher.publishEvent("hello world");
	}

	static class MyApplicationListener implements ApplicationListener<ApplicationEvent>{
		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			System.out.println("MyApplicationListener 监听器触发事件！！！！");
		}
	}
}
