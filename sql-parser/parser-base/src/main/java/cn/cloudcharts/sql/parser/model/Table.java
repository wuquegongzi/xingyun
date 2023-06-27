package cn.cloudcharts.sql.parser.model;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
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

    public String catalog;
    public String db;
    public String tblName;

}
