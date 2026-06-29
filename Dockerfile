FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew build || return 0

COPY . .
RUN ./gradlew build

EXPOSE 8080

CMD ["./gradlew", "bootRun"]
