#FROM maven:latest as builder

#RUN apt update
#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:21-jdk-oracle
MAINTAINER jeizas
ARG JAR_FILE=target/*exec.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENV botToken="default_topken"
ENV botName="default_name"
ENV botChatId="123"
ENV JAVA_OPTS="-server -Xmx512m -Xms512m"
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${botToken} ${botName} ${botChatId}"]

