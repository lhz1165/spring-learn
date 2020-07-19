package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.User;
import com.sun.org.apache.xml.internal.security.Init;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

import javax.annotation.PostConstruct;

/**
 *
 * bean初始化阶段的回调
 * @author: lhz
 * @date: 2020/7/16
 **/
public class UserHolder implements BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, InitializingBean {
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


    private ClassLoader classLoader;

    private BeanFactory beanFactory;

    private String beanName;


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        //this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public String toString() {
        return "UserHolder{" +
                "user=" + user +
                ", number=" + number +
                ", desc='" + desc + '\'' +
                ", beanName='" + beanName + '\'' +
                '}';
    }

    /**
     * bean实例化阶段的三种回调
     */
    @PostConstruct
    public void initPostConstruct() {
        System.out.println("PostConstruct");
        this.desc = "PostConstruct";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("override afterPropertiesSet");
        this.desc = "afterPropertiesSet";
    }

    /**
     *
     * 配置文件的方法
     * init-method="customerInit"
     */
    public void customerInit(){
        System.out.println("customer init ");
    }
}
