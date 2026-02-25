# Estágio de build (builder)
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace/app

# Copia os arquivos essenciais do maven e o pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dá permissão de execução ao mvnw e baixa as dependências (faz cache na camada Docker)
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Copia o restante do código-fonte
COPY src src

# Faz o build pulando os testes para ser mais rápido (os testes rodam no CI)
RUN ./mvnw clean package -DskipTests

# Estágio de runtime (produção)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Adiciona um usuário não-root por questões de segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Variáveis de ambiente com valores padrão (opcional)
ENV PORT=8081

# Copia apenas o JAR final do estágio de build
COPY --from=builder /workspace/app/target/*.jar app.jar

# Expõe a porta que a aplicação escuta
EXPOSE ${PORT}

# Ponto de entrada
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
