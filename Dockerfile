FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/dreamshops-0.0.1-SNAPSHOT.jar /app/dreamshops.jar

EXPOSE 9191

ENTRYPOINT ["java", "-jar", "dreamshops.jar"]