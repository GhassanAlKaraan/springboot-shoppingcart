FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/your-app.jar /app/your-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "your-app.jar"]
