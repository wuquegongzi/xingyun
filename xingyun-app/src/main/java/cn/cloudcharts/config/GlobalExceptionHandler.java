package cn.cloudcharts.config;

import cn.cloudcharts.common.constant.HttpStatus;
import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.common.exception.DemoModeException;
import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


/**
 * @author wuque
 * @title: GlobalExceptionHandler
 * @projectName xingyun
 * @description: 全局统一异常处理
 * @date 2023/5/1114:05
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                 HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return R.fail(HttpStatus.BAD_REQUEST,"请求方式不支持",e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? R.fail(code, "业务异常",e.getMessage()) : R.fail(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return R.fail("系统繁忙,请稍后再试");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return R.fail(HttpStatus.ERROR,"发生系统异常",e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public R handleHttpMessageNotWritableException(HttpMessageNotWritableException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getMessage();
        return R.fail(message);
    }



    /**
     * 参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleMethodIllegalArgumentException(IllegalArgumentException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getMessage();
        return R.fail(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public R handleDemoModeException(DemoModeException e)
    {
        return R.fail("演示模式，不允许操作");
    }
}
