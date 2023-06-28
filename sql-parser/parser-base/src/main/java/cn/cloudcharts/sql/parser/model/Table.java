package cn.cloudcharts.sql.parser.model;

import lombok.Data;

/**
 * @author wuque
 * @title: Table
 * @projectName xingyun
 * @description:
 * @date 2023/6/2710:22
 */
@Data
public class Table {

    private String catalog;
    private String db;
    private String tblName;

}
