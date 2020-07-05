package com.lhz.spring.bean.factory;

import com.lhz.spring.ioc.domain.User;

/**
 * @author lhzlhz
 * @create 2020/7/5
 */
public class UserFactory {
	public User createUserByFactory() {
		User user = new User();
		user.setId(2);
		user.setName("lhz factory");
		return user;
	}
}
