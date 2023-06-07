# Build application with containerized gradle wrapper.
FROM docker.io/eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

# Extract JAR layers.
FROM docker.io/eclipse-temurin:17-jdk-alpine AS extractor
WORKDIR /app
COPY --from=builder /app/build/uniquity-backend.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Copy JAR contents into final layer.
FROM docker.io/eclipse-temurin:17-jdk-alpine AS runner
WORKDIR /app

COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

# The container has its 8080 port open, but it may be mapped to a different host port.
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
