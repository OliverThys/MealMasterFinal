# Étape 1 : Build avec Maven + JDK 17
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution avec JDK 17 léger
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/MealMasterFinal-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
