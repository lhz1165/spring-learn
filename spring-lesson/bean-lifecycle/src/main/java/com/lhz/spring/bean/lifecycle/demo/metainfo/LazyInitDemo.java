package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author: lhz
 * @date: 2020/7/22
 * spring bean懒加载的示例
 * //实例化所有bean的方法  如果是lazy-init=true 那么会判断不去实例化这个bean
 * @see AbstractApplicationContext#finishBeanFactoryInitialization(ConfigurableListableBeanFactory)
 * @see ConfigurableListableBeanFactory#preInstantiateSingletons()
 *
 **/
public class LazyInitDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("META-INF/lazy-dependency-look-up.xml");
        //如果没有懒加载 bean的实例化会发生在finishBeanFactoryInitialization(beanFactory);--->beanFactory.preInstantiateSingletons();
        applicationContext.refresh();
        System.out.println("-------------");
        User bean = applicationContext.getBean(User.class);
        System.out.println(bean);
        applicationContext.close();


    }
}
