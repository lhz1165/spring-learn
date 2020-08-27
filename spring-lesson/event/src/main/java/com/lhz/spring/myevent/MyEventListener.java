package com.lhz.spring.myevent;

import org.springframework.context.ApplicationListener;

/**
 * @author lhzlhz
 * @create 2020/8/28
 */
public class MyEventListener implements ApplicationListener<MyspringEvent> {
	@Override
	public void onApplicationEvent(MyspringEvent event) {
		System.out.printf("【线程 %s 监听到事件】: %s \n",Thread.currentThread().getName(),event);
	}
}
