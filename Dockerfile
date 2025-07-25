# Build stage
FROM maven:3.9.3-eclipse-temurin-17-alpine AS build
COPY . /home/app
WORKDIR /home/app

# Verifica o diretório atual e lista o conteúdo para debug
RUN pwd && ls -la

# Compila o projeto, ignorando os testes para ser mais rápido
RUN mvn clean install

# Package stage
FROM bellsoft/liberica-openjdk-alpine:17.0.4
RUN addgroup demogroup && adduser --ingroup demogroup --disabled-password goldenfingers98
USER goldenfingers98

# Adicione as variáveis de ambiente para o banco de dados
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgresqldb:5432/db-desafio-backend
ENV SPRING_DATASOURCE_USERNAME=desafio
ENV SPRING_DATASOURCE_PASSWORD=desafio

# Expõe a porta 8080
EXPOSE 8080
ARG JAR_FILE=/home/app/application/target/*.jar
COPY --from=build ${JAR_FILE} /app.jar
# Comando de inicialização da aplicação
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
