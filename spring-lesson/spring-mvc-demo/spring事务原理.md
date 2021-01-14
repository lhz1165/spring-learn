[<<<Returan to Spring事务](../spring-事务.md)
# spring事务原理



**TransactionInterceptor**：事务的拦截器，实现了MethodInterceptor即AOP的拦截器，代表着对方法的增强，其增强效果表示根据代码的执行情况判断回滚不回滚。

**TransactionDefinition**：里面封装的事务属性，例如传播行为，隔离级别

**TransactionStatus**：事务的状态，例如是否只读，是否有保存点，获取或判断事务的相应状态信息。

**TransactionInfo**：包含transactionManager，transactionAttribute，transactionStatus事务所有的基本属性

增强点在invoke()方法之中,关键代码如下

```java
// Standard transaction demarcation with getTransaction and commit/rollback calls.
TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
Object retVal = null;
try {
   // This is an around advice: Invoke the next interceptor in the chain.
   // This will normally result in a target object being invoked.
   retVal = invocation.proceedWithInvocation();
}
catch (Throwable ex) {
   // target invocation exception
   completeTransactionAfterThrowing(txInfo, ex);
   throw ex;
}
finally {
   cleanupTransactionInfo(txInfo);
}
commitTransactionAfterReturning(txInfo);
return retVal;
```

获取TransactionInfo就知道事务的执行情况了，所以用下面方法来做

```
TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
```

进入

```java
protected TransactionInfo createTransactionIfNecessary(
      PlatformTransactionManager tm, TransactionAttribute txAttr, final String joinpointIdentification) {

   // If no name specified, apply method identification as transaction name.
   if (txAttr != null && txAttr.getName() == null) {
      txAttr = new DelegatingTransactionAttribute(txAttr) {
         @Override
         public String getName() {
            return joinpointIdentification;
         }
      };
   }

   TransactionStatus status = null;
   if (txAttr != null) {
      if (tm != null) {
         status = tm.getTransaction(txAttr);
      }
      else {
         if (logger.isDebugEnabled()) {
            logger.debug("Skipping transactional joinpoint [" + joinpointIdentification +
                  "] because no transaction manager has been configured");
         }
      }
   }
   return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
}
```

status = tm.getTransaction(txAttr);这个方法就是执行事务的方法了

这是PlatformTransactionManager接口的方法，需要执行的事务管理器来执行。

