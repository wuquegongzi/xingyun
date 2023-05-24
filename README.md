# xingyun

**星云-轻量化实时数仓**



----

API 访问地址：

[springdoc api](http://localhost:8088/swagger-ui/index.html): [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html)

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
  "url": "jdbc:mysql://192.168.217.232:9030/delta?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true",
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
  "statement": "show tables;",
  "databaseId": 1,
  "maxRowNum": 100
}'





