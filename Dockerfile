# ══════════════════════════════════════════════
# Stage 1 — BUILD
# ══════════════════════════════════════════════
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./pom.xml
COPY rest-api-composite/pom.xml ./rest-api-composite/pom.xml
COPY server-composite/pom.xml ./server-composite/pom.xml

RUN mvn dependency:go-offline -q

COPY rest-api-composite/src ./rest-api-composite/src
COPY server-composite/src ./server-composite/src

RUN mvn clean package -DskipTests -q

# ══════════════════════════════════════════════
# Stage 2 — RUNTIME
# ══════════════════════════════════════════════
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

LABEL maintainer="fode_diakite" \
      service="composite-service" \
      version="0.0.1-SNAPSHOT"

RUN apk add --no-cache curl && \
    addgroup -S odoru && \
    adduser -S odoru -G odoru && \
    mkdir -p /app/config && \
    chown -R odoru:odoru /app

COPY --from=build --chown=odoru:odoru /app/server-composite/target/composite-service.jar app.jar

EXPOSE 8083

USER odoru

ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-XX:+ExitOnOutOfMemoryError", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dspring.cloud.config.import-check.enabled=false", \
            "-jar", \
            "app.jar"]
