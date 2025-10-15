FROM ubuntu:latest
LABEL authors="saurinkumarghosh"

ENTRYPOINT ["top", "-b"]

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/order-service-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
