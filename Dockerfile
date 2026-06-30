FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Копируем только необходимые файлы для сборки
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

# Скачиваем зависимости (кэшируем этот слой)
RUN ./gradlew dependencies --no-daemon || return 0

# Копируем исходный код и собираем JAR
COPY src src
RUN ./gradlew build -x test --no-daemon

# Второй этап: создаем легкий образ для запуска
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем собранный JAR из первого этапа
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Запускаем JAR с ограничением памяти
CMD ["java", "-Xmx256m", "-jar", "app.jar"]
