FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

COPY . .

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]