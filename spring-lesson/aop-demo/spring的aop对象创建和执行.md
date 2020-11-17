[<<<Return To AOP](../spring-aop归纳.md)

# springAop流程

## 1. spring获取代理对象

普通的spring初始化Bean周期

```java
doGetBean(name, null, null, false);

Object beanInstance = doCreateBean(beanName, mbdToUse, args);
//初始化对象
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
    //执行aware接口方法
	invokeAwareMethods(beanName, bean);
	Object wrappedBean = bean;
    //前置处理器
	wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean)
    //初始化方法@postcontruct或者InitializingBean重写方法
	invokeInitMethods(beanName, wrappedBean, mbd);
    //后置处理器
	wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, 
	return wrappedBean;
}
```

在后置处理器来创建代理对象(AspectJAwareAdvisorAutoProxyCreator对象的后置处理器)

```
具体是AbstractAutoProxyCreator extends ProxyProcessorSupport来创建的
```

```java
wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

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
-----------------------------------------------------------------------------------------
	protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
		// Create proxy if we have advice.
        //连接器链（advisor数组）
		Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
        //创建代理对象
			Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
			this.proxyTypes.put(cacheKey, proxy.getClass());
			return proxy;
		}
		return bean;
	}
```

创建代理的方法

```java
protected Object createProxy(Class<?> beanClass, @Nullable String beanName,
      @Nullable Object[] specificInterceptors, TargetSource targetSource) {
   ProxyFactory proxyFactory = new ProxyFactory();
	。。。。。
	//设置属性

   return proxyFactory.getProxy(getProxyClassLoader());
}
```



获取一个ProxyFactory工厂的getProxy()方法来创建

```java
public Object getProxy(@Nullable ClassLoader classLoader) {
   return createAopProxy().getProxy(classLoader);
}
	//1 createAopProxy
	protected final synchronized AopProxy createAopProxy() {
		if (!this.active) {
			activate();
		}
		return getAopProxyFactory().createAopProxy(this);
	}
	//2 getProxy
	Object getProxy(@Nullable ClassLoader classLoader);

```

getProxy具体包含以下过程

**1-1**
	获取AopProxyFactory==》DefaultAopProxyFactory
**1-2**

​	使用DefaultAopProxyFactory的createAopProxy(）方法来获取AopProxy(JdkDynamicAopProxy||CglibAopProxy)

**2-1**

有了AopProxy之后，调用对应实现类的getProxy()方法获得代理对象

```
Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
```



来到CglibAopProxy的getProxy()来创建代理对象

## 2. spring执行代理方法

有了代理对象之后，只要执行到目标方法(joinpoint)，如果是cglib代理，那么进入CglibAopProxy的内部类DynamicAdvisedInterceptor的intercept(）方法

new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();是关键

```java
public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		//省略
    	//关键
        retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
      }
      retVal = processReturnType(proxy, target, method, retVal);
      return retVal;
   }

   }
```

CglibMethodInvocation是ReflectiveMethodInvocation子类执行proceed()，就是执行ReflectiveMethodInvocation的方法

```java
public Object proceed() throws Throwable {
      return super.proceed();
}

	public Object proceed() throws Throwable {
		// We start with an index of -1 and increment early.
		if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
			return invokeJoinpoint();
		}

		Object interceptorOrInterceptionAdvice =
				this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
		if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
			// Evaluate dynamic method matcher here: static part will already have
			// been evaluated and found to match.
			InterceptorAndDynamicMethodMatcher dm =
					(InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
			Class<?> targetClass = (this.targetClass != null ? this.targetClass : this.method.getDeclaringClass());
			if (dm.methodMatcher.matches(this.method, targetClass, this.arguments)) {
				return dm.interceptor.invoke(this);
			}
			else {
				// Dynamic matching failed.
				// Skip this interceptor and invoke the next in the chain.
				return proceed();
			}
		}
		else {
			// It's an interceptor, so we just invoke it: The pointcut will have
			// been evaluated statically before this object was constructed.
			return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
		}
	}
```