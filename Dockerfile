FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/asistantProject-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} assistant.jar
EXPOSE 3020
ENTRYPOINT ["java", "-jar", "assistant.jar"]
