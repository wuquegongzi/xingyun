#!/bin/sh

# 打包
mvn clean package -DskipTests -U -X && \

# 上传
scp xy-app/target/xy-app-0.0.1-SNAPSHOT.jar root@app1:/opt/app/xy-app.jar && \

# 远程重启
ssh root@n1 "cd /opt/app; sh run.sh restart"
