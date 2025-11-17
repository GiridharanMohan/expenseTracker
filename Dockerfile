FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
RUN ./mvnw -q -DskipTests package
CMD ["java", "-jar", "target/expenseTracker-0.0.1-SNAPSHOT.jar"]