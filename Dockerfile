FROM openjdk:17
LABEL authors="anshan"

# 设置工作目录
WORKDIR /app
# 将本地jar文件复制到容器中的/app目录下
COPY xy-app/target/*.jar /app/xingyun.jar

# 暴露8088端口
EXPOSE 8088

#ENTRYPOINT ["top", "-b"]
CMD ["java", "-jar", "/app/xingyun.jar"]


