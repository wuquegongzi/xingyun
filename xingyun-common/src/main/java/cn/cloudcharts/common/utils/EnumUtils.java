package cn.cloudcharts.common.utils;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;


/**
 * @author wuque
 * @title: EnumUtils
 * @projectName xingyun
 * @description: 枚举工具类
 * @date 2023/6/15
 */
public class EnumUtils<V> extends EnumUtil {

    private final static  EnumUtils enumUtils = new EnumUtils();

    /**
     * 根据属性名获取enum
     * @param clazz
     * @param fieldName
     * @param fieldValue
     * @param <T>
     * @return
     */
    public <T> T getEnumByFiled(Class<T> clazz, String fieldName, V fieldValue) {
        if (clazz == null || StringUtils.isBlank(fieldName) || ObjectUtil.isNull(fieldValue)|| !clazz.isEnum()) {
            return null;
        }

        // 获取实体类的所有属性
        Field[] fields = clazz.getDeclaredFields();

        Type type = null;
        for (Field field : fields) {
            if(field.getName().equals(fieldName)){
                //获取属性类型
                type = field.getGenericType();
                break;
            }
        }

        T[] objs = (T[]) clazz.getEnumConstants();
        for (int i = 0; i < objs.length; i++) {
            try {
                Method m = null;
                try {
                    m = clazz.getMethod("get"+getMethodName(fieldName));
                } catch (NoSuchMethodException e) {
                    m = clazz.getMethod(fieldName.toLowerCase());
                }

                if(fieldValue instanceof  Integer && null != type && type.toString().equals("class java.lang.Integer")){
                    if (fieldValue == m.invoke(objs[i])) {
                        return objs[i];
                    }
                }else if(null != type && type.toString().equals("class java.lang.String")){
                    if (StrUtil.toString(fieldValue).equals(m.invoke(objs[i]))) {
                        return objs[i];
                    }
                }else{
                    if (fieldValue.equals(m.invoke(objs[i]))) {
                        return objs[i];
                    }
                }

            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 通过code 获取枚举
     *     枚举必须有code值，枚举类必须有getCode()方法
     *     不建议使用
     * @param clazz
     * @param value
     * @return
     */
    public static <T> T getEnumByCode(Class<T> clazz, String value) {
        return (T)enumUtils.getEnumByFiled(clazz,"code",value);
    }

    /**
     * 通过code 获取枚举
     *     枚举必须有code值，枚举类必须有getCode()方法
     *     不建议使用
     * @param clazz
     * @param value
     * @return
     */
    public static <T> T getEnumByCode(Class<T> clazz, Integer value) {
        return (T)enumUtils.getEnumByFiled(clazz,"code",value);
    }

    /**
     * 把一个字符串的第一个字母大写
     */
    private static String getMethodName(String fildeName){
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }


}
