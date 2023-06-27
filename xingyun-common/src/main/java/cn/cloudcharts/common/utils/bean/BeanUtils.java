package cn.cloudcharts.common.utils.bean;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean 工具类
 *
 * @author wuque
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    /** Bean方法名中属性名开始的下标 */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * 匹配getter方法的正则表达式 */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * 匹配setter方法的正则表达式 */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     *
     * @param dest 目标对象
     * @param src 源对象
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的setter方法。
     *
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     *
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     *
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }

    private static final String SEPARATOR = "_";

    /**
     * 嵌套对象转大map(扁平化)
     *
     * @param object 源对象
     * @return map
     */
    public static Map<String, Object> nestedObj2Map(Object object) {
        Map<String, Object> maps = JSON.parseObject(JSON.toJSONString(object), Map.class);
        Map<String, Object> result = new HashMap<>();
        maps.forEach((key, value) -> {
            common(maps, result, key, value, key);
        });
        return result;
    }

    /**
     * List嵌套对象转大list map(扁平化)
     *
     * @param objectList 源List对象
     * @return map
     */
    public static <T> List<Map<String, Object>> nestedObjList2ListMap(List<T> objectList) {
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        for (T t : objectList) {
            resultList.add(nestedObj2Map(t));
        }
        return resultList;
    }


    public static void common(Map<String, Object> maps, Map<String, Object> result, String key, Object value, String newKey) {
        if (maps.get(key) != null && value instanceof JSONObject) {
            Map<String, Object> subMaps = (Map) maps.get(key);
            Map<String, Object> map = nestedObj2Map(subMaps, newKey);
            if (map != null && !map.isEmpty()) {
                result.putAll(map);
            }
        } else {
            result.put(newKey, maps.get(key));
        }
    }

    public static Map<String, Object> nestedObj2Map(Map<String, Object> maps, String prefix) {
        Map<String, Object> result = new HashMap<>();
        String keyPrefix = prefix + SEPARATOR;
        maps.forEach((key, value) -> {
            String newKey = keyPrefix + key;
            common(maps, result, key, value, newKey);
        });
        return result;
    }

    public static Map<String, Object> bean2Map(Object bean) {

        Map<String, Object> map = new HashMap<>();
        BeanMap beanMap = BeanMap.create(bean);
        for (Object object : beanMap.entrySet()) {
            if (object instanceof Map.Entry) {
                Map.Entry<String , Object> entry = (Map.Entry<String, Object>)object ;
                String key = entry.getKey();
                map.put(key, beanMap.get(key));
            }
        }

        return map;
    }

}
