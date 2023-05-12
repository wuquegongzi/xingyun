
# openapi地址：
http://localhost:8088/v3/api-docs

# openapi-generator 官方文档
https://github.com/OpenAPITools/openapi-generator

# 显示用法
java -jar openapi-generator-cli-6.6.0.jar help

# 构建客户端代碼生成命令：
cd docs

java -jar openapi-generator-cli-6.6.0.jar generate \
-i http://localhost:8088/v3/api-docs \
-g java \
--api-package cn.cloudcharts.hs.olap.client.api \
-t ../sdk/xingyun-hs-v1-sdk/src/main/java \
--additional-properties artifactId=xingyun-hs-v1-sdk,hideGenerationTimestamp:true \
-o ../sdk/xingyun-hs-v1-sdk

## 在线 OpenAPI 生成器
### 以下是公共在线服务：

# 最新稳定版： https: //api.openapi-generator.tech
# 最新大师： https: //api-latest-master.openapi-generator.tech（每小时更新最新大师）
