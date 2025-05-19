FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/asistantProject-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} assistant.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENV DBNAME=jdbc:mysql://localhost:3307/project?zeroDateTimeBehavior=CONVERT_TO_NULL
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "assistant.jar"]
