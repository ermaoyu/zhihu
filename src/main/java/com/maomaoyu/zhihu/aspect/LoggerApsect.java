package com.maomaoyu.zhihu.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Arrays;
import java.util.Date;

/**
 * maomaoyu    2018/12/14_18:02
 **/
@Aspect
@Component
public class LoggerApsect {
    private static  final Logger LOGGER = LoggerFactory .getLogger(LoggerApsect.class);

    //自定义的切入表达式
    //myPointCut:切入点表达式的名字
    @Pointcut(value = "execution(* com.maomaoyu.zhihu.service.*.*(..))")
    public void myPointCut(){}


    /**
     * @Before;前置通知
     * value=切入表达式 execution(* com.maomaoyu.zhihu.service.*.*(..))
     * */
    @Before(value = "myPointCut()")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()){
            sb.append("arg: " + arg.toString());
        }
        LOGGER.info("before method: " + sb.toString());
    }
    /**
     *   你的目标类的方法执行完毕后,就执行代码
     * */
    @AfterReturning(value = "myPointCut()",returning = "res")
    public void showLoading(JoinPoint joinPoint,Object res){
        LOGGER.info("返回通知 返回值结果为: " + Arrays.asList(res) + "--返回时间为:--" + new Date());
    }

    /**
     * @AfterThrowing: 异常通知,当执行的目标方法发生异常时执行
     * */
    @AfterThrowing(value = "myPointCut()",throwing = "myThrowing")
    public void  showException(JoinPoint joinPoint,Throwable myThrowing){
        LOGGER.error("异常通知--" + "异常信息"+ myThrowing.getMessage() + "--发生时间--"+ new Date());
    }

    /**
     * @After:最终通知
     */
    @After(value = "myPointCut()")
    public void afterMethod(){
        LOGGER.info("方法结束---" + new Date());
    }
}
