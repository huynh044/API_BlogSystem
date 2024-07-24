# Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/APIMobileStore-0.0.1-SNAPSHOT.jar APIMobileStore-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "APIMobileStore-0.0.1-SNAPSHOT.jar"]
