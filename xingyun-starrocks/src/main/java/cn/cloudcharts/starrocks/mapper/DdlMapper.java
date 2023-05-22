package cn.cloudcharts.starrocks.mapper;

import cn.cloudcharts.starrocks.model.Column;
import cn.cloudcharts.starrocks.model.CreateTableDTO;
import cn.cloudcharts.starrocks.model.CreateTableLikeDTO;
import cn.cloudcharts.starrocks.model.ddl.TableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: DdlMapper
 * @projectName xingyun
 * @description: DDL相关
 * @date 2023/5/614:22
 */
@Mapper
public interface DdlMapper {


//    int alterDatabase();

    List<Map> showResources();

    /**
     * 创建明细模型
     * @param dto
     * @return
     */
    int createDetailTbl(@Param("dto") CreateTableDTO dto);

    /**
     * 创建聚合模型
     * @param dto
     * @return
     */
    int createAggregateTbl(@Param("dto") CreateTableDTO dto);

    /**
     * 创建更新模型
     * @param dto
     * @return
     */
    int createUniqueTbl(@Param("dto") CreateTableDTO dto);

    /**
     * 创建主键模型
     * @param dto
     * @return
     */
    int createPrimaryTbl(@Param("dto") CreateTableDTO dto);

    /**
     * 创建一张外表
     * @param dto
     * @return
     */
    int createExternalTbl(@Param("dto") CreateTableDTO dto);

    /**
     * 创建一个表结构和另一张表完全相同的空表
     * @param dto
     * @return
     */
    int createTableLike(@Param("dto") CreateTableLikeDTO dto);

    int createTableBySql(@Param("sql") String sql);


    List<TableColumn> queryAllColumns(@Param("table") String table);

    String addColumns(@Param("table") String table, @Param("columns")  List<Column> columns);



}
