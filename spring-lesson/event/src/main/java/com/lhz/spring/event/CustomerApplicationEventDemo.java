package com.lhz.spring.event;

import com.lhz.spring.myevent.MyEventListener;
import com.lhz.spring.myevent.MyspringEvent;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author lhzlhz
 * @create 2020/8/28
 * 自定义事件的三个要求
 * 扩展ApplicationEvent
 * 实现ApplicationListener
 * 注册ApplicationListener
 *
 */
public class CustomerApplicationEventDemo {
	public static void main(String[] args) {
		GenericApplicationContext context = new GenericApplicationContext();
		context.addApplicationListener(new MyEventListener());
		context.refresh();
		context.publishEvent(new MyspringEvent("hello world"));

		context.close();
	}
}
