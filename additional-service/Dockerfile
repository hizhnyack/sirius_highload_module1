FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon && ls -la /app/build/libs/

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"] 