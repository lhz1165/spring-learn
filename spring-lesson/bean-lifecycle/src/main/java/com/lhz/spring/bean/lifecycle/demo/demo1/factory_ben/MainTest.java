package com.lhz.spring.bean.lifecycle.demo.demo1.factory_ben;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: lhz
 * @date: 2020/11/19
 **/
public class MainTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:MTTA-INF2\\applicationcontxt.xml");

        User1 user = (User1) applicationContext.getBean("user");
        System.out.println(user);

    }
}
