FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Устанавливаем Node.js и npm
RUN apt-get update && apt-get install -y nodejs npm

# Копируем package.json и устанавливаем зависимости
COPY package*.json ./
RUN npm install

# Копируем остальной код
COPY . .

# Копируем готовый фронтенд из пакета Hexlet (ДО сборки Gradle)
RUN mkdir -p src/main/resources/static
RUN cp -r node_modules/@hexlet/java-task-manager-frontend/dist/* src/main/resources/static/

# Собираем JAR
RUN ./gradlew build -x test --no-daemon

# Второй этап: создаем легкий образ для запуска
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем собранный JAR из первого этапа
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Запускаем JAR
CMD ["java", "-Xmx256m", "-jar", "app.jar"]
