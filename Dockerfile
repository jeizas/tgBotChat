FROM ubuntu:20.04
RUN apt update && apt install -y --no-install-recommends procps openjdk-21-jdk curl maven
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

MAINTAINER demoApp
ARG JAR_FILE=target/*exec.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENV botToken="default_topken"
ENV botName="default_name"
ENV JAVA_OPTS="-server -Xmx512m -Xms512m"
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${botToken} ${botName}"]