#!/bin/sh
# mvn clean package -DskipTests
docker build -t xingyun:latest .
docker tag xingyun:latest xingyun:v1.0
# docker login dockerhub.com
# docker push anshan/xingyun:v1.0
docker run -p 18088:8088 xingyun:v1.0
docker ps
