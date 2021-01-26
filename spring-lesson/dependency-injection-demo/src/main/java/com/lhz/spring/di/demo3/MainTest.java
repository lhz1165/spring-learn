package com.lhz.spring.di.demo3;


import com.lhz.spring.di.demo3.config.AopConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * @author: lhz
 * @date: 2020/11/18
 **/
public class MainTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.refresh();


        applicationContext.close();
    }


}
