package lhz.spring01; /**
 * @author lhzlhz
 * @create 2020/6/27
 */

import lhz.spring01.annotaion.Autowried;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * 生成通过setter 注入userService
 */
public class TestSpring {


	@Test
	public void testController() throws Exception{
		UserController userController = new UserController();
		UserService userService = new UserService();
		Class<? extends UserController> clazz = userController.getClass();
		Field userService1 = clazz.getDeclaredField("userService");
		userService1.setAccessible(true);
		String userServiceFiled = userService1.getName();
		String setMethod = "set" + userServiceFiled.substring(0, 1).toUpperCase() + userServiceFiled.substring(1, userServiceFiled.length());
		Method method = clazz.getMethod(setMethod, UserService.class);
		method.invoke(userController,userService);

		System.out.println(userController.getUserService());


	}
	/**
	 * IOC容器  核心的内容
	 *
	 */
	@Test
	public void testIOCController() {
		UserController userController = new UserController();
		Class<? extends UserController> clazz = userController.getClass();
		Stream.of(clazz.getDeclaredFields()).forEach(field -> {
			String fieldName = field.getName();
			//这个字段是否又autowried
			Autowried annotation = field.getAnnotation(Autowried.class);
			if (annotation != null) {
				//注入
				field.setAccessible(true);
				Class<?> type = field.getType();
				try {
					//UserService对象
					//关键是spring中这个对象是bean 通过注解或者xml配置来的，如何找到？
					Object o = type.newInstance();
					field.set(userController,o);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println(userController.getUserService());
	}




}
