package com.lhz.spring.bean.factory;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

/**
 * @author lhzlhz
 * @create 2020/7/5
 */
public class UserFactory  implements InitializingBean {
	public User createUserByFactory() {
		User user = new User();
		user.setId(2);
		user.setName("lhz factory");
		return user;
	}

	@PostConstruct
	public void init(){
		System.out.println("通过@PostConstruct UserFactory 初始化过程重");

	}

	public void init2() {
		System.out.println("通过@Bean 的 initMethod UserFactory 初始化过程重");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("通过InitializingBean#afterPropertiesSet（） UserFactory 初始化过程重");

	}
}
