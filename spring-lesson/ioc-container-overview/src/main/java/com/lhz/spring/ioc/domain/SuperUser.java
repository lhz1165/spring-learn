package com.lhz.spring.ioc.domain;

import com.lhz.spring.ioc.annotation.Super;

/**
 * @author lhzlhz
 * @create 2020/6/16
 */
@Super
public class SuperUser extends User{
	private String address;

	public SuperUser() {
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "SuperUser{" +
				"address='" + address + '\'' +
				"} " + super.toString();
	}
}
