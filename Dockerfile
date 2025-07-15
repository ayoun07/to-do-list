FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests
RUN chmod +x wait-for-it.sh
CMD ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
