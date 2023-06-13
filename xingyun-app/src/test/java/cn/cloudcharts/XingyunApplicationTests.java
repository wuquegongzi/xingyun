package cn.cloudcharts;

import cn.cloudcharts.common.utils.DateUtils;
import cn.cloudcharts.metadata.enums.ColumnTypeEnums;
import cn.cloudcharts.metadata.enums.PartitionDescEnums;
import cn.cloudcharts.metadata.enums.PartitionModeEnums;
import cn.cloudcharts.metadata.model.dto.ColumnDTO;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.cloudcharts.metadata.model.partition.Partition;
import cn.cloudcharts.metadata.model.partition.PartitionFixedRangeParm;
import cn.cloudcharts.service.DdlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class XingyunApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    DdlService ddlService;

    @Test
    void testDs() throws Exception{
        System.out.println("获取的数据库连接为:"+dataSource.getConnection());
    }

    /**
     * 测试创建明细表
     */
    @Test
    void testDdlCreateDetailTable(){

        CreateTableDTO createTableDTO = new CreateTableDTO();
        createTableDTO.setDatabaseId(1);
        createTableDTO.setSchemaName("ssb");
        createTableDTO.setTblName("testDetailByJdbc"+DateUtils.dateTime());
        createTableDTO.setTblDataType("0");
        createTableDTO.setTblType("0");
        createTableDTO.setTblEngine("OLAP");
        createTableDTO.setComment("jdbc连接,向导模式创建的测试明细表");

        List<ColumnDTO> columns = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String colName = "k"+i;
            String len = "20";
            String comment = "测试";
            String notNull = "NOT NULL";

            ColumnDTO column = new ColumnDTO();
            column.setColName(colName);
            if(i == 0){
                column.setColType(ColumnTypeEnums.DATE.name());
            } else {
                column.setColType(ColumnTypeEnums.VARCHAR.name());
            }
            column.setComment(comment);
            column.setLen(len);
            column.setNotNull(notNull);
            columns.add(column);
        }

        createTableDTO.setCols(columns);
        createTableDTO.setKeyDesc("`k0`,`k1`");

        Partition partition = new Partition();
        partition.setPartitionCols("k0");
        partition.setPartitionMode(PartitionModeEnums.dominant.name());
        partition.setPartitionDesc(PartitionDescEnums.Fixed_Range.name());
        List<PartitionFixedRangeParm> partitionFixedRangeParms = new ArrayList<>();
        PartitionFixedRangeParm partitionFixedRangeParm = new PartitionFixedRangeParm();
        partitionFixedRangeParm.setPartitionName("p20220311");
        partitionFixedRangeParm.setLowerVals("'2022-03-11'");
        partitionFixedRangeParm.setUpperVals("'2022-03-12'");
        partitionFixedRangeParms.add(partitionFixedRangeParm);
        partition.setPartitionFixedRangeParms(partitionFixedRangeParms);
        createTableDTO.setPartition(partition);

        createTableDTO.setDistributedCols("`k0`,`k1`");
        createTableDTO.setBuckets("3");

        Map properties = new HashMap(1);
        properties.put("replication_num","1");
        createTableDTO.setProperties(properties);

        ddlService.createTbl(createTableDTO);

    }

    /**
     * 测试SQL建表
     */
    @Test
    void testDdlCreateTableBySql(){

        String sql = "CREATE TABLE IF NOT EXISTS `detailDemo"+ DateUtils.dateTimeNow(DateUtils.YYYYMMDDHHMMSS) +"`(\n" +
                "    `recruit_date`  DATE           NOT NULL COMMENT \"YYYY-MM-DD\",\n" +
                "    `region_num`    TINYINT        COMMENT \"range [-128, 127]\",\n" +
                "    `num_plate`     SMALLINT       COMMENT \"range [-32768, 32767] \",\n" +
                "    `tel`           INT            COMMENT \"range [-2147483648, 2147483647]\",\n" +
                "    `id`            BIGINT         COMMENT \"range [-2^63 + 1 ~ 2^63 - 1]\",\n" +
                "    `password`      LARGEINT       COMMENT \"range [-2^127 + 1 ~ 2^127 - 1]\",\n" +
                "    `name`          CHAR(\n" +
                "20\n" +
                ")       NOT NULL COMMENT \"range char(m),m in (1-255)\",\n" +
                "    `profile`       VARCHAR(\n" +
                "500\n" +
                ")   NOT NULL COMMENT \"upper limit value 1048576 bytes\",\n" +
                "    `hobby`         STRING         NOT NULL COMMENT \"upper limit value 65533 bytes\",\n" +
                "    `leave_time`    DATETIME       COMMENT \"YYYY-MM-DD HH:MM:SS\",\n" +
                "    `channel`       FLOAT          COMMENT \"4 bytes\",\n" +
                "    `income`        DOUBLE         COMMENT \"8 bytes\",\n" +
                "    `account`       DECIMAL(\n" +
                "12\n" +
                ",\n" +
                "4\n" +
                ")  COMMENT \"\",\n" +
                "    `ispass`        BOOLEAN        COMMENT \"true/false\"\n" +
                ") ENGINE=OLAP\n" +
                "DUPLICATE KEY(`recruit_date`, `region_num`)\n" +
                "PARTITION BY RANGE(`recruit_date`)\n" +
                "(\n" +
                "    PARTITION p20220311 VALUES [('2022-03-11'), ('2022-03-12')),\n" +
                "    PARTITION p20220312 VALUES [('2022-03-12'), ('2022-03-13')),\n" +
                "    PARTITION p20220313 VALUES [('2022-03-13'), ('2022-03-14')),\n" +
                "    PARTITION p20220314 VALUES [('2022-03-14'), ('2022-03-15')),\n" +
                "    PARTITION p20220315 VALUES [('2022-03-15'), ('2022-03-16'))\n" +
                ")\n" +
                "DISTRIBUTED BY HASH(`recruit_date`, `region_num`) BUCKETS \n" +
                "8\n" +
                "\n" +
                "PROPERTIES (\n" +
                "    \"replication_num\" = \"1\" \n" +
                ")";

//        System.out.println( ddlMapper.createTableBySql(sql));
    }


    @Test
    void contextLoads(){

    }

}
