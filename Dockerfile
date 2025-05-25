# Use Java ... as base image
FROM openjdk:17
#FROM ubuntu:latest
LABEL authors="hiiico"

# Copy the Spring Boot JAR file into the container
#COPY target/vacation_planning-*.jar app.jar
COPY target/vacation_planning-*.jar urlaubsplanung.jar

# define how to run the application
#ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "urlaubsplanung.jar"]

