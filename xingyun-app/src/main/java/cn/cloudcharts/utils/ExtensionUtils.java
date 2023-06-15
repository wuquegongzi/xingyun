package cn.cloudcharts.utils;

import cn.cloudcharts.common.constant.BizConstants;
import cn.cloudcharts.common.constant.ComparatorEnums;
import cn.cloudcharts.common.enums.ComparTypeEnums;
import cn.cloudcharts.common.enums.DataSourceTypeEnum;
import cn.cloudcharts.common.enums.OffsetDateUnitTypeEnums;
import cn.cloudcharts.common.utils.GsonUtils;
import cn.cloudcharts.common.utils.StringUtils;
import cn.cloudcharts.model.request.condition.ConditionGroup;
import cn.cloudcharts.model.request.condition.ConditionNode;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * @author wuque
 * @title: ExtensionUtils
 * @projectName xingyun
 * @description:
 * @date 2023/6/15
 */
@Slf4j
public class ExtensionUtils {

    /**
     * 根据偏移量和偏移类型 生成 变量表达式
     * @param fieldValueOffsetType  @cn.cloudcharts.common.enums.OffsetDateUnitTypeEnums
     * @param offsetValue
     * @return
     */
    public static String getVariableExpression(String fieldValueOffsetType, String offsetValue) {

        OffsetDateUnitTypeEnums offsetDateUnitTypeEnum = OffsetDateUnitTypeEnums.valueOf(fieldValueOffsetType);
        if(null == offsetDateUnitTypeEnum){
            log.error("不支持该偏移量单位！");
            return "";
        }

        int offset = 0;
        try {
            offset = Integer.valueOf(offsetValue);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return offsetValue;
        }
        String var = offsetValue;

        String calculation;
        if(offset > 0){
            calculation  = "+";
        }else{
            calculation  = "-";
        }

        //年
        if(OffsetDateUnitTypeEnums.y.name().equals(offsetDateUnitTypeEnum.name())){
            var = calculation + "12*"+Math.abs(offset);
        }
        //月
        else if(OffsetDateUnitTypeEnums.M.name().equals(offsetDateUnitTypeEnum.name())){
            var = calculation+"30*"+Math.abs(offset);
        }
        //周
        else if(OffsetDateUnitTypeEnums.w.name().equals(offsetDateUnitTypeEnum.name())){
            var = calculation+"7*"+Math.abs(offset);
        }
        //天
        else if(OffsetDateUnitTypeEnums.d.name().equals(offsetDateUnitTypeEnum.name())){
            var = calculation+Math.abs(offset);
        }
        //小时
        else if(OffsetDateUnitTypeEnums.H.name().equals(offsetDateUnitTypeEnum.name())){
            var = calculation+Math.abs(offset) + "/24";
        }
        //分钟
        else if(OffsetDateUnitTypeEnums.m.name().equals(offsetDateUnitTypeEnum.name())){
            var = calculation+Math.abs(offset) + "/24/60";
        }

        return String.format(offsetDateUnitTypeEnum.getExpression(), var);
    }


    /**
     * 根据偏移量和偏移类型 获取具体 时间
     * @param fieldValueOffsetType   @cn.cloudcharts.common.enums.OffsetDateUnitTypeEnums
     * @param offsetValue
     * @return
     */
    public static String getVariableDateStr(String fieldValueOffsetType, String offsetValue) {

        OffsetDateUnitTypeEnums offsetDateUnitTypeEnum = OffsetDateUnitTypeEnums.valueOf(fieldValueOffsetType);
        Date date = new Date();
        DateTime offsetDate=null;
        if(null == offsetDateUnitTypeEnum){
            log.error("不支持该偏移量单位！");
            return "";
        }

        int offset = 0;
        try {
            offset = Integer.valueOf(offsetValue);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return offsetValue;
        }

        //年
        if(OffsetDateUnitTypeEnums.y.name().equals(offsetDateUnitTypeEnum.name())){
            offsetDate = DateUtil.offset(date, DateField.YEAR, Integer.valueOf(offset));
        }
        //月
        if(OffsetDateUnitTypeEnums.M.name().equals(offsetDateUnitTypeEnum.name())){
            offsetDate = DateUtil.offset(date, DateField.MONTH, Integer.valueOf(offset));
        }
        //周
        else if(OffsetDateUnitTypeEnums.w.name().equals(offsetDateUnitTypeEnum.name())){
            offsetDate = DateUtil.offset(date, DateField.WEEK_OF_YEAR, Integer.valueOf(offset));
        }
        //天
        else if(OffsetDateUnitTypeEnums.d.name().equals(offsetDateUnitTypeEnum.name())){
            offsetDate = DateUtil.offset(date, DateField.DAY_OF_YEAR, Integer.valueOf(offset));
        }
        //小时
        else if(OffsetDateUnitTypeEnums.H.name().equals(offsetDateUnitTypeEnum.name())){
            offsetDate = DateUtil.offset(date, DateField.HOUR, Integer.valueOf(offset));
        }
        //分钟
        else if(OffsetDateUnitTypeEnums.m.name().equals(offsetDateUnitTypeEnum.name())){
            offsetDate = DateUtil.offset(date, DateField.MINUTE, Integer.valueOf(offset));
        }
        return offsetDate.toString("yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 过滤SQL 提取
     * @param conditionType  条件类型 0-可视化配置 1-SQL
     * @param condition  条件配置
     * @param sourceType  数据源类型 默认 0-starrocks
     * @return
     */
    public static String getFilterSql(String conditionType, String condition, String sourceType) {

        if(StrUtil.isEmpty(condition)){
            return "";
        }

        if(BizConstants.FILTER_SQL.equals(conditionType)){
            return condition;
        }

        //配置转换SQL
        List<ConditionGroup> formulaFilterGroups = GsonUtils.jsonToList(condition, ConditionGroup.class);
        StringBuffer sb = new StringBuffer();

        ConditionGroup preFormulaFilterGroup = null;
        sourceType = StringUtils.isEmpty(sourceType)?DataSourceTypeEnum.STARROCKS.code():sourceType;
        for (ConditionGroup formulaFilterGroup : formulaFilterGroups){

            List<ConditionNode> formulaFilterNodes = formulaFilterGroup.getNodes();
            StringBuffer nodeSb = new StringBuffer();

            ConditionNode  preFormulaFilterNode = null;
            for ( ConditionNode  formulaFilterNode : formulaFilterNodes ) {
                if(StrUtil.isEmpty(formulaFilterNode.getComparator())
                        || StrUtil.isEmpty(formulaFilterNode.getComparType())
                        || StrUtil.isEmpty(formulaFilterNode.getFieldLabel())
                        || StrUtil.isEmpty(formulaFilterNode.getComparValue())
                        //分区字段无需转化过滤SQL属性
                        || BizConstants.FIELD_TYPE_PART.equals(formulaFilterNode.getPartType())
                ){
                    continue;
                }

                if (ComparatorEnums.toMap().containsKey(formulaFilterNode.getComparator())){
                    if(nodeSb.length() > 0 && ObjectUtil.isNotNull(preFormulaFilterNode)){
                        nodeSb.append(" ").append(preFormulaFilterNode.getNextRelation()).append(" ");
                    }

                    if(ComparatorEnums.LIKE.getType().equals(formulaFilterNode.getComparator())){
                        nodeSb.append(formulaFilterNode.getFieldLabel()).append(" ")
                                .append(formulaFilterNode.getComparator()).append(" ")
                                .append("'%")
                                .append(StringUtils.stringStartEndTrim(getCompareValue(formulaFilterNode,sourceType),"'"))
                                .append("%'")
                        ;
                    }else if(ComparatorEnums.LEFT_LIKE.getType().equals(formulaFilterNode.getComparator())){
                        nodeSb.append(formulaFilterNode.getFieldLabel()).append(" ")
                                .append("like").append(" ")
                                .append("'%")
                                .append(StringUtils.stringStartEndTrim(getCompareValue(formulaFilterNode,sourceType),"'"))
                                .append("'")
                        ;
                    }else if(ComparatorEnums.RIGHT_LIKE.getType().equals(formulaFilterNode.getComparator())){
                        nodeSb.append(formulaFilterNode.getFieldLabel()).append(" ")
                                .append("like").append(" '")
                                .append(StringUtils.stringStartEndTrim(getCompareValue(formulaFilterNode,sourceType),"'"))
                                .append("%'")
                        ;
                    }else{
                        nodeSb.append(fieldLabelHandle(formulaFilterNode,sourceType)).append(" ")
                                .append(formulaFilterNode.getComparator()).append(" ")
                                .append(getCompareValue(formulaFilterNode,sourceType));
                    }

                }

                preFormulaFilterNode = formulaFilterNode;
            }

            if(StrUtil.isNotEmpty(nodeSb.toString())){
                if(sb.length() > 0 && ObjectUtil.isNotNull(preFormulaFilterGroup)){
                    sb.append(" ").append(preFormulaFilterGroup.getNextRelation()).append(" ");
                }
                sb.append(" ( ").append(nodeSb).append(" ) ");
            }

            preFormulaFilterGroup = formulaFilterGroup;
        }

        System.out.println("过滤SQL生成："+sb);
        return sb.toString();
    }


    public static String getCompareValue(ConditionNode formulaFilterNode, String datasourceType) {

        if(ComparTypeEnums.STRING.getType().equals(formulaFilterNode.getComparType())){
            return "'"+formulaFilterNode.getComparValue()+"'";
        }else if(ComparTypeEnums.DATETIME.getType().equals(formulaFilterNode.getComparType())){
            return dateTimeHandle(formulaFilterNode.getComparValue(),datasourceType);
        }else if(ComparTypeEnums.DATE.getType().equals(formulaFilterNode.getComparType())){
            return "'"+ DateUtil.format(DateUtil.parseDate(formulaFilterNode.getComparValue()),"yyyy-MM-dd")+"'";
        }else if(ComparTypeEnums.NUMBER.getType().equals(formulaFilterNode.getComparType())){
            return formulaFilterNode.getComparValue();
        }else if(ComparTypeEnums.EXPRESSION.getType().equals(formulaFilterNode.getComparType())){
            //动态表达式过滤 TODO
            return formulaFilterNode.getComparValue();
        }else if(ComparTypeEnums.VARIABLE.getType().equals(formulaFilterNode.getComparType())){
            //变量 https://dolphinscheduler.apache.org/zh-cn/docs/latest/user_doc/guide/parameter/built-in.html
            String variableDateStr = getVariableDateStr(formulaFilterNode.getFieldValueOffsetType(), formulaFilterNode.getComparValue());
            return dateTimeHandle(variableDateStr,datasourceType);

        }else{
            log.error("暂不支持该比较类型{}",formulaFilterNode.getComparType());
            return "";
        }
    }


    public static String fieldLabelHandle(ConditionNode formulaFilterNode,String datasourceType){
        String fieldLabel=formulaFilterNode.getFieldLabel();
        switch (DataSourceTypeEnum.valueOfV2(datasourceType)){
            case HIVE:
                if(ComparTypeEnums.DATETIME.getType().equals(formulaFilterNode.getComparType())){
                    fieldLabel=String.format("date_format(%s,'yyyy-MM-dd hh:MM:ss')",fieldLabel);
                }
                break;
            case SQLSERVER:
                if(ComparTypeEnums.DATETIME.getType().equals(formulaFilterNode.getComparType())){
                    fieldLabel=String.format("DATEDIFF(ss, '%s', %s)",DateUtil.format(DateUtil.parse(formulaFilterNode.getComparValue())
                            ,"yyyy-MM-dd HH:mm:ss")
                            ,fieldLabel);
                }else if(ComparTypeEnums.VARIABLE.getType().equals(formulaFilterNode.getComparType())){
                    String variableDateStr = getVariableDateStr(formulaFilterNode.getFieldValueOffsetType(), formulaFilterNode.getComparValue());
                    fieldLabel=String.format("DATEDIFF(ss, '%s', %s)",variableDateStr,fieldLabel);
                }
                break;
            case HANA:
                if(ComparTypeEnums.DATETIME.getType().equals(formulaFilterNode.getComparType())){
                    fieldLabel=String.format("SECONDS_BETWEEN (%s, '%s')",DateUtil.format(DateUtil.parse(formulaFilterNode.getComparValue())
                            ,"yyyy-MM-dd HH:mm:ss")
                            ,fieldLabel);
                }else if(ComparTypeEnums.VARIABLE.getType().equals(formulaFilterNode.getComparType())){
                    String variableDateStr = getVariableDateStr(formulaFilterNode.getFieldValueOffsetType(), formulaFilterNode.getComparValue());
                    fieldLabel=String.format("SECONDS_BETWEEN (%s, '%s')",fieldLabel,variableDateStr);
                }
                break;
            default:
                break;
        }
        return fieldLabel;
    }


    public static String dateTimeHandle(String value, String datasourceType){
        String dateStr="";
        switch (DataSourceTypeEnum.valueOfV2(datasourceType)){
            case ORACLE:
                dateStr=String.format("to_date('%s','yyyy-MM-dd hh24:mi:ss')",value);
                break;
            case MYSQL:
            case STARROCKS:
                dateStr="str_to_date('"+value+"','%Y-%m-%d %T')";
                break;
            case HIVE:
                dateStr=String.format("date_format('%s','yyyy-MM-dd hh:MM:ss')",value);
                break;
            case SQLSERVER:
                dateStr="0";
                break;
            default:
                dateStr="'"+DateUtil.format(DateUtil.parse(value),"yyyy-MM-dd HH:mm:ss")+"'";
                break;
        }
        return dateStr;
    }


}
