# ===============================
# Build stage
# ===============================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ===============================
# Runtime stage
# ===============================
FROM eclipse-temurin:21-jre-jammy

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
