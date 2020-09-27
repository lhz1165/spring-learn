
[<<<Returan to SpringMvc](../spring-mvc归纳.md)

# SpringMVC的请求处理大概流程

springMVC的FrameworkServlet类重写了doGet（） 等http方法

```java
@Override
protected final void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

   processRequest(request, response);
}
```

进入processRequest(request, response);

首先做两件事情localeContext和requestAttributes 目的是让每次线程的作用域和上下文一样

核心方法在doService()

最后http请求执行完了向spring发布事件

```java
protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

   long startTime = System.currentTimeMillis();
   Throwable failureCause = null;

   LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
   LocaleContext localeContext = buildLocaleContext(request);

   RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
   ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);

   WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
   asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());

   initContextHolders(request, localeContext, requestAttributes);

   try {
       //核心方法
      doService(request, response);
   }
   catch (ServletException | IOException ex) {
      failureCause = ex;
      throw ex;
   }
   catch (Throwable ex) {
      failureCause = ex;
      throw new NestedServletException("Request processing failed", ex);
   }

   finally {
      resetContextHolders(request, previousLocaleContext, previousAttributes);
      if (requestAttributes != null) {
         requestAttributes.requestCompleted();
      }
      logResult(request, response, failureCause, asyncManager);
      publishRequestHandledEvent(request, response, startTime, failureCause);
   }
}
```

之前都是为作用域设置一些基本准备属性，

核心代码是doDispatch(request, response);

```
@Override
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
   logRequest(request);

	.....
   try {
      doDispatch(request, response);
   }
   finally {
      if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
         // Restore the original attribute snapshot, in case of an include.
         if (attributesSnapshot != null) {
            restoreAttributesAfterInclude(request, attributesSnapshot);
         }
      }
   }
}
```



## doDispatch执行需要的controller

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
   HttpServletRequest processedRequest = request;
   HandlerExecutionChain mappedHandler = null;
   boolean multipartRequestParsed = false;

   WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

   try {
      ModelAndView mv = null;
      Exception dispatchException = null;

      try {
         processedRequest = checkMultipart(request);
         multipartRequestParsed = (processedRequest != request);

         // Determine handler for the current request.
         mappedHandler = getHandler(processedRequest);
         if (mappedHandler == null) {
            noHandlerFound(processedRequest, response);
            return;
         }

         // Determine handler adapter for the current request.
         HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

         // Process last-modified header, if supported by the handler.
         String method = request.getMethod();
         boolean isGet = "GET".equals(method);
         if (isGet || "HEAD".equals(method)) {
            long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
            if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
               return;
            }
         }

         if (!mappedHandler.applyPreHandle(processedRequest, response)) {
            return;
         }

         // Actually invoke the handler.
         mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

         if (asyncManager.isConcurrentHandlingStarted()) {
            return;
         }

         applyDefaultViewName(processedRequest, mv);
         mappedHandler.applyPostHandle(processedRequest, response, mv);
      }
      catch (Exception ex) {
         dispatchException = ex;
      }
      catch (Throwable err) {
         // As of 4.3, we're processing Errors thrown from handler methods as well,
         // making them available for @ExceptionHandler methods and other scenarios.
         dispatchException = new NestedServletException("Handler dispatch failed", err);
      }
      processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
   }
   catch (Exception ex) {
      triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
   }
   catch (Throwable err) {
      triggerAfterCompletion(processedRequest, response, mappedHandler,
            new NestedServletException("Handler processing failed", err));
   }
   finally {
      if (asyncManager.isConcurrentHandlingStarted()) {
         // Instead of postHandle and afterCompletion
         if (mappedHandler != null) {
            mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
         }
      }
      else {
         // Clean up any resources used by a multipart request.
         if (multipartRequestParsed) {
            cleanupMultipart(processedRequest);
         }
      }
   }
}
```

### 获得HandlerExecutionChain

首先我们用HandlerExecutionChain来表示我们当前的匹配到的controller方法，这里面封装了一个HandlerMethod 和拦截器

```
private final Object handler;
```

```
@Nullable
private List<HandlerInterceptor> interceptorList;
```

在doDispatch里面是通过mappedHandler = getHandler(processedRequest);来获取的

具体是通过我们上下文中，配置的HandlerMapping的getHandler()里面嵌套的getHandlerInternal()的方法来获取

主要也是两步

1获取HandlerMethod 

2添加获取拦截器

3组装成HandlerExecutionChain

(23步都在getHandlerExecutionChain里面)

```java
@Nullable
protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
   if (this.handlerMappings != null) {
      for (HandlerMapping mapping : this.handlerMappings) {
         HandlerExecutionChain handler = mapping.getHandler(request);
         if (handler != null) {
            return handler;
         }
      }
   }
   return null;
}
```



```java
public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
   Object handler = getHandlerInternal(request);

   HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);

   return executionChain;
}
```

#### 先来看看getHandlerInternal()

第一步是先找到url  lookupPath()

第二步 使用lookupHandlerMethod()，通过url去匹配扫描到的controller

这样就获取到了HandlerMethod代表controller方法的类

```java
@Override
protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
   String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
   request.setAttribute(LOOKUP_PATH, lookupPath);
   this.mappingRegistry.acquireReadLock();
   try {
      HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
      return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
   }
   finally {
      this.mappingRegistry.releaseReadLock();
   }
}
```

#### 再来看看getHandlerExecutionChain()

通过url去匹配是否这个controller需要拦截器，然后把拦截器一个一个添加到HandlerExecutionChain的interceptorList变量里

```java
protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
   HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ?
         (HandlerExecutionChain) handler : new HandlerExecutionChain(handler));

   String lookupPath = this.urlPathHelper.getLookupPathForRequest(request, LOOKUP_PATH);
   for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
      if (interceptor instanceof MappedInterceptor) {
         MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptor;
         if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
            chain.addInterceptor(mappedInterceptor.getInterceptor());
         }
      }
      else {
         chain.addInterceptor(interceptor);
      }
   }
   return chain;
}
```

## 找到了对应的Handler(HandlerExecutionChain) 接着使用HandlerAdapter来执行请求



```
HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
// Actually invoke the handler.
mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
```





执行拦截器

```
mappedHandler.applyPostHandle(processedRequest, response, mv);
```
