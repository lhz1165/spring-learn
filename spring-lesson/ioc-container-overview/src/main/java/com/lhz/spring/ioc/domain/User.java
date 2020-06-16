package com.lhz.spring.ioc.domain;

/**
 * @author lhzlhz
 * @create 2020/6/15
 */
public class User {
	private int id;
	private String name;

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
