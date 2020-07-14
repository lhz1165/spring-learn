package com.lhz.spring.bean;

import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * bean的构建
 * @author lhzlhz
 * @create 2020/6/23
 */
public class BeanDefinitionCreationDemo {
	public static void main(String[] args) {
		//1通过BeanDefinitionBuilder来构建bean
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
		//通过属性设置
		beanDefinitionBuilder.addPropertyValue("id", 14);
		beanDefinitionBuilder.addPropertyValue("name", "lhz");
		//获取beanDefinition实例  AbstractBeanDefinition
		BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
		//beanDefinition并非最终状态可以修改
		//-----------------------------------------------
		//2通过抽象类派生  extends AbstractBeanDefinition
		GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
		//设置bean类型
		genericBeanDefinition.setBeanClass(User.class);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("id", 1)
				.add("name", 2);
		genericBeanDefinition.setPropertyValues(propertyValues);

	}
}
