###指定java8环境镜像
FROM java:8
###复制文件到容器
ADD search-0.0.1-SNAPSHOT.jar /search.jar
###声明启动端口号
EXPOSE 9090
###配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","/search.jar"]
