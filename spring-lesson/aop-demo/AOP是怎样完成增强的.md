# AOP是怎样完成增强的

## 原理：这些增强操作封装在一个拦截器链之中的，每次执行目标对象的某个方法，通过匹配器去判断，然后递归进入下一个拦截器。

Jdk和cglib会生成不同的AopProxy代理对象，然后构造不同的回调方法来启动对拦截器链的调用，

当执行目标对象的目标方法时，会直接跳到jdk的invoke（）或cglib的intercept（）（内部类DynamicAdvisedInterceptor的方法）

，**殊途同归的是他们调用拦截器链都是在ReflectiveMethodinvocation的proceed（）**实现的

```java
public Object proceed() throws Throwable {
   // 如果拦截器调用完毕那么直接执行target目标方法
    //这是通过AopUtils的反射机制来完成的
   if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
      return invokeJoinpoint();
   }
	//获取拦截器链里面的拦截器advice
   Object interceptorOrInterceptionAdvice =
         this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
		//省略多余代码	。。。
   
      // It's an interceptor, so we just invoke it: The pointcut will have
      // been evaluated statically before this object was constructed.
       //如果当前增强是一个interceptor直接调用这个invoke方法
       //就是advice的Invoke方法
      return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
  
}
```

## 拦截器（Advice）链

interceptorsAndDynamicMethodMatchers就是这个拦截器链，那么这个存放inteceprot或者advice的List集合是如何配置的呢？

我们回到jdkDynamicAopProx的invoke方法和CglibAopProxy的intercept()方法

这一段代码就是获取所有增强的地方

```java

//advised 可以是ProxyFactoryBean或ProxyFactory等
//返回值是advice对象
List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
```

advised对象是一个AdvisedSupport类型的

最终实在DefaultAdvisorChainFactory类里面完成的这一系列过程

```java
@Override
public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
      Advised config, Method method, @Nullable Class<?> targetClass) {

   // This is somewhat tricky... We have to process introductions first,
   // but we need to preserve order in the ultimate list.
    //这是注册器，利用它来对xml中配置的advice，加入list
   AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
   Advisor[] advisors = config.getAdvisors();
    //首先设置一个集合，长度是通知器的数量
    //就是XML中ProxyFactoryBean的interceptNames属性
    //advisor有多少个就有多少个advice是一一对应的
   List<Object> interceptorList = new ArrayList<>(advisors.length);
   Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
   Boolean hasIntroductions = null;

   for (Advisor advisor : advisors) {
      if (advisor instanceof PointcutAdvisor) {
         // Add it conditionally.
         PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
         if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
            MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
            boolean match;
            if (mm instanceof IntroductionAwareMethodMatcher) {
               if (hasIntroductions == null) {
                  hasIntroductions = hasMatchingIntroductions(advisors, actualClass);
               }
                //使用methodMatcher进行判断
               match = ((IntroductionAwareMethodMatcher) mm).matches(method, actualClass, hasIntroductions);
            }
            else {
               match = mm.matches(method, actualClass);
            }
             
            if (match) {
                //通过register获取上面说的advice
               MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
               if (mm.isRuntime()) {
                  // Creating a new object instance in the getInterceptors() method
                  // isn't a problem as we normally cache created chains.
                  for (MethodInterceptor interceptor : interceptors) {
                     interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
                  }
               }
               else {
                  interceptorList.addAll(Arrays.asList(interceptors));
               }
            }
         }
      }
      else if (advisor instanceof IntroductionAdvisor) {
         IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
         if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
            Interceptor[] interceptors = registry.getInterceptors(advisor);
            interceptorList.addAll(Arrays.asList(interceptors));
         }
      }
      else {
         Interceptor[] interceptors = registry.getInterceptors(advisor);
         interceptorList.addAll(Arrays.asList(interceptors));
      }
   }

   return interceptorList;
}
```

至于上面所说的advisor是怎么来的那么就ProxyFactoryBean而言就是在initializeAdvisorChain

```java
public Object getObject() throws BeansException {
    //初始化连接器链，
   initializeAdvisorChain();
   ....
}
private synchronized void initializeAdvisorChain() {
    。。。
        //通过getBean回调从factory中获取advice对象
       
    Object advice;
    if (this.singleton || this.beanFactory.isSingleton(name)) {
        // Add the real Advisor/Advice to the chain.
        advice = this.beanFactory.getBean(name);
    }
     //然后为advisor配置
    addAdvisorOnChainCreation(advice);
}
//把advice添加到advisor里面去
private void addAdvisorOnChainCreation(Object next) {
		// We need to convert to an Advisor if necessary so that our source reference
		// matches what we find from superclass interceptors.
		addAdvisor(namedBeanToAdvisor(next));
	}

```

## advice的原理

```java
//单例模式,返回一个DefaultAdvisorAdapterRegistry对象
AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
```

### 适配advice(适配器模式)

正是DefaultAdvisorAdapterRegistry里面配置了一些适配器，才为aop的advice提供了编织能力。

它维护了三种适配器，用来适配advisor和advice

```java
//添加这些通知的适配器
public DefaultAdvisorAdapterRegistry() {
   registerAdvisorAdapter(new MethodBeforeAdviceAdapter());
   registerAdvisorAdapter(new AfterReturningAdviceAdapter());
   registerAdvisorAdapter(new ThrowsAdviceAdapter());
}
//把advice或者inteceptor包装成 advisor
	@Override
	public Advisor wrap(Object adviceObject) throws UnknownAdviceTypeException {
		if (adviceObject instanceof Advisor) {
			return (Advisor) adviceObject;
		}
		if (!(adviceObject instanceof Advice)) {
			throw new UnknownAdviceTypeException(adviceObject);
		}
		Advice advice = (Advice) adviceObject;
        //如果是Inteceptor不需要适配
		if (advice instanceof MethodInterceptor) {
			// So well-known it doesn't even need an adapter.
			return new DefaultPointcutAdvisor(advice);
		}
        //如果是AfterReturningAdvice或者MethodBeforeAdvice
        //循环遍历，查看是否支持适配
		for (AdvisorAdapter adapter : this.adapters) {
			// 使用adaptor的support方法来构造advisor
			if (adapter.supportsAdvice(advice)) {
				return new DefaultPointcutAdvisor(advice);
			}
		}
		throw new UnknownAdviceTypeException(advice);
	}
//从适配器里获取拦截器，这些拦截器都必须是Inteceptor的形式
	@Override
public MethodInterceptor[] getInterceptors(Advisor advisor) throws UnknownAdviceTypeException {
		List<MethodInterceptor> interceptors = new ArrayList<>(3);
		Advice advice = advisor.getAdvice();
		if (advice instanceof MethodInterceptor) {
			interceptors.add((MethodInterceptor) advice);
		}
		for (AdvisorAdapter adapter : this.adapters) {
			if (adapter.supportsAdvice(advice)) {
				interceptors.add(adapter.getInterceptor(advisor));
			}
		}
		if (interceptors.isEmpty()) {
			throw new UnknownAdviceTypeException(advisor.getAdvice());
		}
		return interceptors.toArray(new MethodInterceptor[0]);
	}

```

再看看这些具体的适配器

```java
//AfterReturningAdviceAdapter
//MethodBeforeAdviceAdapter
//ThrowsAdviceAdapter

以及他们需要实现的方法
class MethodBeforeAdviceAdapter implements AdvisorAdapter, Serializable {

	@Override
	public boolean supportsAdvice(Advice advice) {
		return (advice instanceof MethodBeforeAdvice);
	}

	@Override
	public MethodInterceptor getInterceptor(Advisor advisor) {
		MethodBeforeAdvice advice = (MethodBeforeAdvice) advisor.getAdvice();
		return new MethodBeforeAdviceInterceptor(advice);
	}

}
//再看看Interceptor
public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice, Serializable {

	private final MethodBeforeAdvice advice;


	/**
	 * Create a new MethodBeforeAdviceInterceptor for the given advice.
	 * @param advice the MethodBeforeAdvice to wrap
	 */
	public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
		Assert.notNull(advice, "Advice must not be null");
		this.advice = advice;
	}


	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
		return mi.proceed();
	}

}
```

可以得出结论spring AOP为了实现advice的织入，设计了拦截器封装这些功能,所以advice回首先变成inteceptor，然后执行拦截器的invoke()方法时候回出发advice的回调。

有了这些封装，我们就可以直接调方法获取拦截器，加入拦截器链执行拦截器了。