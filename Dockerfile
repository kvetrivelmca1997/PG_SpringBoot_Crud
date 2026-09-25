# ---- Stage 1: build the jar with Maven + JDK 17 (also runs the tests on H2) ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package

# ---- Stage 2: run the jar on a small Java 17 runtime ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /src/target/app.jar app.jar
USER app
EXPOSE 9100
HEALTHCHECK --interval=15s --timeout=3s --start-period=40s --retries=5 \
  CMD wget -qO- http://localhost:9100/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
