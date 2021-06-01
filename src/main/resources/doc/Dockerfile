FROM fabric8/java-alpine-openjdk8-jdk
RUN echo "Asia/Shanghai" > /etc/timezone
VOLUME /tmp
ADD search-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9876
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","/app.jar"]