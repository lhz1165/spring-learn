[<<<Returan to Bean](../spring-Bean的归纳.md)
# spring循环依赖的深入研究问题

普通A依赖b   B依赖a

如果这个时候A是代理对象呢？

```java
@Component("aa")
public class A {
    @Autowired
    B b;


    public void p() {
        System.out.println("执行a.p()");
        b.bbb();
    }

    public void setB(B b) {
        this.b = b;
    }
}
```

```java
@Component("bb")
public class B {
    @Autowired
    A a;


    public void bbb() {
        System.out.println("执行b.bbb()");
    }
    public void setA(A a) {
        this.a = a;
    }

}
```

```java
public class CycleDependencyDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //注册一个切面类对象配置代理
        applicationContext.register(AopConfig.class);
        applicationContext.register(A.class, B.class);
        applicationContext.refresh();
        
        
        A a = applicationContext.getBean(A.class);
        System.out.println("a的引用b为" + a.b);
        a.p();

        applicationContext.close();
    }
}
```



```java
@Aspect
public class LoginAspect {
    @Pointcut("execution(public * com.lhz.spring.di.demo2.A.p(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void beforeAdvice() {
        System.out.println("proxyFactory-----记录登录日志.......");
    }

}
```

输出结果如何呢

```
a的引用b为null
proxyFactory-----记录登录日志.......
执行a.p()
执行b.bbb()
```

为什么a.b为null 

并且明明a.b为null但是a.p();是可以调用到b对象的bbb()。这就有点奇怪了

-----------



因为是这样的。

正常情况下A对象的生成包括一下步骤

1. 实例化A对象，通过反射的构造函数
2. 属性填充，例如把B的依赖赋给A
3. 初始化对象，执行aware，postProcessBeforeInitialization init方法 postProcessAfterInitialization

需要注意的是postProcessAfterInitialization是产生代理对象。

但是在循环依赖的时候代理对象提前产生了，就这就在三级缓存的factory方法中实现的，即在第二步属性填充好，A的代理对象已经有了

此时 普通A包含普通B的依赖，普通B包含代理对象A的依赖，这样就不构成循环的了，但是执行A代理对象的方法，还是会走到普通A的方法里面去的

过程是这样的

```java
//A 加入三级缓存
addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
//填充属性
populateBean(beanName, mbd, instanceWrapper);
//初始化
exposedObject = initializeBean(beanName, exposedObject, mbd);


---------------------------------------------------------------------------------------

//如果B需要三级缓存的A，那么就会调用这个方法，然后直接产生代理对象
protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
		Object exposedObject = bean;
		if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
			for (BeanPostProcessor bp : getBeanPostProcessors()) {
				if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
					SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
                    //代理对象在这里产生了，不会在后面的postProcessAfterInitialization产生了
					exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
				}
			}
		}
		return exposedObject;
	}



```

创建A，A加入三级缓存，填充A，检测到需要B

创建B，B加入三级缓存，填充A，从三级缓存拿，getEarlyBeanReference()，拿到一个对象A（Proxy），A（Proxy)加入二级缓存，B依赖这个A（Proxy），B创建好了加入一级缓存

A填充好B，依赖这个B，把A的对象替换成代理对象加入一级缓存。





所以回答了上面的问题，A（Proxy）没有依赖B，所以a.b是null，但是B以来了代理对象A，所以能打印出结果，

a.p（）执行代理对象的p方法，最终走到普通A对象的P方法，然后普通A依赖了B，所以说a.p（）不会出现空指针异常

