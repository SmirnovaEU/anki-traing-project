FROM openjdk:11-alpine
COPY /training-system/target/anki-training-system.jar /app/anki-training-system.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/anki-training-system.jar"]