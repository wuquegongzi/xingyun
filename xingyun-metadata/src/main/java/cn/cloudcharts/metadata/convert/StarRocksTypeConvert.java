package cn.cloudcharts.metadata.convert;

import cn.cloudcharts.common.utils.StringUtils;
import cn.cloudcharts.metadata.task.schema.StarrocksCloumn;
import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wuque
 * @title: StarRocksTypeConvert
 * @projectName xingyun
 * @description:
 * @date 2023/5/2414:21
 */
public class StarRocksTypeConvert implements ITypeConvert {

    @Override
    public String convertCols(List<Map<String, Object>> srcColumnMapList,String fieldName,Set<String> topKeySet) {

        List<StarrocksCloumn> srcColumnList = srcColumnMapList.stream().map(m ->{
            StarrocksCloumn starrocksCloumn = BeanUtil.fillBeanWithMap(m,new StarrocksCloumn(),false);
            return starrocksCloumn;
        }).collect(Collectors.toList());

        List<String>  fieldList = srcColumnList.stream().map(starrocksCloumn -> {
                    if("UNKNOWN_TYPE".equals(starrocksCloumn.getType().toUpperCase())){
                        return "";
                    }
                    return starrocksCloumn.getField();
                }).filter(s -> {return StringUtils.isNotEmpty(s);})
                .collect(Collectors.toList());

       return  sortAndJoin(fieldList,topKeySet);
    }

}
