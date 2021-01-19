[<<<Returan to SpringBoot&SpringMvc的全局参数转化](SpringBoot&SpringMvc的全局参数转化.md)
# 关于SpringBoot/SpringMvc参数转化的探索

```
@RequestMapping("/hello2")
@ResponseBody
public LocalDateTime handleRequest2(@RequestParam LocalDateTime dateTime){
    return dateTime;
}
```

如果我在浏览器输入http://localhost:8080/hello2?dateTime=2017-02-12T12:00:00
此时访问不到这个请求，因为此时dateTime=2017-02-12T12:00:00为字符串类型，不能变成LocalDateTime dateTime。

使用自带的类型转化工具，因为这个不是json格式的请求体，所以不能使用时@JsonFormat。

如何解决呢

方法一：使用自带的工具，默认需要传递UTC格式的字符串

```
@RequestMapping("/lai")
/**
		 * The most common ISO DateTime Format {@code yyyy-MM-dd'T'HH:mm:ss.SSSXXX},
		 * e.g. "2000-10-31T01:30:00.000-05:00".
		 * <p>This is the default if no annotation value is specified.
		 */
public LocalDateTime handleRequest2(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){
    return dateTime;
}
```



```java


public final class TemporalAccessorParser implements Parser<TemporalAccessor> {
@Override
public TemporalAccessor parse(String text, Locale locale) throws ParseException {
   DateTimeFormatter formatterToUse = DateTimeContextHolder.getFormatter(this.formatter, locale);
	//.....省略其他类型
   if (LocalDateTime.class == this.temporalAccessorType) {
      return LocalDateTime.parse(text, formatterToUse);
   }
    //.....省略其他类型
   }
 }
}
```



方法二：自定义Convert
如果前端约定传递时间戳字符换，例如http://localhost:8080/lai?dateTime=1610962256000

```java
@Component
public class Timestamp2LocaldateTimeConvert implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        Instant instant = Instant.ofEpochMilli(Long.parseLong(source));
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
```

## 原理

公共的流程是这样的

在RequestParamMethodArgumentResolver参数解析器，首先调用convertIfNecessary来解析参数

1.使用**ConversionService**对象来找到对应的**GenericConverter**，

2.再调用GenericConverter的convert方法（自定义和自带的就取决于具体的GenericConverter实现）

```
//
arg = binder.convertIfNecessary(arg, parameter.getParameterType(), parameter);
```

这里面做的事情是获取SimpleTypeConverter然后调用它的convertIfNecessary()方法解析

```java
public <T> T convertIfNecessary(@Nullable String propertyName, @Nullable Object oldValue, @Nullable Object newValue,
      @Nullable Class<T> requiredType, @Nullable TypeDescriptor typeDescriptor) throws IllegalArgumentException {
	
   ConversionFailedException conversionAttemptEx = null;

   // 找到ConversionService
   ConversionService conversionService = this.propertyEditorRegistry.getConversionService();
  	
      TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
     	//调用它的convert
      return (T) conversionService.convert(newValue, sourceTypeDesc, 
   }
```

**ConversionService**的convert

```java
      
//2017-02-12T12:00:00字符串
//前端传递的String类型
//目标类型LocalDateTime
@Override
	@Nullable
	public Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		//1 找到GenericConverter
		GenericConverter converter = getConverter(sourceType, targetType);
		//2 执行GenericConverter的convert方法
			Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
			return handleResult(sourceType, targetType, result);
	}
```

最终就在invokeConverter方法里做

```java
@Nullable
public static Object invokeConverter(GenericConverter converter, @Nullable Object source,
      TypeDescriptor sourceType, TypeDescriptor targetType) {
      return converter.convert(source, sourceType, targetType);

}
```

### 自带DateTimeFormat解析的原理：

invokeConverter里面参数的最终找到的是ParserConverter

这里找到的是ParserConverter,里面依赖了一个Parser，这个Parser就是解析工具

```java
private static class ParserConverter implements GenericConverter {
   private final Parser<?> parser;
   @Override
		@Nullable
		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			String text = (String) source;
				result = this.parser.parse(text, LocaleContextHolder.getLocale());
			return result;
		}
}

```

```java
@Override
public TemporalAccessor parse(String text, Locale locale) throws ParseException {
   DateTimeFormatter formatterToUse = DateTimeContextHolder.getFormatter(this.formatter, locale);
    //...
    if (LocalTime.class == this.temporalAccessorType) {
      return LocalTime.parse(text, formatterToUse);
   }
    //...
```

### 自定义时间解析器的原理：

invokeConverter里面的Convert就是我们自定义的实现类,所以直接走进来

```java
@Component
public class Timestamp2LocaldateTimeConvert implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        try {
            Instant instant = Instant.ofEpochMilli(Long.parseLong(source));
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (Exception e) {
            return null;
        }
    }
}
```

### 补充：

在其中我们只能使用一种转化器，一旦定义好了默认的就会被替换，原因是

```java
GenericConverter converter = getConverter(sourceType, targetType);
```

在获取convert方法中,需要用source类型和target类型，组成的key对象，例如string->localdatetime，然后去map里面get，这个ConverterCacheKey重写了hashcode和equlas方法，put进去就替换掉了，所以对于同一种类型的转换只能存在一种convert

```java
ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
GenericConverter converter = this.converterCache.get(key);
```
