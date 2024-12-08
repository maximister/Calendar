
# Используем официальный образ OpenJDK для базового слоя
FROM openjdk:17-jdk-alpine

# Сеттинги рабочей директории
WORKDIR /app

# Копируем сборочный артефакт вашего приложения в контейнер
COPY target/event-management-service.jar /app/event-management-service.jar

# Указываем команду для запуска вашего приложения
ENTRYPOINT ["java", "-jar", "/app/event-management-service.jar"]
