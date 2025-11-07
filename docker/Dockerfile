FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build
COPY . .
RUN mvn dependency:go-offline
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /build/target/*.jar /app/app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xmx256m -Xms128m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
