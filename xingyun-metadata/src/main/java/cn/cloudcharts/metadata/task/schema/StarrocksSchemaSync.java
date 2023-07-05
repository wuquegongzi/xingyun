package cn.cloudcharts.metadata.task.schema;

import cn.cloudcharts.metadata.enums.TblDataModelEnums;
import cn.cloudcharts.metadata.model.dto.ColumnDTO;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.hutool.core.bean.BeanUtil;

import java.util.*;

/**
 * @author wuque
 * @title: StarrocksSchemaSync
 * @projectName xingyun
 * @description:
 * @date 2023/6/2714:38
 */
public class StarrocksSchemaSync implements SchemaSync<CreateTableDTO> {

    @Override
    public CreateTableDTO transformTbl(String schemaName,String tblName, List<Map<String,Object>> srcList, String srcType, String sinkType) {

        CreateTableDTO ctDto = new CreateTableDTO();
        ctDto.setSchemaName(schemaName);
        ctDto.setTblName(tblName);

        List<ColumnDTO> cols = new LinkedList<>();
        Set<String> keyDescSet = new LinkedHashSet<>();
        for (Map map : srcList) {
            StarrocksCloumn starrocksCloumn = BeanUtil.fillBeanWithMap(map,new StarrocksCloumn(),false);
            ColumnDTO columnDTO = new ColumnDTO();
            columnDTO.setColName(starrocksCloumn.getField());
            String type = starrocksCloumn.getType();
            if("UNKNOWN_TYPE".equals(type.toUpperCase())){
                starrocksCloumn.setType("STRING");
            }
            columnDTO.setColType(starrocksCloumn.getType());
//            columnDTO.setLen();
            columnDTO.setComment(starrocksCloumn.getComment());
            columnDTO.setDefaultVal(starrocksCloumn.getDefault());
            if(!"YES".equals(starrocksCloumn.getNull())){
                columnDTO.setNotNull("NOT NULL");
            }
            if("YES".equals(starrocksCloumn.getKey())){
                keyDescSet.add(starrocksCloumn.getField());
            }
            if("id".equals(starrocksCloumn.getField())){
                keyDescSet.add(starrocksCloumn.getField());
            }
            cols.add(columnDTO);
        }
        ctDto.setCols(cols);

        //  "表数据模型  0-明细 1-聚合 2-更新 3-主键"
        ctDto.setTblDataType(keyDescSet.size() > 0 ? TblDataModelEnums.PRIMARY.getCode() : TblDataModelEnums.DUPLICATE.getCode());
        StringJoiner sj = new StringJoiner(",");
        for(String s : keyDescSet){
            sj.add(s);
        }
        ctDto.setKeyDesc(sj.toString());

        return ctDto;
    }
}
