package com.lhz.spring.event;

import com.lhz.spring.myevent.MyEventListener;
import com.lhz.spring.myevent.MyspringEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author lhzlhz
 * @create 2020/8/28
 */
public class InjectionApplicationEventPublishDemo implements ApplicationEventPublisherAware
		, ApplicationContextAware, BeanPostProcessor {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		//3 4谁先谁后不一定
		applicationEventPublisher.publishEvent(new MyspringEvent(" from @Autowired   [applicationEventPublisher]"));
		applicationContext.publishEvent(new MyspringEvent("from   @Autowired  [applicationContext]"));
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext c = new AnnotationConfigApplicationContext();
		c.register(InjectionApplicationEventPublishDemo.class);
		c.addApplicationListener(new MyEventListener());
		c.refresh();
		c.close();

	}

	//1
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		applicationEventPublisher.publishEvent(new MyspringEvent("from ApplicationEventPublisherAware"));
	}
	//2
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		applicationContext.publishEvent(new MyspringEvent("from ApplicationContextAware"));
	}
}
