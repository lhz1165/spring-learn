[Bean](../spring-Bean的归纳.md)

# FactoryBean的应用

```java
       BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/bean-creation-context.xml");
      //3factoryBean
      User user3 = beanFactory.getBean("user-by-factoryBean", User.class);

    <!--  3factoryBean实例化  -->
    <bean id="user-by-factoryBean" class="com.lhz.spring.bean.factory.UserFactoryBean"/>

```

```java
public class UserFactoryBean implements FactoryBean<User> {
   @Override
   public User getObject() throws Exception {
      User user = new User();
      user.setId(3);
      user.setName("lhz factoryBean");
      return user;
   }
   @Override
   public Class<?> getObjectType() {
      return User.class;
   }
}
```

先准备好UserFactoryBean实现FactoryBean，完成它的实现方法。

接着我们会去探索为什么获取以[user-by-factoryBean]为bean名字的bean是User对象而不是FactoryBean对象，如果不知道上面写的代码是什么意思，可以入土了，点右上角差走吧。

首先在beanFactory.getBean("user-by-factoryBean", User.class);之前会再applicationcontext里面初始化UserFactoryBean的对象实例bean。

其次我们getBean的时候进入doGetBean()方法，

```java
protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
      @Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {

   final String beanName = transformedBeanName(name);
   Object bean;

   // Eagerly check singleton cache for manually registered singletons.
   Object sharedInstance = getSingleton(beanName);
   if (sharedInstance != null && args == null) {
      bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
   }
          ........
```

在缓存中会获取到类型为UserFactoryBean，beanName为user-by-factoryBean的实例



然后在 bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);转化为User实例

具体过程,先判断是否要获取FactoryBean实例

```java
if (BeanFactoryUtils.isFactoryDereference(name)) {
   return beanInstance;
}
-----------------------------------------------------------------------------------------
public static boolean isFactoryDereference(@Nullable String name) {
		return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
}
-----------------------------------------------------------------------------------------
String FACTORY_BEAN_PREFIX = "&";	
```

显而易见，如果我们bean的名字带有&那么直接返回刚刚获取到的FactoryBean实例，

否则进入object = getObjectFromFactoryBean(factory, beanName, !synthetic);

再进入object = doGetObjectFromFactoryBean(factory, beanName);

关节代码 object = factory.getObject(); 就是我们UserFactoryBean实现的

```java
@Override
   public User getObject() throws Exception {
      User user = new User();
      user.setId(3);
      user.setName("lhz factoryBean");
      return user;
   }

```

同样找到这个对象之后也可以执行后置处理

```java
try {
   object = postProcessObjectFromFactoryBean(object, beanName);
}
	@Override
	protected Object postProcessObjectFromFactoryBean(Object object, String beanName) {
		return applyBeanPostProcessorsAfterInitialization(object, beanName);
	}

```

到此返回这个实例，并且加入缓存



## 总结

**在bean生命周期的第一步便是要跨过FactoryBean的这道坎**

  如果我们是获取由实现FactoryBean自定义返回的对象，那么需要使用用FactoryBean的BeanName来获取想要的实例，当检测到FactoryBean的BeanName那么就会有方法去执行getObject()来返回我们自定义的对象。

  如果我们的FactoryBean的BeanName之前加上&符号，那么就会检测到我们想要的是FactoryBean实例而不是

FactoryBean产生的自定义对象实例。

[测试代码](src/main/java/com/lhz/spring/bean/BeanInstantiationDemo.java)
