package cn.cloudcharts.common.utils;

import cn.cloudcharts.common.exception.ServiceException;
import org.springframework.util.Assert;

import java.util.Collection;

import static org.apache.commons.lang3.ArrayUtils.isEquals;

public class AssertUtil{

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
    public static void checkNotNull(Object object, String msg) {
        if (isNull(object)) {
            throw new ServiceException(msg);
        }
    }

    public static void checkNullString(String key, String msg) {
        if (isNull(key) || isEquals("", key)) {
            throw new ServiceException(msg);
        }
    }

    public static boolean isEqualsIgnoreCase(String str1, String str2) {
        return (str1 == null && str2 == null) || (str1 != null && str1.equalsIgnoreCase(str2));
    }
    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNullString(String str) {
        return isNull(str) || str.isEmpty();
    }

    public static boolean isNotNullString(String str) {
        return !isNullString(str);
    }

}
