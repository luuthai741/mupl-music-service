# ====== Build Stage ======
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven Wrapper & Dependencies
COPY mvnw .mvn/ ./
COPY pom.xml ./

# Download Dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code & Build application
COPY src ./src
RUN ./mvnw package -DskipTests

# ====== Runtime Stage ======
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Giảm log level của JVM & kích hoạt Garbage Collector
ENV JAVA_OPTS="-XX:+UseZGC -Xmx512m -Xms256m"

# Copy file từ build stage
COPY --from=builder /app/target/*.jar app.jar

# Mở cổng mặc định của ứng dụng
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
