# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy everything (so multi-module build works)
COPY pom.xml .
COPY src ./src

# Move into the module and build just that
#WORKDIR /app
RUN mvn -B -DskipTests clean package

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
