[<<<Return To AOP](../spring-aop归纳.md)
# Aop的Proxy的准备到产生过程，再到拦截

### 设计原理

Spring的代理对象生成思想是这样的：Spring协调了ProxyFactoryBean来完成这个任务，在ProxyFactoryBean中又封装了代理对象的生成过程（jdk，cglib）；

------

相关类

**ProxyConfig**（顶级类，确保所有的proxy的创建者都有一致的配置）

​		继承

**AdvisedSupport** （实现了Advised）他是代理对象配置的管理者，例如获取目标对象，决定以什么方式（jdk or cglib）来创建代理，他不是proxy的创建者，只是持有Advices和Advisors

​		继承

**ProxyCreatorSupport** （配置产生Proxy的工厂，createAopProxy()）

​		继承

**ProxyFactoryBean**，**ProxyFactory**，**AspectProxyFactory**（具体生产）



### 具体展开

生成代理对象可以通过三个类ProxyFactoryBean，ProxyFactory，AspectProxyFactory，使用哪个产生代理对象取决于你的配置

### 1.ProxyFactoryBean

#### 如何配置

可在spring中声明式的配置，根据spring官方文档可以查看怎么配置，主要配置包括三个bean，ProxyFactoryBean，targetClass，advisor，由ProxyFactoryBean标签的target ref -》targetClass，Inteceptor来应用advisor。

#### 生成Proxy代理对象

获取ProxyBean核心代码

```java
public Object getObject() throws BeansException {
    //首先初始化通知链各种Advisor
    //扫描所有的配置
    //（切点这些）
   initializeAdvisorChain();
   if (isSingleton()) {
       //获取代理对象（进去可以看到调用了getProxy（）方法）
      return getSingletonInstance();
   }
   else {
      if (this.targetName == null) {
         logger.info("Using non-singleton proxies with singleton targets is often undesirable. " +
               "Enable prototype proxies by setting the 'targetName' property.");
      }
      return newPrototypeInstance();
   }
}
```

```java
private synchronized Object getSingletonInstance() {
  	//其他省略，就是为了代理对象做住准备
    this.singletonInstance = getProxy(createAopProxy());
   return this.singletonInstance;
}

protected Object getProxy(AopProxy aopProxy) {
		return aopProxy.getProxy(this.proxyClassLoader);
	}
```

AopProxy这个对象就是判断我们使用jdk还是CGLIB来创建代理对象的，代理对象有了接下来会介绍AOP拦截器实现的原理

## SpringAop拦截器的实现

这是初始化拦截链的一些过程

```java
private synchronized void initializeAdvisorChain(){
      // Materialize interceptor chain from bean names.
    //通过beanName来实现这个通知拦截器链
      for (String name : this.interceptorNames) {
          //
         if (name.endsWith(GLOBAL_SUFFIX)) {
          	//添加Advisor链的调用
             //通过name来配置
            addGlobalAdvisors((ListableBeanFactory) this.beanFactory,
                  name.substring(0, name.length() - GLOBAL_SUFFIX.length()));
         }

         else {
            // If we get here, we need to add a named interceptor.
            // We must check if it's a singleton or prototype.
            Object advice;
            if (this.singleton || this.beanFactory.isSingleton(name)) {
               // Add the real Advisor/Advice to the chain.
                //获取advice，然后加入到链
               advice = this.beanFactory.getBean(name);
            }
            else {
               // It's a prototype Advice or Advisor: replace with a prototype.
               // Avoid unnecessary creation of prototype bean just for advisor chain initialization.
               advice = new PrototypePlaceholderAdvisor(name);
            }
            addAdvisorOnChainCreation(advice);
         }
      }
   }


}
```

### 原理：

​	相关的配置都在代理对象之中，拦截器在代理对象起作用是通过对这些方法的回调来完成的。

  如果使用Jdk动态代理，那么使用过InvocationHandler来设置的回调；如果是CGlib是通过DynamicAdvisedInterceptor来完成的回调

### jdkDynamicAopProxy的拦截

```java
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
   Object oldProxy = null;
   boolean setProxyContext = false;

   TargetSource targetSource = this.advised.targetSource;
   Object target = null;

   try{
       if (!this.advised.opaque && method.getDeclaringClass().isInterface() &&
            method.getDeclaringClass().isAssignableFrom(Advised.class)) {
         // 根据代理对象的配置来调用服务
         return AopUtils.invokeJoinpointUsingReflection(this.advised, method, args);
      }

      Object retVal;

      if (this.advised.exposeProxy) {
         // Make invocation available if necessary.
         oldProxy = AopContext.setCurrentProxy(proxy);
         setProxyContext = true;
      }

      // 得到目标对象
      // in case it comes from a pool.
      target = targetSource.getTarget();
      Class<?> targetClass = (target != null ? target.getClass() : null);

      // 得到连接器链
      List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

     
         //创建一个 MethodInvocation（实际上是jointPoint接口）
         MethodInvocation invocation =
               new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
         // 沿着拦截器前进
         retVal = invocation.proceed();
    

      // 获取返回结果.
      Class<?> returnType = method.getReturnType();
      if (retVal != null && retVal == target &&
            returnType != Object.class && returnType.isInstance(proxy) &&
            !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
         // Special case: it returned "this" and the return type of the method
         // is type-compatible. Note that we can't help if the target sets
         // a reference to itself in another returned object.
         retVal = proxy;
      }
      else if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
         throw new AopInvocationException(
               "Null return value from advice does not match primitive return type for: " + method);
      }
      return retVal;
   }
   finally {
      if (target != null && !targetSource.isStatic()) {
         // Must have come from TargetSource.
         targetSource.releaseTarget(target);
      }
      if (setProxyContext) {
         // Restore old proxy.
         AopContext.setCurrentProxy(oldProxy);
      }
   }
}
```
