package com.lhz.spring.di.demo;

import com.lhz.spring.ioc.domain.User;
import org.springframework.stereotype.Component;

/**
 * @author lhzlhz
 * @create 2020/7/9
 */
@Component
public class UserHolder {
	private User user;

	public UserHolder() {
	}

	public UserHolder(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserHolder{" +
				"user=" + user +
				'}';
	}
}
