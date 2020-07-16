package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.SuperUser;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.ObjectUtils;

/**
 *
 * 实例化生命周期
 * @author: lhz
 * @date: 2020/7/16
 **/
public class InstantiationLifeCycleDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //添加BeanPostProcessor
        factory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        String location = "META-INF/dependency-look-up.xml";
        Resource resource =new ClassPathResource(location);
        EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
        int number = reader.loadBeanDefinitions(encodedResource);

        System.out.println("加载beandefinition数量 : "+number);
        //父
        User user = factory.getBean("user", User.class);
        //子
        User superUser = factory.getBean("superUser", SuperUser.class);
        System.out.println(user);
        System.out.println(superUser);
    }

    static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor{
        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            if (ObjectUtils.nullSafeEquals("superUser", beanName)&&SuperUser.class.equals(beanClass)) {
                //覆盖掉xml里面的对象
                return new SuperUser();
            }
            return null;//保持ioc容器的操作
        }
    }
}
