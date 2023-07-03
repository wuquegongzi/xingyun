package cn.cloudcharts.aspectj;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.core.domain.ResResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuque
 * @title: JobRunTimeAop
 * @projectName xingyun
 * @description: 运行时长 切面
 * @date 2023/6/30 17:35
 */
@Aspect
@Component
public class ApiRunTimeAspect {

    private static Log logger = LogFactory.getLog(ApiRunTimeAspect.class);

    /**
     *  一分钟，即60000ms
      */
    private static final long ONE_MINUTE = 60000;

    @Pointcut(" execution(* cn.cloudcharts.controller..*(..))" )
    public void point() {
    }

    @Around("point()")
    public Object runTimeStatistics(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Long startTime = System.currentTimeMillis();

        Signature signature = pjp.getSignature();
        String methodName = signature.getName();

        Object[] requestParams = pjp.getArgs();
        Object response = pjp.proceed(requestParams);

        Long endTime = System.currentTimeMillis();
        long betweenTime = endTime - startTime;

        // 打印耗时的信息
        this.printExecTime(methodName, betweenTime);

        if(response instanceof R){
            R r = (R)response;
            r.setExecuteTime(betweenTime);
            r.setAction(request.getRequestURL().toString());
        }else if(response instanceof ResResult){
            ResResult r = (ResResult)response;
            r.put(ResResult.EXECUTETIME_TAG,betweenTime);
            r.put(ResResult.ACTION_TAG,request.getRequestURL().toString());
        }

        return response;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     * @param methodName
     * @param betweenTime
     */
    private void printExecTime(String methodName, long betweenTime) {
        if (betweenTime > ONE_MINUTE) {
            logger.warn("-----" + methodName + " 方法执行耗时：" + betweenTime + " ms");
        }
    }

}
