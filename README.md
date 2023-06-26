# xingyun

**星云-轻量化小数仓**

----
检查启动状态：
http://localhost:8088/actuator/health
监控：
http://localhost:8088/monitor

----

API 访问地址：

- [swagger api](http://localhost:8088/swagger-ui/index.html): [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html)
- [knife4j api](http://localhost:8088/doc.html): [http://localhost:8088/doc.html](http://localhost:8088/doc.html)

----

基础环境：

1. jdk17
2. mysql 5.7+
3. StarRocks 3.0
4. kubesphere + k8s + harbor
5. flink17
6. (可选)Hadoop3

----
详细说明：

- 启动类： xingyun-app/cn.cloudcharts.XingyunApplication
- 应用代码生成工具：xingyun-app/cn.cloudcharts.generator.FastAutoGeneratorTest
- MySQL DB初始化文件： xingyun-docs/sql

----
数据源管理：

- 数据源增加：

  ```` curl -X 'POST' \
  'http://localhost:8088/database/add' \
  -H 'accept:*/*' \
  -H 'Content-Type: application/json' \
  -d '{
  "tenantId": 0,
  "name": "dev-starrocks",
  "groupName": "dev",
  "type": "StarRocks",
  "ip": "192.168.217.232",
  "port": 9030,
  "url": "jdbc:mysql://192.168.217.232:9030?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true",
  "username": "root",
  "password": "",
  "note": "string",
  "flinkConfig": "string",
  "flinkTemplate": "string",
  "dbVersion": "3.0",
  "status": true,
  "healthTime": "2023-05-24T05:44:11.673Z",
  "heartbeatTime": "2023-05-24T05:44:11.673Z",
  "enabled": true,
  "createTime": "2023-05-24T05:44:11.673Z",
  "updateTime": "2023-05-24T05:44:11.673Z",
  "code": "1234565"
  }' 
  
- 执行StarRocks SQL
 ````curl -X 'POST' \
  'http://localhost:8088/executeSql' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "statement": "show catalogs;",
  "databaseId": 1,
  "maxRowNum": 100
}'
````

- 备注：statement为SQL语句，多个;分割，目前仅支持透出最后一个查询SQL结果传回。原则上IRS只允许传递查询语句。

# 1、查看catalogs ： show catalogs;
     默认内部OLAP：default_catalog
     开发测试环境的Delta:  delta_test
     预发环境的Delta：  delta_test2

# 2、查询开发测试环境的Delta：
 - 查询 Delta Lake 集群中的数据库：
   SHOW DATABASES FROM delta_test
- 查询 Delta Lake 集群中的STG数据库的表： 
   SHOW tables FROM delta_test.stg
- 查询某张表的数据
   select * from delta_test.stg.xxx


教程：https://docs.starrocks.io/zh-cn/latest/data_source/catalog/deltalake_catalog



### antlr4 
mvn generate-sources




