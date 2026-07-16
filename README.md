# Проект №5: Менеджер задач (Java)

[![CI](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/ci.yml/badge.svg)](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/ci.yml)
[![Hexlet Check](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/hexlet-check.yml)
[![SonarQube Analysis](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/sonarqube.yml/badge.svg)](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/sonarqube.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bobbidenisleo-glitch_java-project-99&metric=alert_status)](https://sonarcloud.io/dashboard?id=bobbidenisleo-glitch_java-project-99)

## Описание проекта

Это учебный проект, разработанный в рамках курса Хекслет. 
Представляет собой бэкенд для менеджера задач, созданный на Spring Boot.

## Технологии

- **Java 21**
- **Spring Boot 3.5.16**
- **Gradle 8.7+**
- **PostgreSQL**
- **Render** (деплой)

## Деплой

Приложение развернуто на платформе Render.

**Ссылка на работающее приложение:**
[https://java-project-99-p96b.onrender.com/welcome](https://java-project-99-p96b.onrender.com/welcome)

*При переходе по ссылке вы увидите текст "Welcome to Spring".*

## Как запустить проект локально

### Требования

- **Java 21** (проверьте командой `java -version`)
- **Gradle 8.7+** (проверьте командой `gradle --version`)

### Установка и запуск

1. Склонируйте репозиторий и перейдите в папку проекта:

```bash
git clone git@github.com:bobbidenisleo-glitch/java-project-99.git
cd java-project-99
```

2. Соберите проект:

```bash
./gradlew build
```

3. Запустите приложение:

```bash
./gradlew bootRun
```

4. Откройте в браузере:

```
http://localhost:8080/welcome
```

## Статус сборки

[![Java CI](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/bobbidenisleo-glitch/java-project-99/actions/workflows/hexlet-check.yml)

## Тестовые данные для входа

Для проверки работы приложения вы можете использовать следующие тестовые учётные записи.

### Администратор

- **Сайт (для входа в систему):** https://java-project-99-p96b.onrender.com
- **Имя пользователя (email):** `hexlet@example.com`
- **Пароль:** `qwerty`
- **API-эндпоинт для получения токена:** `POST /api/login` (для разработчиков)

### Обычный пользователь

- **Имя пользователя (email):** `test@example.com`
- **Пароль:** `password123`

> **Примечание:** Если эти пользователи не существуют в базе данных, вы можете создать их через эндпоинт `POST /api/users` или зарегистрироваться через интерфейс приложения.

## Автор

**bobbidenisleo-glitch**  
GitHub: [https://github.com/bobbidenisleo-glitch](https://github.com/bobbidenisleo-glitch)
