package com.lhz.spring.aop.demo.customerAop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;

/**
 * @author: lhz
 * @date: 2020/7/21
 **/
class MyAnnotationAwareAspectJAutoProxyCreator implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("indexImpl".equals(beanName)|| beanName.contains("index")) {
            //需要代理的接口
            Class<?>[] interfaces = bean.getClass()
                    .getInterfaces();
            Object o = Proxy.newProxyInstance(MyAnnotationAwareAspectJAutoProxyCreator.class.getClassLoader(), interfaces, new MyInvocationHandler(bean));
            return o;

        }
        return null;
    }
}
