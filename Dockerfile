FROM eclipse-temurin:21-jdk

WORKDIR /app

# Копируем только gradle-обертку
COPY gradlew gradlew
COPY gradle gradle

# Даем права на выполнение
RUN chmod +x gradlew

# Копируем файлы сборки
COPY build.gradle settings.gradle ./

# Первая сборка для загрузки зависимостей (кэшируем)
RUN ./gradlew dependencies --no-daemon || return 0

# Копируем исходный код
COPY src src

# Собираем приложение, но НЕ запускаем тесты (экономия памяти)
RUN ./gradlew build -x test --no-daemon

# Указываем порт
EXPOSE 8080

# Запускаем приложение с ограничением памяти
CMD ["./gradlew", "bootRun", "--no-daemon", "-Djava.awt.headless=true"]
