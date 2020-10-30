package com.lhz.spring.aop.demo;

import com.lhz.spring.aop.demo.advice.CountingAdvice;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.util.Arrays;

/**
 * 切面类
 *
 * @author lfy
 * <p>
 * 你要餐厅点餐，菜单上的菜都是joinPoint，然后最后你选择的菜就是PointCut
 * @Aspect： 告诉Spring当前类是一个切面类
 * <p>
 * 使用order 优先级越高before越先执行，after越后执行
 */
@Aspect
public class LogAspects implements Ordered {
    //抽取公共的切入点表达式
    //1、本类引用
    //2、其他的切面引用
    //切点 用来匹配我们的joinPoint（这里匹配到了div(int i,int j)）
    // joinPoint是MathCalculator类潜在的可能被执行增强的方法，例如public int div(int i,int j)
    @Pointcut("execution(public * com.lhz.spring.aop.demo.MathCalculator2.*(..))")
    public void pointCut() {
    }

    ;

    //@Before在目标方法之前切入；切入点表达式（指定在哪个方法切入）

    /**
     * 下面都是advice
     *
     * @param joinPoint
     */
//    @Before("pointCut()")
//    public void logStart(JoinPoint joinPoint) {
//        Object[] args = joinPoint.getArgs();
//        System.out.println("" + joinPoint.getSignature()
//                .getName() + "运行。。。@Before111111111111111:参数列表是：{" + Arrays.asList(args) + "}");
//    }
//
//    @After("pointCut()")
//    public void logEnd(JoinPoint joinPoint) {
//        System.out.println("" + joinPoint.getSignature()
//                .getName() + "结束。。。@After11111111111111111111");
//    }

    @Override
    public int getOrder() {
        return 4;
    }

//    //JoinPoint一定要出现在参数表的第一位
    @AfterReturning(value="pointCut()",returning="result")
    public void logReturn(JoinPoint joinPoint,Object result){
        System.out.println(""+joinPoint.getSignature().getName()+"正常返回。。。@AfterReturning:运行结果：{"+result+"}");
    }

    //    @AfterThrowing(value="pointCut()",throwing="exception")
//    public void logException(JoinPoint joinPoint,Exception exception){
//        System.out.println(""+joinPoint.getSignature().getName()+"异常。。。异常信息：{"+exception+"}");
//    }
    //advice [增强]
    @Bean
    public CountingAdvice advice() {
        return new CountingAdvice();
    }

}
