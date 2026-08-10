#FROM ubuntu:latest
#LABEL authors="S.Gopi"
#
#ENTRYPOINT ["top", "-b"]
#
#
#
#

FROM eclipse-temurin:21-alpine

WORKDIR /app

EXPOSE 8080

COPY target/hospital-management-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

