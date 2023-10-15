# Используем базовый образ с Java
FROM axiom/docker-erddap:latest-jdk17-openjdk

# Создаем директорию app внутри Docker-образа
WORKDIR /app

# Копируем собранный .jar файл внутрь Docker-образа
COPY ./build/libs/Avenir-0.0.1-SNAPSHOT.jar /app/app.jar

# Устанавливаем команду, которая будет выполняться при запуске Docker-контейнера
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
