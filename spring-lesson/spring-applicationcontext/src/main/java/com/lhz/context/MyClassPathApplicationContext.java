package com.lhz.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: lhz
 * @date: 2020/9/24
 **/
public class MyClassPathApplicationContext extends ClassPathXmlApplicationContext {
    public MyClassPathApplicationContext(String configLocation) throws BeansException {
        super(configLocation);
    }

    @Override
    public void setValidating(boolean validating) {
        System.out.println("1111111");
        super.setValidating(validating);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("META-INF/bean-creation-context.xml");
        applicationContext.register(PropertyEditorConfig.class);
        applicationContext.refresh();
        User user = applicationContext.getBean("user", User.class);
        System.out.println(user);
        applicationContext.close();

    }
}
