package com.lhz.ioc.java.beans;

/**
 * @author lhzlhz
 * @create 2020/6/15
 */
public class Person {
	//String-> string
	String name;//properties
	//String -> int
	Integer age;//properties

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
