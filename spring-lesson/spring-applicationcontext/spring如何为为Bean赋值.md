[<<<Return To applicationContext](../spring的上下文拓展归纳.md)
# spring如何为为Bean赋值

```
<bean id="user" class="com.lhz.context.User">
    <property name="name" value="lhz"/>
    <property name="birthday" value="1997-10-14"/>
</bean>

class User {
    private String name;
    private Date birthday;
    ...
}
```

如果直接把 1997-10-14赋值给birthday，会有异常产生，那么如何把string类型转化为Date类型呢？

**这就需要我们自定义属性编辑器**

## 属性编辑器

要完成上述目标，这就需要我们自己定义类型转化工具，继承PropertyEditorSupport类重写setAsText方法

```
public class DatePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        System.out.println("text: " + text);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(text);
            this.setValue(date);
        } catch (Exception e) {

        }
    }
}
```

那么怎么知道我们的date属性就要用到这个转化工具呢，第二步需要准备date类型和关联转化器的映射

```
@Bean
public static CustomEditorConfigurer getConfig() {
    CustomEditorConfigurer configurer = new CustomEditorConfigurer();
    Map<Class<?>, Class<? extends PropertyEditor>> map = new HashMap<>();
    map.put(Date.class,DatePropertyEditor.class);
    configurer.setCustomEditors(map);
    return configurer;
}
```

以上，当检测到user的Date类型的值的时候，就回去找相对应的转化器，来把string---->date

### 详细步骤

根据之前我们知道属性赋值在populateBean()这个方法之中

```
if (pvs != null) {
   applyPropertyValues(beanName, mbd, bw, pvs);
}
进入applyPropertyValues()发现类型转化是在这一步发生的 string ----->date

if (convertible) {
	convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);
}
```
