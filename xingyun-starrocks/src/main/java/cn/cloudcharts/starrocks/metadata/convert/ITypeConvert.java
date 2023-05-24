
package cn.cloudcharts.starrocks.metadata.convert;

import cn.cloudcharts.common.utils.AssertUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}
