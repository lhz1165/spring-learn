package com.lhz.spring.bean.factory;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author lhzlhz
 * @create 2020/7/5
 */
public class UserFactoryBean implements FactoryBean<User> {
	@Override
	public User getObject() throws Exception {
		User user = new User();
		user.setId(3);
		user.setName("lhz factoryBean");
		return user;
	}

	@Override
	public Class<?> getObjectType() {
		return User.class;
	}
}
