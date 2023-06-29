package cn.cloudcharts.common.utils.sql;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.common.utils.StringUtils;
import cn.cloudcharts.common.exception.UtilException;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * sql操作工具类
 *
 * @author wuque
 */
public class SqlUtil
{
    /**
     * 定义常用的 sql关键字
     */
    public static String SQL_REGEX = "select |insert |delete |update |drop |count |exec |chr |mid |master |truncate |char |and |declare ";

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new UtilException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL关键字检查
     */
    public static void filterKeyword(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords)
        {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1)
            {
                throw new UtilException("参数存在SQL注入风险");
            }
        }
    }


    private static final String SEMICOLON = ";";

    public static String[] getStatements(String sql) {
        return getStatements(sql, SEMICOLON);
    }

    public static String[] getStatements(String sql, String sqlSeparator) {
        if (AssertUtil.isNullString(sql)) {
            return new String[0];
        }

        String[] splits = sql.replace(";\r\n", ";\n").split(sqlSeparator);
        String lastStatement = splits[splits.length - 1].trim();
        if (lastStatement.endsWith(SEMICOLON)) {
            splits[splits.length - 1] = lastStatement.substring(0, lastStatement.length() - 1);
        }

        return splits;
    }

    public static String removeNote(String sql) {

        if (AssertUtil.isNotNullString(sql)) {
            // Remove the special-space characters
            sql = sql.replaceAll("\u00A0", " ")
                    .replaceAll("[\r\n]+", "\n")
                    .replaceAll("`","");
            // Remove annotations Support '--aa' , '/**aaa*/' , '//aa' , '#aaa'
            Pattern p = Pattern.compile("(?ms)('(?:''|[^'])*')|--.*?$|//.*?$|/\\*.*?\\*/|#.*?$|");
            String presult = p.matcher(sql).replaceAll("$1");
            return presult.trim();
        }
        return sql;
    }

    public static String replaceAllParam(String sql, String name, String value) {
        return sql.replaceAll("\\$\\{" + name + "\\}", value);
    }

    /**
     * replace sql context with values params, map's key is origin variable express by `${key}`,
     * value is replacement. for example, if key="name", value="replacement", and sql is "${name}",
     * the result will be "replacement".
     *
     * @param sql sql context
     * @param values replacement
     * @return replace variable result
     */
    public static String replaceAllParam(String sql, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            sql = replaceAllParam(sql, entry.getKey(), entry.getValue());
        }
        return sql;
    }


}
