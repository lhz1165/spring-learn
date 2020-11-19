package com.lhz.spring.bean.lifecycle.demo.demo1.factory_ben;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author: lhz
 * @date: 2020/11/19
 **/
public class ProxyCreatorPostProcess implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("user")) {
            User1 bean1 = (User1) bean;
           // System.out.println("原来对象"+bean1.toString());

            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(bean1);
            return proxyFactory.getProxy();
        }

        return null;
    }
}
