package com.lhz.spring.aop.demo.advice;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lhz
 * @date: 2020/8/26
 **/
public class MethodCounter implements Serializable {
    private Map<String, Integer> map = new HashMap<>();
    //所有方法的次数
    private int allCount;

    //CountingBeforeAdvice的调用入口
    protected void count(Method method) {
        count(method.getName());
    }

    protected void count(String methodName) {
        Integer times = map.get(methodName);
        times = (times != null) ? new Integer(times + 1) : new Integer(1);
        allCount++;
        map.put(methodName, times);
    }

    public Integer getCalls(Method method) {
        return map.get(method.getName()) == null ? 0 : map.get(method.getName());
    }
}
