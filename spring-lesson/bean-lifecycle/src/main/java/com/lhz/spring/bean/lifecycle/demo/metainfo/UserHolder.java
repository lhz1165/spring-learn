package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.User;

/**
 * @author: lhz
 * @date: 2020/7/16
 **/
public class UserHolder {
    private User user;

    public UserHolder() {
    }

    public UserHolder(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserHolder{" +
                "user=" + user +
                '}';
    }
}
