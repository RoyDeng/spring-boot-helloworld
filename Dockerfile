FROM gradle:8.0.2 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM openjdk:17-jdk-slim-buster

ENV PORT 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "spring-boot-application.jar"]