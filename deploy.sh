#!/bin/sh

# 打包
mvn clean package -DskipTests -U -X && \

# 上传
scp xingyun-app/target/xingyun-app-0.0.1-SNAPSHOT.jar root@k7:/data/app/xingyun/xingyun-app.jar && \

# 远程重启
ssh root@k7 "cd /data/app/xingyun; bash run.sh restart"
