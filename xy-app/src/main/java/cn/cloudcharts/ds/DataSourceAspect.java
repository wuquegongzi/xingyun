package cn.cloudcharts.ds;

import cn.cloudcharts.common.annotation.XyDataSource;
import cn.cloudcharts.common.enums.DataSourceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author wuque
 * @title: DataSourceAspect
 * @projectName xingyun
 * @description:
 * @date 2023/5/1311:02
 */
@Aspect
@Component
@Order(value = 1)
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(cn.cloudcharts.common.annotation.XyDataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        XyDataSource ds = method.getAnnotation(XyDataSource.class);
        if (ds == null) {
            DynamicDataSource.setDataSource(DataSourceType.OLAP.name().toLowerCase());
            logger.info("set datasource is " + DataSourceType.OLAP.name());
        } else {
            DynamicDataSource.setDataSource(ds.value().name().toLowerCase());
            logger.info("set datasource is " + ds.value().name());
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            logger.info("clean datasource");
        }
    }

}
