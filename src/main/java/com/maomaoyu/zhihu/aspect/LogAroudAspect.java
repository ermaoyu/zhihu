package com.maomaoyu.zhihu.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * maomaoyu    2018/12/14_18:17
 **/
@Aspect
@Component
public class LogAroudAspect {
    private static  final Logger LOGGER = LoggerFactory.getLogger(LogAroudAspect.class);

    /**
     *  环绕通知
     * */
    @Around("execution(* com.maomaoyu.zhihu.service.*.*(..))")
    public Object showAround(ProceedingJoinPoint joinPoint){
        Object res = null;
        //获取方法参数
        StringBuilder sb = new StringBuilder();
        for (Object args : joinPoint.getArgs()){
            sb.append("arg : " + args.toString());
        }

        try {
            //前置通知
            LOGGER.info("before method: " + sb.toString());
            res = joinPoint.proceed();
            LOGGER.info("返回通知 返回值结果为: " + Arrays.asList(res) + "--返回时间为:--" + new Date());

        } catch (Throwable throwable) {
            LOGGER.error("异常通知--" + "异常信息"+ throwable.getMessage() + "--发生时间--"+ new Date());
        }finally {
            LOGGER.info("方法结束---" + new Date());
        }
        return res;
    }

}
