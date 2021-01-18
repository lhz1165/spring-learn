[<<<Returan to Spring事务](SpringBoot&SpringMvc的全局参数转化.md)
# 关于SpringBoot/SpringMvc  传入Json格式的参数的转化（JsonDeserializer&JsonSerializer）

## JsonDeserializer：用来解析前端传入类型为controller方法参数的类型（针对请求体）

如果前端使用POST请求

http://localhost:8080/lai2

{

  "dateTime":"2021-01-18T14:28:10"

}

```java
 @RequestMapping("/lai2")
    public Obj handleRequest22(@RequestBody Date1 dateTime){
        Obj o = new Obj();
        o.setDateTime(dateTime.getDateTime());
        return o;
    }
	//请求体体
    static class Date1{
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        LocalDateTime dateTime;

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;

        }
    }
	//返回体
    static class Obj{
        LocalDateTime dateTime;

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }

```

这样来传递参数是没问题的，因为Springboot 内置了JsonDeserializer把传递进来的 "dateTime":"2021-01-18T14:28:10"字符串解析成了LocalDateTime类型

如果不想使用UTC格式的字符串 而采用自定义的类型，需要在时间的变量上加上注解

```
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
LocalDateTime dateTime;
```

```java
package com.fasterxml.jackson.datatype.jsr310.deser;
public class LocalDateTimeDeserializer
    extends JSR310DateTimeDeserializerBase<LocalDateTime>{
    //默认的字符串解析类型UTC格式的时间 2021-01-18T14:28:10
    protected final DateTimeFormatter _formatter;
    
        @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {		
        	//前端传入的字符串
            String string = parser.getText().trim();
            	//默认采用UTC格式的字符串转化为LocalDateTime
	            if (_formatter == DEFAULT_FORMATTER) {
	            	return LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC);
                }
				//自定义的格式 例如yyyy-MM-dd HH:mm:ss转化为LocalDateTime
                return LocalDateTime.parse(string, _formatter);
    
    
}
```

但是如果传递请求体

{

  "dateTime":"1610951290000"

}

则会报异常，因为string 1610951290000无法转化成LocalDateTime

因此我们需要自定义JsonDeserializer，再注册到json转化器之中

```java
public static final class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    public static final LocalDateTimeDeserializer INSTANCE = new LocalDateTimeDeserializer();

    LocalDateTimeDeserializer() {
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        //时间戳
        String s = jsonParser.getText().trim();
        try {
            long timestamp = Long.parseLong(s) / 1000;
            return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(8));
        } catch (Exception ignored) {
            return LocalDateTime.parse(s);
        }
    }
}
```



## JsonSerializer：用来把controller返回的对象转化为json格式。（针对返回体）

返回收到这样的

{

  "dateTime": "2017-09-05T02:26:34"

}

因为内置的转化工具LocalDateTimeSerializer

```java
public class LocalDateTimeSerializer extends JSR310FormattedSerializerBase<LocalDateTime>
{
	 @Override
    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider)
        throws IOException
    {
            DateTimeFormatter dtf = _formatter;
            if (dtf == null) {
                //默认返回utc格式的日期字符串
                dtf = _defaultFormatter();
            }
            g.writeString(value.format(dtf));
        }
    }
}
```

如果我们想把LocalDateTime转化为自己想要的格式例如时时间戳，那么也需要自定义解析器

```java
public static class LocalDateTimeToStringSerializer extends JsonSerializer<LocalDateTime> {
    public final static LocalDateTimeToStringSerializer INSTANCE = new LocalDateTimeToStringSerializer();

    LocalDateTimeToStringSerializer() {
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        System.out.println(value);
        gen.writeString(String.valueOf(value.toInstant(ZoneOffset.ofHours(8)).toEpochMilli())+"???????");
    }
}
```

接收到的返回格式为

{

  "dateTime": "1504549594000???????"

}