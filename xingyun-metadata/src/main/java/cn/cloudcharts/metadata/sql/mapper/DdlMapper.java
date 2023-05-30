package cn.cloudcharts.metadata.sql.mapper;

import cn.cloudcharts.metadata.model.dto.ColumnDTO;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.cloudcharts.metadata.model.dto.CreateTableLikeDTO;
import cn.cloudcharts.metadata.model.result.TableColumnResult;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: DdlMapper
 * @projectName xingyun
 * @description: DDL相关
 * @date 2023/5/614:22
 */
//@Mapper
@Deprecated
public interface DdlMapper {


//    int alterDatabase();

    List<Map> showResources();

    /**
     * 创建明细模型
     * @param dto
     * @return
     */
    int createDetailTbl(CreateTableDTO dto);

    /**
     * 创建聚合模型
     * @param dto
     * @return
     */
    int createAggregateTbl(CreateTableDTO dto);

    /**
     * 创建更新模型
     * @param dto
     * @return
     */
    int createUniqueTbl(CreateTableDTO dto);

    /**
     * 创建主键模型
     * @param dto
     * @return
     */
    int createPrimaryTbl(CreateTableDTO dto);

    /**
     * 创建一张外表
     * @param dto
     * @return
     */
    int createExternalTbl(CreateTableDTO dto);

    /**
     * 创建一个表结构和另一张表完全相同的空表
     * @param dto
     * @return
     */
    int createTableLike(CreateTableLikeDTO dto);

    int createTableBySql(String sql);


    List<TableColumnResult> queryAllColumns(String table);

    String addColumns(String table, List<ColumnDTO> columns);



}
