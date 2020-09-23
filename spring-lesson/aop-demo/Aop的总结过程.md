[AOP](../spring-aop归纳.md)

# Aop的总结



## 过程（以ProxyFactoryBean和ProxyFactory分析）

### 1.配置拦截器和目代理类以便proxy代理对象正确产生

**<u>原理</u>**

无论是通过xml配置（ProxyFactoryBean），还是编程注解的方式配置（ProxyFactory），都需要一个AopProxy对象，这就是代理对象分为CglibAopProxy或JdkDynamicAopProxy。

他们是由AopProxyFactory（DefaultAopProxyFactory）生产的。createAopProxy(AdvisedSupport config)，通过AdvisedSupport 来判断是jdk还是cglib.

<u>**场景**</u>

spring通过getBean方法回去代理对象的时候会调用ProxyFactory或者ProxyFactoryBean的getProxy（）方法，这个方法就是封装的工厂创建的aopProxy对象

### 2.代理对象执行目标方法。

**<u>原理</u>**

jdk的动态代理或者cglib的字节码

jdk是JdkDynamicAopProxy的invoke（），cglib是CglibAopProxy的intercept（）。

<u>**场景**</u>

这里有两步

首先会调用ProxyFactory的getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);来获取连接器的集合List<MethodInterceptor>chain

然后调用ReflectiveMethodInvocation的proeccd（），递归的去一个一个匹配执行拦截器的invoke方法，这个invoke方法又封装的是advice的增强方法，增强之后最终再通过反射执行目标方法。



