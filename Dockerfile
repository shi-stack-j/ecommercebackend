FROM eclipse-temurin:22-jre-jammy

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY target/*-SNAPSHOT.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8081

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
