[<<<Returan to Spring事务](../spring-事务.md)
# Spring的事务-1

本章节，介绍一下spring事务所用到的基本类以及作用。

## TransactionStatus

用来表示事务的状态以编程方式请求回滚（而不是抛出导致隐式回滚的异常)；

继承了**SavepointManager**可以让这个接口让它拥有创建savePoint，rollbackToSavepoint，releaseSavepoint

的功能。

它自身有着setRollbackOnly，让这个事务必须回滚，防止try- catch出抛出异常就不回滚。以及判断是否有回滚点，是否RollbackOnly等方法。

### 主要的实现类**DefaultTransactionStatus**

持有transaction对象，实际上这就是SavepointManager的子类(mysql使用的的**DataSourceTransactionManager.DataSourceTransactionObject**)

还有一些全局变量和接口的判断方法有关。

以及通过transaction对象，调用底层的Connection接口的setSavepoint()方法来设置SavePoint；

```java
public Object createSavepoint() throws TransactionException {
    ConnectionHolder conHolder = this.getConnectionHolderForSavepoint();
	return conHolder.createSavepoint();
}
ConnectionHolder.java中
    public Savepoint createSavepoint() throws SQLException {
        ++this.savepointCounter;
        return this.getConnection().setSavepoint("SAVEPOINT_" + this.savepointCounter);
    }

```



## TransactionDefinition

定义事务传播行为和隔离级别和超时的接口，例如

```java
int PROPAGATION_REQUIRED = 0;
int PROPAGATION_SUPPORTS = 1;
...
int ISOLATION_DEFAULT = -1;
...
//使用底层交易系统的默认超时。如果不支持超时，则为无
int TIMEOUT_DEFAULT = -1;
```

**TransactionAttribute**继承了TransactionDefinition，这个接口为TransactionDefinition增加了一个代码rollbackOn规范。



## TransactionInfo

保存交易信息的类，内部包含PlatformTransactionManager，TransactionAttribute，TransactionStatus，等所有事务有关的信息。



## TransactionAspectSupport

事务有切面的基本功能类，它可以让spring底层的事务基础设置很容易的被使用去实现切面，针对不同的PlatformTransactionManager使用了策略模式去执行事务管理，

通aop的增强方法来实现事务管理

```java
@Override
@Nullable
public Object invoke(MethodInvocation invocation) throws Throwable {
   // Work out the target class: may be {@code null}.
   // The TransactionAttributeSource should be passed the target class
   // as well as the method, which may be from an interface.
   Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);

   // Adapt to TransactionAspectSupport's invokeWithinTransaction...
   return invokeWithinTransaction(invocation.getMethod(), targetClass, invocation::proceed);
}
```