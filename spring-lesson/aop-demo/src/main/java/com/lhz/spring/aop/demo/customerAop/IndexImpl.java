package com.lhz.spring.aop.demo.customerAop;

/**
 * @author: lhz
 * @date: 2020/7/21
 **/
public class IndexImpl implements Index{

    @Override
    public int div(int i, int j) {
        System.out.println("div method invocation");
        return i+j;
    }
}
