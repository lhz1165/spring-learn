[Parent](../README.md)
# String的引用的探索

#### intern方法在1.7和1.6及之前差异

字面量：指的是String的值，两个字符串equals为true，字面量相同

1. jdk1.6及之前

- 如果常量池存在该字面量的字符串，返回这个常量池的对象引用
- 常量池不存在这个字面量的字符串，a.intern()会常量池创建字面量一样的字符串，返回常量池(新建)的对象引用

1. jdk1.7及之后

- 如果常量池存在该字面量的字符串，返回这个常量池的对象的引用（同1.6）
- 如常量池不存在字面量的对象，在常量池中记录首次出现的实例引用。调用intern()返回这个引用。

## 情况1

```java
public static void main(String[] args) {
    String s1 = new String("1234");
    String s2 = s1.intern();//
    String s3 = "1234";
     System.out.println(System.identityHashCode(s1));
    System.out.println(System.identityHashCode(s2));
    System.out.println(System.identityHashCode(s3));
}

结果
    
1164175787
290658609
290658609
```

因为创建s1的时候，堆上创建一个"1234",常量池也创建一个"1234",所以s1.intern()返回的是常量池的地址



## 情况2

```java
public static void main(String[] args) {
    String s1 = new String("123")+new String("4");
    String s2 = s1.intern();
    String s3 = "1234";

    System.out.println(System.identityHashCode(s1));
    System.out.println(System.identityHashCode(s2));
    System.out.println(System.identityHashCode(s3));
}
结果
    
1164175787
1164175787
1164175787
    //如果提前在常量池放入一个1234
 public static void main(String[] args) {

        String s1 = new String("123")+new String("4");
        String s4 = "1234";
        String s2 = s1.intern();
        String s3 = "1234";

        System.out.println(System.identityHashCode(s1));
        System.out.println(System.identityHashCode(s2));
        System.out.println(System.identityHashCode(s3));
   
    }
结果
1164175787
290658609
290658609
```

因为创建s1的时候，堆上创建一个"1234",常量池创建一个"123"和一个"4",

由于s1.intern()是在常量池中记录首次出现的实例引用，调用intern()返回这个引用。

所以就是返回堆上面的地址所以三个地址一样，

当提前放入了一个"1234"，所以intern直接返回常量池里面这个地址

## 情况3

```java
public static void main(String[] args) {
    String a = "123";
    String a1 = "4";
    
    String s1 = "1234";
    String s2 = a + a1;
    
    System.out.println(System.identityHashCode(s1));
    System.out.println(System.identityHashCode(s2));
  
}
结果
1164175787
290658609

 public static void main(String[] args) {
        String a = "123";
        String a1 = "4";

        String s1 = "1234";
        String s2 = "123" +"4";

        System.out.println(System.identityHashCode(s1));
        System.out.println(System.identityHashCode(s2));

    }
结果
1164175787
1164175787

```

 String s2 = a + a1;这样是在堆上产生了一个对象相当于new String("123")+new String("4");

而String s2 = "123" +"4";直接是使用常量池的