# Backend do KL Club (Spring Boot 4 / Java 21)

# ---- Build: compila e empacota o jar com o Maven ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Baixa as dependências antes de copiar o código, para aproveitar o cache das camadas
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src
RUN mvn -q -B -DskipTests package

# ---- Runtime: só o JRE e o jar ----
FROM eclipse-temurin:21-jre
WORKDIR /app

ENV TZ=America/Sao_Paulo

COPY --from=build /app/target/service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
