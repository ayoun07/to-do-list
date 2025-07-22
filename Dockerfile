# Étape 1 : Build de l'application avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Définir le dossier de travail
WORKDIR /app

# Copier les fichiers nécessaires au build
COPY pom.xml .
COPY src ./src

# Compiler et packager l'application sans les tests
RUN mvn clean package -DskipTests

# Étape 2 : Exécution avec une image JDK légère
FROM eclipse-temurin:17-jdk-alpine

# Dossier de travail
WORKDIR /app

# Copier le JAR depuis l'image de build
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port de l'application
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]

