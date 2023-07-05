
package cn.cloudcharts.metadata.convert;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.hutool.core.util.ObjectUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface ITypeConvert {

    default Object convertValue(ResultSet results, String columnName, String javaType)
            throws SQLException {
        if (AssertUtil.isNull(javaType)) {
            return results.getString(columnName);
        }
        switch (javaType.toLowerCase()) {
            case "string":
                return results.getString(columnName);
            case "double":
                return results.getDouble(columnName);
            case "int":
                return results.getInt(columnName);
            case "float":
                return results.getFloat(columnName);
            case "bigint":
                return results.getLong(columnName);
            case "decimal":
                return results.getBigDecimal(columnName);
            case "date":
            case "localdate":
                return results.getDate(columnName);
            case "time":
            case "localtime":
                return results.getTime(columnName);
            case "timestamp":
                return results.getTimestamp(columnName);
            case "blob":
                return results.getBlob(columnName);
            case "boolean":
                return results.getBoolean(columnName);
            case "byte":
                return results.getByte(columnName);
            case "bytes":
                return results.getBytes(columnName);
            default:
                return results.getString(columnName);
        }
    }

    default String convertCols(List<Map<String, Object>> srcColumnMapList,String fieldName, Set<String> topKeySet){

        List<String>  fieldList = srcColumnMapList.stream().map(map -> {
                    return ObjectUtil.toString(map.getOrDefault(fieldName,""));
                }).collect(Collectors.toList());

        return  sortAndJoin(fieldList,topKeySet);
    }

    default String sortAndJoin(List<String> fieldList, Set<String> topKeySet){
        List<String> sortColumnList = new LinkedList<>();
        if(null != topKeySet && topKeySet.size() > 0){
            topKeySet.remove("");
            fieldList.removeAll(topKeySet);
            sortColumnList.addAll(topKeySet);
        }

        sortColumnList.addAll(fieldList);

        return  sortColumnList.stream().collect(Collectors.joining(",", "", ""));
    }


}
