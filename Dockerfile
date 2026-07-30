FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src src
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests

FROM eclipse-temurin:17-jre
RUN groupadd --system phishsense && useradd --system --gid phishsense phishsense
WORKDIR /app

COPY --from=build /workspace/target/phish-awareness-sim-1.0.0-SNAPSHOT.jar app.jar

ENV PORT=10000
EXPOSE 10000
USER phishsense

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
