[Bean](../spring-Bean的归纳.md)

## Bean的创建流程


#### 1.main方法的获取bean的方法

------



```java
UserHolder userHolder = factory.getBean("userHolder2", UserHolder.class);
```

#### 2.doGetBean(name, requiredType, null, false);

------

  进去获得对象，有就从缓存(singletonObjects)拿没有就通过ObjectFactory创建，它的实现是lambda表达式。

```java
sharedInstance = getSingleton(beanName, () -> {
   try {
      return createBean(beanName, mbd, args);
   }
```

#### 3.进入createBean获取对象

------



```java
Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
```

​     这里面是执行InstantiationAwareBeanPostProcessor接口的个实现方法postProcessBeforeInstantiation(应postProcessAfterInstantiation个法在充性候行)，这个处理器可以返回自己定义对象，如果有bean那么就不会执行spring通过反射生产bean的流程了（Object beanInstance = doCreateBean(beanName, mbdToUse, args);）

#### 4.进入Object beanInstance = doCreateBean(beanName, mbdToUse, args);获取对象，实例化阶段

------



```java
if (instanceWrapper == null) {
   instanceWrapper = createBeanInstance(beanName, mbd, args);
}
```

创建bean包装对象，会检测是自动注入还是普通的没有注入(通过int AUTOWIRE_CONSTRUCTOR = 3来判断)

#### 5.填充属性，进入初始化前阶段

------



```java
try {
    //instanceWrapper是BeanWrapper
   populateBean(beanName, mbd, instanceWrapper);
    //exposedObject是BeanWrapper的wrappedObject，就是我们需要的Bean对象
   exposedObject = initializeBean(beanName, exposedObject, mbd);
}
```

populateBean()会执行InstantiationAwareBeanPostProcessor的两个实现方法postProcessAfterInstantiation和postProcessPropertyValues这两个是我们自定义的填充属性的方式，如果postProcessAfterInstantiation返回false代表不使用spring的赋值，通过改变参数的引用来修改对象的属性，然后直接返回，如果postProcessPropertyValues返回有对象，pvs = pvsToUse，接下来通过applyPropertyValues(beanName, mbd, bw, pvs);来执行属性的赋值，这里的pvs 应该是从配置文件里面读取，我们可以postProcessPropertyValues方法，修改属性，设置我们想要的属性。

#### 6.后置处理去，初始化的中后期阶段

------

```java
exposedObject = initializeBean(beanName, exposedObject, mbd);
```

进入分别看到以下三个方法,最后返回

```java
invokeAwareMethods(beanName, bean);
wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
invokeInitMethods(beanName, wrappedBean, mbd);
wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
```

分是执行的是

- 实现某某Aware例如BeanNameAware, BeanFactoryAware, BeanClassLoaderAware的setBeanClassLoader，setBeanFactory，setBeanName复制操作

- 实现了BeanPostProcessor的Object postProcessBeforeInitialization(Object bean, String beanName)

- 自定义的初始化方法，包括@PostConstruct，xml或@bean配置的init-method，实现InitializingBean的afterPropertiesSet方法。

- 实现了BeanPostProcessor的Object postProcessAfterInitialization(Object bean, String beanName)

  #### 7.至此bean的初始化完成然后返回就行，

  返回到最开始的getSingleton(String beanName, ObjectFactory<?> singletonFactory)里的addSingleton(beanName, singletonObject);放入this.singletonObjects.put(beanName, singletonObject);缓存
