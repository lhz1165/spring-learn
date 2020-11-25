[<<<Returan to Spring事务](../spring-事务.md)
# 关于spring事务研究

一般来说，一个controller调用一个service，要是service1调用service的事务方法，那也只是涉及到事务的传播行为的问题，但是service内部会调用自身的事务方法，但这可能造成事务不回滚，属于自生调用的问题。



基于以上问题研究什么情况下自身调用回回滚以及如何解决。

```java
@PostMapping("/test01")
public CommonResult test() throws Exception {
    //调用一个service
    userService.addRequire(new User("123","456"));
    return CommonResult.succeed("666");
}

-----------------------------------------------------------------------------------------
[userService]
   //两个内部方法，一个被一个外部的service调用
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
  @Override
    public void addRequire2(User user) {
        save(user);
    }
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void addRequiteException(User user) throws Exception {
        save(user);
        throw new Exception("123");
    }
```

## 没有try-catch

情景零：外部事物方法不加注解

```
@Override
//@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
public void addRequire(User user) throws Exception {

        addRequire2(user);
        user.setUsername("123");
        addRequiteException(user);


}
```

结果，全部不回滚

情景一：外部方法事务注解

```java
@Override
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
public void addRequire(User user) throws Exception {
        addRequire2(user);
        user.setUsername("123");
        addRequiteException(user);
}
```

结果：回滚

## 内部try-catch

情景二：内部try-catch

```java
@Override
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
public void addRequire(User user) throws Exception {
    try {
        addRequire2(user);
        user.setUsername("123");
        addRequiteException(user);
    } catch (Exception e) {

    }

}
```

结果都不回滚

情景三：注入自己，并且外部事务不加注解

```java
@Override
public void addRequire(User user) throws Exception {
    try {
        userService.addRequire2(user);
        user.setUsername("123");
        userService.addRequiteException(user);
    } catch (Exception e) {

    }

}
```

结果：第一个方法不回滚，第二个回滚。



情景三：注入自己，并且外部事务注解

```java
@Override
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
public void addRequire(User user) throws Exception {
    try {
        userService.addRequire2(user);
        user.setUsername("123");
        userService.addRequiteException(user);
    } catch (Exception e) {

    }

}
```

结果：理想状态正常，都回滚

## 内部抛出exception

```java
@Override
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
public void addRequire(User user) throws Exception {
    try {
        userService.addRequire2(user);
        user.setUsername("123");
        userService.addRequire2(user);
        throw new Exception("123");
    } catch (Exception e) {
        
    }

}
```

都不回滚

```java
@Override
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
public void addRequire(User user) throws Exception {
    try {
        userService.addRequire2(user);
        user.setUsername("123");
        throw new Exception("123");
    } catch (Exception e) {
        e.printStackTrace();
        throw new Exception(e.getMessage());
    }

}
```

这样才会都回滚