package cn.cloudchats.common.core.domain;

import cn.cloudchats.common.constant.HttpStatus;
import cn.cloudchats.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;

/**
 * 操作消息提醒
 *
 * @author wuque
 */
@ApiModel(value = "操作消息")
public class ResResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /** 执行时间戳 */
    public static final String TIMESTAMP_TAG = "timestamp";

    /** 执行时间 */
    public static final String EXECUTETIME_TAG = "executeTime";

    /** 异常信息 */
    public static final String EXCEPTION_TAG = "exception";

    /** 执行URL */
    public static final String ACTION_TAG = "action";


    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public ResResult()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     */
    public ResResult(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(TIMESTAMP_TAG,System.currentTimeMillis());
    }

    /**
     * 初始化一个新创建的 ResResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public ResResult(int code, String msg, Object data)
    {
        super.put(TIMESTAMP_TAG,System.currentTimeMillis());
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     * @param exception 异常信息
     */
    public ResResult(int code, String msg, Object data,String exception)
    {
        super.put(TIMESTAMP_TAG,System.currentTimeMillis());
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(EXCEPTION_TAG, exception);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ResResult success()
    {
        return ResResult.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ResResult success(Object data)
    {
        return ResResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ResResult success(String msg)
    {
        return ResResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ResResult success(String msg, Object data)
    {
        return new ResResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static ResResult error()
    {
        return ResResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResResult error(String msg)
    {
        return ResResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResResult error(String msg, Object data)
    {
        return new ResResult(HttpStatus.ERROR, msg, data);
    }

     /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResResult error(int code, String msg)
    {
        return new ResResult(code, msg, null);
    }


    /**
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static ResResult error(int code, String msg, Object data)
    {
        return new ResResult(code, msg, data);
    }

    /**
     *
     * @param code
     * @param msg
     * @param data
     * @param exception
     * @return
     */
    public static ResResult error(int code, String msg, Object data,String exception)
    {
        return new ResResult(code, msg, data, exception);
    }

    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public ResResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}
