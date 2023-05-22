package cn.cloudcharts;

import cn.cloudcharts.common.utils.DateUtils;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;


@SpringBootTest
class XingyunApplicationTests {

    @Autowired
    DataSource dataSource;


    @Test
    void testDs() throws Exception{
        System.out.println("获取的数据库连接为:"+dataSource.getConnection());
    }

//    @Test
//    void testDdlMapper(){
//        List<Map> maps = ddlMapper.showResources();
//        System.out.println(maps.toArray().toString());
//    }
//
    /**
     * 测试创建明细表
     */
//    @Test
//    void testDdlCreateDetailTable(){
//
//        CreateTableDTO createTableDTO = new CreateTableDTO();
//        createTableDTO.setTblName("testDetailByJdbc");
//        createTableDTO.setTblDataType("0");
//        createTableDTO.setTblType("0");
//        createTableDTO.setTblEngine("OLAP");
//        createTableDTO.setComment("jdbc连接,向导模式创建的测试明细表");
//
//        List<Column> columns = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            String colName = "k"+i;
//            String len = "20";
//            String comment = "测试";
//            String notNull = "NOT NULL";
//
//            Column column = new Column();
//            column.setColName(colName);
//            if(i == 0){
//                column.setColType(ColumnTypeEnums.DATE);
//            } else {
//                column.setColType(ColumnTypeEnums.VARCHAR);
//            }
//            column.setComment(comment);
//            column.setLen(len);
//            column.setNotNull(notNull);
//            columns.add(column);
//        }
//
//        createTableDTO.setCols(columns);
//        createTableDTO.setKeyDesc("`k0`,`k1`");
//
//        Partition partition = new Partition();
//        partition.setPartitionCols("k0");
//        partition.setPartitionMode(PartitionModeEnums.dominant);
//        partition.setPartitionDesc(PartitionDescEnums.Fixed_Range);
//        List<PartitionFixedRangeParm> partitionFixedRangeParms = new ArrayList<>();
//        PartitionFixedRangeParm partitionFixedRangeParm = new PartitionFixedRangeParm();
//        partitionFixedRangeParm.setPartitionName("p20220311");
//        partitionFixedRangeParm.setLowerVals("'2022-03-11'");
//        partitionFixedRangeParm.setUpperVals("'2022-03-12'");
//        partitionFixedRangeParms.add(partitionFixedRangeParm);
//        partition.setPartitionFixedRangeParms(partitionFixedRangeParms);
//        createTableDTO.setPartition(partition);
//
//        createTableDTO.setDistributedCols("`k0`,`k1`");
//        createTableDTO.setBuckets("3");
//
//        Map properties = new HashMap(1);
//        properties.put("replication_num","1");
//        createTableDTO.setProperties(properties);
//
//        ddlMapper.createDetailTbl(createTableDTO);
//
//    }

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

        // 定义连接集群的 Token
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ii1rMzBsaXF1TXVPeTFFVmJGNGZPY1EwTmlVeDFLTVZ0UFlwYUZWWFFVaDgifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJkZWZhdWx0LXRva2VuLWJueHg3Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImRlZmF1bHQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJhMzhkNzE3ZS1mMDQ5LTRhYTQtOTczOS05OWI4MWUyNTI1MjQiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06ZGVmYXVsdCJ9.vo7IPlMIgfeFJ7QFlxqDOrqhgGDl3n9KVo1ToMGWmzr5i3Rw5oP5aqc0NfSFp1bC6VQfc0JIQflyd5kjfcID9UdOUdX3O__Br89rznDTERn-33S5oJ1RPEGSkhNN0XamHSwOfAh5Ou0t2U_vT3qe8udPtFjinwNIJHZsCHP9PuiVynpRFX8irb-GtWzjSDI54BkpYCx9ZMExmibTTq2je16YnUMbhn6_DHbkvfEQwYzO4q6eUvzZaA7E7u4G1ufDdI1kBxvtUXSNM-FY-QjuL9S8vG3nKQ8ENvm5TVutB5gInvCRl3A9NUl8zYsqaePCbBCi6Y_m9E8bSOownBcVRw";
        // 定义 Kubernetes 集群地址
        String url = "https://192.168.216.190:6443";

        // 配置客户端
//        ApiClient apiClient = Config.fromToken(url, token, false);

        ApiClient client =
                new ClientBuilder().setBasePath(url)
                        .setVerifyingSsl(false)
                        .setAuthentication(new AccessTokenAuthentication(token))
                        .build();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList list = null;
        try {
            list = api.listPodForAllNamespaces(
                    true, "", "", "",
                    10, "", "", "",
                    5, true);

            for (V1Pod item : list.getItems()) {
                System.out.println(item.getMetadata().getName());
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

}
