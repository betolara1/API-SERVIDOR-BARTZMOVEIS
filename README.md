<div align="center">

# 🏗️ Bartz Móveis ERP API

### REST API de Integração (Ponte) para o ERP Legado (IBM DB2)

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![IBM DB2](https://img.shields.io/badge/IBM_DB2-12.1-052FAD?style=for-the-badge&logo=ibm&logoColor=white)](https://www.ibm.com/products/db2)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![JWT Auth](https://img.shields.io/badge/Auth-JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](#-segurança)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)

</div>

---

## 📸 Preview (Swagger UI)

<div align="center">
  <img src="images/swagger-v3.png" alt="Swagger UI Preview" width="100%">
</div>

---

## 📌 Sobre o Projeto

A **Bartz Móveis ERP API** atua como uma **Camada Anticorrupção (Ponte)** entre o frontend moderno e o sistema ERP legado (IBM DB2). 

Para garantir a máxima performance e simplicidade no acesso aos dados legados, a API utiliza **Spring JDBC (JdbcTemplate)** em vez de um ORM completo. Isso permite consultas SQL nativas otimizadas para o DB2, retornando dados diretamente em DTOs (Data Transfer Objects), eliminando o overhead de gerenciamento de entidades JPA em um cenário de leitura intensiva.

Construída com foco em **produção real**, a API incorpora:

- ✅ **Arquitetura Simplificada** (Controller → Service → JdbcTemplate)
- ✅ **Segurança Stateless** via JWT (Pacote Modular `jwt-package`)
- ✅ **Consultas Nativas** otimizadas para IBM DB2
- ✅ **Consulta Estrutural Hierárquica (Árvore de Itens / EPM019)**
- ✅ **Containerização completa** com Docker e Docker Compose
- ✅ **Documentação interativa** com Swagger / OpenAPI 3
- ✅ **Monitoramento de Saúde** via Spring Boot Actuator
- ✅ **Suíte de testes** abrangente (Controllers e Services)
- ✅ **Tratamento Global de Erros** padronizado

---

## 🏛️ Arquitetura

### Fluxo de Dados (Stateless)

```mermaid
flowchart TB
  subgraph Application
    Auth["AuthController"] --> Jwt["JwtUtil"]
    A1["ItemController"] --> B1["ItemService"]
    A2["CorController"] --> B2["CorService"]
    B1 --> C1["JdbcTemplate"]
    B2 --> C1
    C1 --> D["IBM DB2 Database"]
  end
```

### Estrutura de Pastas

```
📦 apigetitem
 ├── 🔐 security/            # Filtros de segurança (JWT-Package)
 ├── ⚙️ config/              # Configurações de Segurança, CORS e Swagger
 ├── 📡 controller/          # Endpoints REST (Auth, Item, Cor)
 ├── 🧩 service/             # Regras de negócio e Consultas SQL (JdbcTemplate)
 ├── 📤 dto/                 # Data Transfer Objects (ItemDTO, CorDTO, ItemNodeDTO, LoginDTO)
 └── ⚠️ exceptions/          # Tratamento global de erros (GlobalExceptionHandler)
```

---

## 🚀 Endpoints da API

### 🔑 Autenticação (`/auth`)
| Método | Endpoint | Parâmetros Query | Descrição | Auth |
|--------|----------|------------------|-----------|------|
| `GET` | `/auth/login` | `username`, `password` | Autenticação via Query Params (retorna o Token JWT) | ❌ |

### 📦 Itens (`/itens`)
| Método | Endpoint | Parâmetros Query | Descrição | Auth |
|--------|----------|------------------|-----------|------|
| `GET` | `/itens` | - | Lista todos os itens | ✅ |
| `GET` | `/itens/search` | `codigo` | Busca por código (parcial/exato) | ✅ |
| `GET` | `/itens/search` | `descricao` | Busca por descrição (parcial/exato) | ✅ |
| `GET` | `/itens/search` | `codigoBarras` | Busca por código de barras | ✅ |
| `GET` | `/itens/estrutura` | `codigo` | Retorna a estrutura hierárquica em árvore do item (EPM019) | ✅ |

### 🎨 Cores (`/cores`)
| Método | Endpoint | Parâmetros Query | Descrição | Auth |
|--------|----------|------------------|-----------|------|
| `GET` | `/cores` | - | Lista todas as cores | ✅ |
| `GET` | `/cores/search` | `codigo` | Busca por sigla/código | ✅ |
| `GET` | `/cores/search` | `descricao` | Busca por descrição | ✅ |

### 🏥 Monitoramento (`/actuator`)
| Método | Endpoint | Parâmetro | Descrição | Auth |
|--------|----------|-----------|-----------|------|
| `GET` | `/actuator/health` | - | Verificação de integridade/saúde da API | ❌ |

---

## 🔐 Segurança

A autenticação é baseada em **JWT (JSON Web Token)** de forma totalmente Stateless, utilizando o pacote modular `jwt-package`.

### Como obter o Token
Envie uma requisição `GET` para `/auth/login` com as credenciais administrativas configuradas no `.env`.

**Exemplo:**
```http
GET /auth/login?username=seu_usuario&password=sua_senha
```

**Resposta:**
```text
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZXVfdXN1YXJpbyIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDQzMjAwfQ...
```

### Uso do Token
Em todas as requisições protegidas, inclua o token no cabeçalho HTTP:

**Header Obrigatório:**
```http
Authorization: Bearer <seu_token_jwt>
```

**Fluxo Interno:**
1. O `JwtAuthFilter` intercepta a requisição.
2. Valida o token usando a `jwt.secret-key` definida no `.env`.
3. Se válido, libera o acesso às consultas ao DB2.
4. Se inválido ou expirado, retorna `401 Unauthorized`.

---

## 🧪 Testes

A API possui cobertura de testes automatizados com JUnit 5 e Mockito:

| Camada | Ferramenta | Classes de Teste |
|--------|------------|-----------------|
| **Service (Unit)** | JUnit 5 + Mockito | `BartzErpServiceTest`, `CorServiceTest` |
| **Controller (Integration)** | `@WebMvcTest` + MockMvc | `BartzErpControllerTest`, `CorControllerTest` |

```bash
# Executar todos os testes
./mvnw test
```

---

## 💻 Executando Localmente (Desenvolvimento)

Para executar a aplicação em modo de desenvolvimento local via Maven Wrapper:

```bash
# Baixar dependências e rodar a aplicação
./mvnw spring-boot:run
```

```bash
# Para gerar o arquivo .JAR
./mvnw clean package -DskipTests
```

---

## 🐳 Rodando com Docker (Produção)

**1. Configure o arquivo `.env`:**
```env
# Segurança JWT
jwt.secret-key=sua_chave_secreta_com_no_minimo_32_chars
jwt.excluded-paths=/auth/login, /swagger-ui/**, /v3/api-docs/**
jwt.expiration-time=43200000

# Conexão DB2
URL_DB=jdbc:db2://seu_host:50000/nomedobanco
USERNAME_DB=usuario_db2
PASSWORD_DB=senha_db2

# Credenciais da API (Admin)
USERNAME_LOGIN=admin
PASSWORD_LOGIN=sua_senha_criptografada_bcrypt

# Porta da Aplicação
DB_PORT=8081
```

**2. Suba o container:**
```bash
docker-compose up --build -d
```

---

## 📖 Documentação Interativa (Swagger)

Acesse: `http://localhost:{PORTA}/swagger-ui.html`

A documentação permite testar todos os endpoints. Lembre-se de clicar em **Authorize** e fornecer o token JWT (`Bearer <token>`) para chamadas aos endpoints protegidos.

---

## 📊 Estrutura de Dados (DB2)

A API mapeia as seguintes informações do banco legado:

- **Tabela `ITEM`**: Campos `ITEM` (Código), `DESCRICAO` e `REF_COMERCIAL`.
- **Tabela `COR`**: Campos `SIGLA_COR` e `DESCRICAO`.
- **Tabela `FICHABAS`**: Estrutura e composição hierárquica de produtos (`ITEM_PAI` / `ITEM_FILHO`).

---

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Finalidade |
|-----------|--------|------------|
| Java | 21 (LTS) | Linguagem principal |
| Spring Boot | 3.4.2 | Framework web e IoC |
| Spring JDBC | — | Acesso a dados via JdbcTemplate |
| Spring Security | 6.4.x | Controle de acesso via JWT |
| Spring Boot Actuator | 3.4.2 | Endpoint de integridade e monitoramento (`/actuator/health`) |
| JWT Package | 1.0.4 | Pacote customizado para gestão de tokens |
| IBM DB2 | 12.1 | Banco de dados legado |
| SpringDoc OpenAPI | 2.3.0 | Documentação Swagger |
| Lombok | — | Redução de boilerplate |
| Docker + Compose | — | Containerização |
| JUnit 5 + Mockito | — | Testes automatizados |
| Maven | 3.9+ | Gerenciamento de build |

---

## 👨‍💻 Autor

Desenvolvido por **Roberto Lara** — Backend Developer

[![GitHub](https://img.shields.io/badge/GitHub-robertolara-181717?style=for-the-badge&logo=github)](https://github.com/betolara1)

---

<div align="center">

**Bartz Móveis ERP API** — A ponte segura e performática para seus dados legados.

</div>
