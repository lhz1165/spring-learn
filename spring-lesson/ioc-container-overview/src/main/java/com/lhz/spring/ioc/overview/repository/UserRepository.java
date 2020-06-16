package com.lhz.spring.ioc.overview.repository;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * @author lhzlhz
 * @create 2020/6/16
 */
public class UserRepository {
	private Collection<User> users;//自定义

	private BeanFactory beanFactory;//内置的

	private ObjectFactory<ApplicationContext> objectFactory;

	public Collection<User> getUsers() {
		return users;
	}

	public ObjectFactory<ApplicationContext> getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(ObjectFactory<ApplicationContext> objectFactory) {
		this.objectFactory = objectFactory;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public String toString() {
		return "UserRepository{" +
				"users=" + users +
				'}';
	}
}
