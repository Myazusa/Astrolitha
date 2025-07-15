FROM openjdk:24

WORKDIR /app

COPY target/AstrolithaBackend-1.0.0-beta.jar ./AstrolithaBackend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "AstrolithaBackend.jar"]