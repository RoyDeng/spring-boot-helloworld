FROM gradle:7.6.1 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-slim

ENV PORT 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "spring-boot-application.jar"]