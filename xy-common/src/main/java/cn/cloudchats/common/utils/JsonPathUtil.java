package cn.cloudchats.common.utils;

import com.jayway.jsonpath.JsonPath;

import java.util.Map;

/**
 * @author wuque
 * @title: JsonPathUtil
 * @projectName datan-saas-common
 * @description:
 * @date 2022/3/37:16 PM
 */
public class JsonPathUtil {


    /**
     * 从多层嵌套Json中解析所需要的值
     * @param json
     * @param path
     * @return
     */
    public static String readValUsingJsonPath(String json, String path) {
        if (json == null || path == null) {
            return null;
        }
        try {
            Object val = JsonPath.read(json, "$." + path);
            return val == null ? null : val.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 对于正确JSON及存在的Path下获取到最终指定值并转成字符串，其他情况一律返回 null
     * @param json JSON串
     * @param path 点分隔的字段路径
     * @return 相应字段的字符串值
     */
    @Deprecated
    public static String readVal(String json, String path) {
        if (json == null || path == null) {
            return null;
        }
        Map<String,Object> map = GsonUtils.gsonToMaps(json);
        if (map == null) {
            // log.warn("parse json failed: " + json);
            return null;
        }
        String[] subpaths = path.split("\\.");
        return readVal(map, subpaths);
    }
    private static String readVal(Map<String, Object> map, String path) {
        return readVal(map, path.split("\\."));
    }
    private static String readVal(Map<String, Object> map, String[] subpaths) {
        Object val = map;
        try {
            for (String subpath: subpaths) {
                if (val != null && val instanceof Map) {
                    val = ((Map)val).get(subpath);
                }
                else {
                    // log.warn("subpath may not exists in " + map);
                    return null;
                }
            }
            return val == null ? null: val.toString();
        } catch (Exception ex) {
            return null;
        }
    }
}
