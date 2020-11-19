package com.lhz.spring.bean.lifecycle.demo.demo1.factory_ben;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author: lhz
 * @date: 2020/11/19
 **/
public class UserFactoryBean implements FactoryBean<User1> {
    @Override
    public User1 getObject() throws Exception {
        User1 user1 = new User1();
        user1.setId(9999);
        return user1;
    }

    @Override
    public Class<?> getObjectType() {
        return User1.class;
    }
}
