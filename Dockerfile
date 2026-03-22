FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

COPY pom.xml .
COPY common/pom.xml common/
COPY security/pom.xml security/
COPY auth/pom.xml auth/
COPY users/pom.xml users/
COPY teams/pom.xml teams/
COPY birthdays/pom.xml birthdays/
COPY application/pom.xml application/

RUN mvn dependency:go-offline -B

COPY common common
COPY security security
COPY auth auth
COPY users users
COPY teams teams
COPY birthdays birthdays
COPY application application

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/application/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]