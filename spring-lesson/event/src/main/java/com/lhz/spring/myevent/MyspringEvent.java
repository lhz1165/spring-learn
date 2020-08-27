package com.lhz.spring.myevent;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.context.ApplicationEvent;

/**
 * @author lhzlhz
 * @create 2020/8/28
 */
public class MyspringEvent extends ApplicationEvent {
	/**
	 * Create a new {@code ApplicationEvent}.
	 *
	 * @param source the object on which the event initially occurred or with
	 *               which the event is associated (never {@code null})
	 */
	public MyspringEvent(String source) {
		super(source);
	}

	@Override
	public String getSource() {
		return (String)super.getSource();
	}

	public String getMessage() {
		return getSource();
	}
}
