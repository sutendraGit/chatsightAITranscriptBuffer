FROM maven:3.8.5-openjdk-17 AS build
LABEL description="Live Chat Buffer Service"

WORKDIR /app

# Copy Maven config and source code
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim AS runtime
LABEL MAINTAINER="sutendra.mr@247.ai"
LABEL company="247ai"
LABEL description="Service used for buffering and fetching live chat transcripts"

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/scripts /app/scripts/
COPY --from=build /app/pom.xml /app/pom.xml
COPY --from=build /app/target/*.jar live-chat-buffer-service.jar
COPY --from=build /app/src/main/resources/docker.application.properties /app/src/main/resources/application.properties

# Expose the default Spring Boot port
EXPOSE 8080

RUN chmod 777 /app/scripts/app-entrypoint.sh

# Run the Spring Boot application
#ENTRYPOINT ["java", "-jar", "live-chat-buffer-service.jar"]
