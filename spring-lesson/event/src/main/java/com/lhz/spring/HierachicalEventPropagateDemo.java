package com.lhz.spring;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author lhzlhz
 * @create 2020/8/27
 * spring层次性 的事件传播
 */
public class HierachicalEventPropagateDemo {
	public static void main(String[] args) {
		//创建父子上下文
		AnnotationConfigApplicationContext parent = new AnnotationConfigApplicationContext();
		parent.setId("parent-context");
		parent.register(MyListener.class);
		AnnotationConfigApplicationContext current = new AnnotationConfigApplicationContext();
		//current---》parent
		current.setId("current-context");
		current.register(MyListener.class);
		current.setParent(parent);
		parent.refresh();

		//current触发了两次事件，一个是自己的，另一个是上级的
		current.refresh();

		parent.close();
		current.close();

	}

	/**
	 * 两个上下文注册同一个事件
	 */
	static class MyListener implements ApplicationListener<ApplicationContextEvent> {
		private static Set<ApplicationContextEvent> process = new LinkedHashSet<>();

		@Override
		public void onApplicationEvent(ApplicationContextEvent event) {
			if (process.add(event)) {
				System.out.printf("监听到spring 上下文【id 为 %s 】：%s\n",event.getApplicationContext().getId()
						,event.getClass().getSimpleName());
			}


		}
	}
}
