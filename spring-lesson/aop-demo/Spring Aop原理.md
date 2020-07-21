# Spring Aop原理

```java
AnnotationConfigApplicationContext beanFactory = new AnnotationConfigApplicationContext();
beanFactory.register(LogAspects.class,MainConfigOfAOP.class);
//refresh方法里面的registerBeanPostProcessors(beanFactory);获取带代理
//finishBeanFactoryInitialization(beanFactory);获得自己定义的bean，并且产生代理对象
beanFactory.refresh();
```

通过断点发现最后还是到了AbstractBeanFactory的getBean(),再到AbstractAutowireCapableBeanFactory的createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)再进入doCreatebean

以上都是bean工厂产生bean的基本步骤

最终在里面进行代理对象的生成

```java
//进入初始化
exposedObject = initializeBean(beanName, exposedObject, mbd);
//后置处理器
wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
//这里调用的是AbstractAutoProxyCreator的实现方法
Object current = processor.postProcessAfterInitialization(result, beanName);
```

------



### AnnotationAwareAspectJAutoProxyCreator的实现

这个处理器是由@EnableAspectJautoProxy放进去的

```
@Override
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
   if (bean != null) {
      Object cacheKey = getCacheKey(bean.getClass(), beanName);
      if (this.earlyProxyReferences.remove(cacheKey) != bean) {
         return wrapIfNecessary(bean, beanName, cacheKey);
      }
   }
   return bean;
}
```



创建代理对象的代码

```java
Object proxy = createProxy(
      bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
//进去到createProxy方法 最后返回这个
return proxyFactory.getProxy(getProxyClassLoader());
```

再 进入getProxy

```java
public Object getProxy(@Nullable ClassLoader classLoader) {
   return createAopProxy().getProxy(classLoader);
}
```

这个getProxy也是一个策略模式通过createAopProxy创建不同的代理对象（包括JdkDynamicAopProxy，CglibAopProxy），使用不同的策略来创建代理对象

```java
//DefaultAopProxyFactory 选择代理对象的产生工厂
@Override
public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
   if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
      Class<?> targetClass = config.getTargetClass();
      if (targetClass == null) {
         throw new AopConfigException("TargetSource cannot determine target class: " +
               "Either an interface or a target is required for proxy creation.");
      }
      if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
         return new JdkDynamicAopProxy(config);
      }
      return new ObjenesisCglibAopProxy(config);
   }
   else {
      return new JdkDynamicAopProxy(config);
   }
}
```

有了具体的工厂（jdk动态代理还是cglib），我们就可以产生代理对象了



一般使用的是CglibAopProxy类的 getProxy(@Nullable ClassLoader classLoader)



这是jdk的动态代理

```
@Override
public Object getProxy(@Nullable ClassLoader classLoader) {
   if (logger.isTraceEnabled()) {
      logger.trace("Creating JDK dynamic proxy: " + this.advised.getTargetSource());
   }
   Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
   findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
   return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
}
```

最后 我在customerAop包里自己实现了一个简单代理对象的前置处理。