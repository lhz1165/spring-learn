# 多模块在tomcat运行springmvc项目失败

首先运行项目会出现异常

```java
09-Sep-2020 22:32:27.016 涓ラ噸 [RMI TCP Connection(3)-127.0.0.1] org.apache.catalina.core.StandardContext.listenerStart Error configuring application listener of class [org.springframework.web.context.ContextLoaderListener]
 java.lang.ClassNotFoundException: org.springframework.web.context.ContextLoaderListener
	at org.apache.catalina.loader.WebappClassLoaderBase.loadClass(WebappClassLoaderBase.java:1364)
	at org.apache.catalina.loader.WebappClassLoaderBase.loadClass(WebappClassLoaderBase.java:1185)
	at org.apache.catalina.core.DefaultInstanceManager.loadClass(DefaultInstanceManager.java:546)
	at org.apache.catalina.core.DefaultInstanceManager.loadClassMaybePrivileged(DefaultInstanceManager.java:527)
	at org.apache.catalina.core.DefaultInstanceManager.newInstance(DefaultInstanceManager.java:150)
	at org.apache.catalina.core.StandardContext.listenerStart(StandardContext.java:4692)



```

这是由于找不到jar包导致的我们可以

打开project Structure---》然后选择Artifacts------》选择右边的Avaliable Elements-----》选择当前模块---》右键put into output root 就导入jar包了