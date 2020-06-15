package com.lhz.ioc.java.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyEditorSupport;
import java.util.stream.Stream;

/**
 * BeanInfo的基本使用
 * @author lhzlhz
 * @create 2020/6/15
 */
public class BeanInfoDemo {
	public static void main(String[] args) throws IntrospectionException {
		//获取对象的getter和setter方法
		BeanInfo beanInfo = Introspector.getBeanInfo(Person.class,Object.class);
		Stream.of(beanInfo.getPropertyDescriptors())
				.forEach(propertyDescriptor -> {
					//PropertyDescriptor 允许添加属性编辑器
					//name -> string
					//age -> int
					Class<?> propertyType = propertyDescriptor.getPropertyType();
					if ("age".equals(propertyDescriptor.getName())) {//为age增加editor
						//string ->int
						propertyDescriptor.setPropertyEditorClass(StringToInteger.class);
					}
				});

	}
	static class StringToInteger extends PropertyEditorSupport{
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			Integer value = Integer.valueOf(text);
			setValue(value);
		}
	}

}
