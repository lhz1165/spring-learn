package com.lhz.spring.aop.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAOP {

    //[目标对象]
    @Bean
    public MathCalculator2 calculator(){
        return new MathCalculator2();
    }

    //切面类加入到容器中
    @Bean
    public LogAspects logAspects(){
        return new LogAspects();
    }
    //切面类加入到容器中
    @Bean
    public LogAspects2 logAspects2(){
        return new LogAspects2();
    }

}