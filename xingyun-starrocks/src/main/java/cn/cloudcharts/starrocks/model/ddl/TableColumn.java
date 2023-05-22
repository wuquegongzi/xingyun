package cn.cloudcharts.starrocks.model.ddl;

import lombok.Data;

/**
 * @author wuque
 * @title: TableColumn
 * @projectName xingyun
 * @description:
 * @date 2023/5/1113:42
 */
@Data
public class TableColumn {

    private String Field;

    private String Type;

    private String Comment;
}
