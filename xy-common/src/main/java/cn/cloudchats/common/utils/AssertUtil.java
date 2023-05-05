package cn.cloudchats.common.utils;

import cn.cloudchats.common.exception.ServiceException;

import java.util.Collection;

/**
 * @author pengxu
 * @date 2022/9/7
 */
public class AssertUtil {

    public static void isBlank(String value, String msg) {
        if (StringUtils.isBlank(value)) {
            throw new ServiceException(msg);
        }
    }

    public static void isNotBlank(String value, String msg) {
        if (StringUtils.isNotBlank(value)) {
            throw new ServiceException(msg);
        }
    }

    public static void isNull(Object value, String msg) {
        if (value == null) {
            throw new ServiceException(msg);
        }
    }

    public static void isNotNull(Object value, String msg) {
        if (value != null) {
            throw new ServiceException(msg);
        }
    }

    public static void isNotNull(Collection<?> collection, String msg) {
        if (collection != null && !collection.isEmpty()) {
            throw new ServiceException(msg);
        }
    }

    public static void isNull(Collection<?> collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new ServiceException(msg);
        }
    }
}
