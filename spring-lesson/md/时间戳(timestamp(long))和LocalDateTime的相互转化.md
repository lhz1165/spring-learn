# 时间戳(timestamp(long))和LocalDateTime的相互转化

**timestamp -> Timestamp->LocalDateTime**

1.   获取Timestamp对象:  Timestamp t = new Timestamp(timestamp )
2.  通过Timestamp 获取 LocaDateTime :  t.toLocalDateTime();

```java
//示例
public LocalDateTime toLocalDateTime1(long timestamp) {
    return new Timestamp(timestamp).toLocalDateTime();
}

```



**timestamp  -> Instant &ZoneId  ->  LocalDateTime**

1. 先获取Instant  
2. 再使用Instant  和 ZoneId,通过LocalDateTime的of()方法获取LocalDateTime.

```java
public LocalDateTime toLocalDateTime4(long timestamp) {
    /1
    Instant instant = Instant.ofEpochMilli(timestamp);
    ZoneId zoneId = ZoneId.systemDefault();
    //2
    return LocalDateTime.ofInstant(instant, zoneId);
 }

```



**timestamp  -> Instant&ZoneOffset   -> OffsetDateTime  -> LocalDateTime**

1.  先获取Instant和ZoneOffset(两种方式)   
   1.  ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();
   2. ZoneOffset zoneOffset = ZoneOffset.of("+8") ;
2.  再获取OffsetDateTime
3. 最后使用toLocalDateTime();

```java

public LocalDateTime toLocalDateTime2(long timestamp) {
    //1
    Instant instant = Instant.ofEpochMilli(timestamp);
    ZoneOffset zoneOffset = ZoneOffset.of("+8");
    //2
    OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
    //3
    return offsetDateTime.toLocalDateTime();
  }

  public LocalDateTime toLocalDateTime3(long timestamp) {
    //1
    Instant instant = Instant.ofEpochMilli(timestamp);
    ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();
    //2
    OffsetDateTime offsetDateTime = instant.atOffset(zoneOffset);
    //3
    return offsetDateTime.toLocalDateTime();
  }


```



**<u>总结</u>**  : 以上是根据long类型的时间戳获取LocalDateTime的方式，同样如果想把Date转化为LocalDateTime，我们根据上面的思路，可以把Date使用getTime()方法变成long类型的时间戳，快一点可以通过toInstan()方法直接把Date转化为Instant对象去获取LocalDateTime；

