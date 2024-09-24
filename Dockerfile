
# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application jar file to the container
COPY target/unitech-app.jar /app/unitech-app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "/app/unitech-app.jar"]
