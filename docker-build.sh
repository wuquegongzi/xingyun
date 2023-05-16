#!/bin/sh
#mvn clean package -DskipTests
#docker build -t xingyun-m1:latest .
docker build --platform linux/amd64 -t xingyun:latest .
#docker login 192.168.216.190:80
# admin  Harbor12345
docker tag xingyun:latest 192.168.216.190:80/xingyun-dev/xingyun:v1.0
docker push 192.168.216.190:80/xingyun-dev/xingyun:v1.0
docker run -p 18088:8088 -d 192.168.216.190:80/xingyun-dev/xingyun:v1.0
docker ps
