FROM openjdk:17
COPY build/libs/ms-accounts-group61-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
CMD ["java","-Dspring.profiles.active=prod", "-jar", "app.jar"]