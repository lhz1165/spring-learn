[<<<Returan to SpringMvc](../spring-mvc归纳.md)
# Spring事务的传播行为研究

## 场景1. Propagation.REQUIRED

### 场景1-1（不开启外围事务）

当事务的传播行为propagation = Propagation.REQUIRED表示当前没有事务，就新建一个，如果存在在一个事务那么加入这个事务

```java
 @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void addRequiteException(User user) throws Exception {
        save(user);
        throw new Exception("11111");
    }
 @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void addRequire(User user) {
        save(user);
    }

----------------------------------------------------------------------------------------
@Test
void testPropagation() throws Exception {
    User user = new User("1233", "4536");
    userService.addRequire(user);
    userService.addRequiteException(user);
}
```

结果： 一个插入成功，一个插入失败

**<u>结论:在一个函数内部，如果外部函数开启事务，内部函数会开启互相独立的事务，互不影响，即使外部函数抛出异常也不会英雄其他内部</u>**

### 场景1-2(开启外围事务)

但是 只要外围方法开始事务，只要感知到异常，全部回滚，统统不插入数据库。

```
@Transactional
@Test
void testPropagation() throws Exception {
    User user = new User("1233", "4536");
    userService.addRequire(user);
    userService.addRequiteException(user);
  
}
```

**<u>结论:在一个函数内部，如果外部函数开始事务，那么所有函数都在一个事务里面,只要内部抛出异常内部事务也一起回滚(就算try-catch也要回滚)</u>**

## 场景2 .Propagation.REQUIRES_NEW

### 场景2-1(不开启外围事务)

```java
@Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void addRequire(User user) {
        save(user);
    }
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void addRequireNew(User user) {
        save(user);
    }

    //@PointC
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void addRequiteException(User user) throws Exception {
        save(user);
        throw new Exception("11111");
    }
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void addRequiteExceptionNew(User user) throws Exception {
        save(user);
        throw new Exception("11111");
    }

----------------------------------------------------------------------------------------
    @Transactional
    @Test
    void testPropagation() throws Exception {
        User user = new User("1233new", "4536new");
        userService.addRequire(new User("123","456"));
        userService.addRequireNew(user);
        userService.addRequireNew(user);
        throw new Exception();
    }
    
```

要是传播行为替换成 Propagation.REQUIRES_NEW,其他都相同，结果还是如上，每个方法各自开启事务，互相不影响

### 场景2-2(开始外围事务)

```java
    @Transactional
    @Test
    void testPropagation() throws Exception {
        User user = new User("1233new", "4536new");
        userService.addRequire(new User("123","456"));
        userService.addRequireNew(user);
        userService.addRequireNew(user);
        throw new Exception();
    }

```

传播行为为REQUIRED回滚，REQUIRED_NEW的不回滚

```java
@Transactional
@Test
void testPropagation() throws Exception {
    userService.addRequire(new User("123","456"));//1
    userService.addRequireNew(new User("1233new", "4536new"));//2
    userService.addRequiteExceptionNew(new User("789","963"));//3
}
```

2插入成功，1，3失败回滚

**<u>结论:即使外部方法开启了事务，只要内部函数传播行为是Propagation.REQUIRES_NEW它还自己新开事务，与外部的不影响</u>**

## 场景3 . Propagation.NESTED

### 场景3-1(不开启外围事务)

与上面相同，还是各自运行在各自的事务之中内部函数互相不影响

### 场景3-2(开启外围事务)

```java
@Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED)
public void addRequireNESTED(User user) {
    save(user);
}
@Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED)
public void addRequiteExceptionNESTED(User user) throws Exception {
    save(user);
    throw new Exception("11111");
}
--------------------------------------------------------------------------------
    @Transactional
    @Test
    void testPropagationNest() throws Exception {
        userService.addRequireNESTED(new User("123","456"));
        userService.addRequiteExceptionNESTED(new User("1233new", "4536new"));
    }
    @Transactional
    @Test
    void testPropagationNest() throws Exception {
        userService.addRequireNESTED(new User("123","456"));
        throw new Exception();
    }
@Transactional
    @Test
    void testPropagationNest() throws Exception {
         userService.addRequireNESTED(new User("123","456"));
        try {
            userService.addRequiteExceptionNESTED(new User("789","963"));
        } catch (Exception e) {

        }
    }

```

test1和test2因为外部开启了事务，所以，内部事务变成了外部事务的子事务，所以主事务异常回滚，子事务跟着回滚

当外部函数没有出现异常，test3因为子事务为NEST各自回滚个的互相不影响。所以只能插入一条，第二条有异常的不插入

**<u>结论：在子事务为NESTED状态是，外围事务回滚，内部子事务也跟着回滚，但是要是外围不会滚，那么他们子事务之间互相独立不影响(try-catch了之后，没有抛出异常的子事务不回滚)</u>**



由于我这是在springboot项目里面操作的比较方便，所以没有贴出具体项目代码，建议不要单元测试，因为单元测试自带回滚