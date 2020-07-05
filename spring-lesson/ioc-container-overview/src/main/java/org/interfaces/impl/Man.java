package org.interfaces.impl;
import org.interfaces.Food;
import org.interfaces.Person;

/** Person接口的具体实现类*/
public class Man implements Person {
	//定义Food接口私有属性，面向Food接口编程，而不是具体的实现类
	private Food food;
	//构建 setter方法，必须要有，后面会讲解为什么
	public void setFood(Food food) {
		this.food = food;
	}
	//实现Person接口eatFood方法
	public void eatFood() {
		System.out.println(food.eat());
	}
}
