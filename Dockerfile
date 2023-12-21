FROM openjdk:11-jre-slim
WORKDIR /app
EXPOSE 8080
COPY target/keda-teste-3.0.1.jar /app.jar
CMD ["java", "-jar", "/app.jar"]