# Этап 1: Финальный образ (Java)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Копируем предварительно собранный JAR-файл приложения
COPY build/libs/moule1-1.0.0.jar app.jar

# Создаем пользователя appuser и даем ему права на запись
RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser

# Открываем порт для приложения
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
