package org.test;
import org.interfaces.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Test {
	public static void main(String[] args) {
		//创建ApplicationContext对象，参数为配置文件放置的位置
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF\\dependency-injection.xml");
		//通过Person bean的id来获取bean实例，面向接口编程，因此此处强制类型转换为接口类型
		Person p = (Person)context.getBean("man");
		//直接执行Person的eatFood()方法
		p.eatFood();
 	}
}
