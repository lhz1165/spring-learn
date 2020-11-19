package com.lhz.spring.bean.lifecycle.demo.demo1.factory_ben;

/**
 * @author: lhz
 * @date: 2020/11/19
 **/
public class User1 {
    int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User1{" +
                "id=" + id +
                '}';
    }
}
