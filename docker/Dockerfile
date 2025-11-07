FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

COPY .env .env
COPY . .

RUN mvn -e clean install -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /build/.env .env
COPY --from=builder /build/target/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

