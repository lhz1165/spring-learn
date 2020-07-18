package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.User;

/**
 * @author: lhz
 * @date: 2020/7/16
 **/
public class UserHolder {
    private User user;

    int number;

    String desc;

    public User getUser() {
        return user;
    }

    public int getNumber() {
        return number;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public UserHolder(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserHolder{" +
                "user=" + user +
                ", number=" + number +
                ", desc='" + desc + '\'' +
                '}';
    }
}
