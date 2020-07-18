package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.SuperUser;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
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
        String location = "META-INF/i-dependency-look-up.xml";
        Resource resource =new ClassPathResource(location);
        EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
        int number = reader.loadBeanDefinitions(encodedResource);

        System.out.println("加载beandefinition数量 : "+number);
        //父
        User user = factory.getBean("user2", User.class);
        //子
        User superUser = factory.getBean("superUser2", SuperUser.class);

        UserHolder userHolder = factory.getBean("userHolder2", UserHolder.class);
        System.out.println(userHolder);
        System.out.println(user);
        System.out.println(superUser);
    }

    static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor{
        /**
         * 直接跳过spring的实例化阶段 用我们的实例化
         * @param beanClass
         * @param beanName
         * @return
         * @throws BeansException
         */
        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            if (ObjectUtils.nullSafeEquals("superUser2", beanName)&&SuperUser.class.equals(beanClass)) {
                //覆盖掉xml里面的对象
                SuperUser superUser = new SuperUser();
                superUser.setId(33);
                superUser.setName("postProcessBeforeInstantiationUser");
                return superUser;
            }
            return null;//保持ioc容器的操作
        }

        /**
         * 跳过属性的赋值，populate
         * @param bean
         * @param beanName
         * @return
         * @throws BeansException
         */
        @Override
        public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
            if (ObjectUtils.nullSafeEquals("user2", beanName)&&User.class.equals(bean.getClass())) {
                User bean1 = (User) bean;
                bean1.setId(2);
                //xml里面的配置元信息忽略
                return true;
            }
            return true;
        }

        /**
         *
         * @param pvs
         * @param bean
         * @param beanName
         * @return
         * @throws BeansException
         */
        @Override
        public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
            if (ObjectUtils.nullSafeEquals("userHolder2", beanName)&&UserHolder.class.equals(bean.getClass())) {
               //<property name="number" value="22"/> 配置的话 PropertyValues包含这个数据
                //使用api操作
                // !!!!!返回了会忽略到xml的配置使用返回的配置
                MutablePropertyValues propertyValues;
                if (pvs instanceof MutablePropertyValues) {
                    propertyValues = (MutablePropertyValues) pvs;
                }else {
                    propertyValues = new MutablePropertyValues();
                }

                //如果存在修改
                if (propertyValues.contains("desc")) {
                    propertyValues.getPropertyValues();
                    propertyValues.removePropertyValue("desc");
                    propertyValues.addPropertyValue("desc","hhhhhh");

                }
                propertyValues.addPropertyValue("number",222);
                //xml里面的配置所有元信息忽略
                return propertyValues;
            }
            return null;
        }
    }

}
