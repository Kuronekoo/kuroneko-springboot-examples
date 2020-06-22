package cn.kuroneko.demos.aspect;

import cn.kuroneko.demos.annotation.ParamLogging;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-05-28 00:11
 **/
@Aspect
@Component
public class ParamLoggingAspect {

    @Pointcut("@annotation(cn.kuroneko.demos.annotation.ParamLogging)")
    public void paramLoggingPointcut(){};


    @Around("paramLoggingPointcut()")
    public Object doParamLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        if(joinPoint instanceof MethodInvocationProceedingJoinPoint){
            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
            ParamLogging paramLogging = methodSignature.getMethod().getAnnotation(ParamLogging.class);
            if(Objects.nonNull(paramLogging) && StringUtils.isNotBlank(paramLogging.value())){
                    methodName = paramLogging.value();
            }
        }
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        final Logger log = LoggerFactory.getLogger(targetClass);
        log.info("method :  {}  inParam : {} " ,methodName, JSON.toJSONString(args));
        Object proceed = joinPoint.proceed();
        if(Objects.nonNull(proceed)){
            log.info("method :  {} outParam : {}" ,methodName, JSON.toJSONString(proceed));
        }
        return proceed;
    }

}
