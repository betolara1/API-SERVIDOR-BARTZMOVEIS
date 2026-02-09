# API Bartz Móveis - ERP 🚀

API desenvolvida para realizar consultas no banco de dados **IBM DB2** do servidor Bartz Móveis, fornecendo informações essenciais sobre itens e produtos de forma rápida e eficiente.

---

## 📚 Documentação (Swagger)

A API conta com documentação interativa via Swagger UI para facilitar o teste e a integração dos endpoints.

- **URL Local:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🛠️ Tecnologias

O projeto utiliza as seguintes tecnologias e dependências:

- **Java 21**
- **Spring Boot 4.0.2**
- **Spring Data JPA** (Hibernate)
- **SpringDoc OpenAPI** (Swagger UI)
- **IBM DB2 JCC** (Driver de Banco de Dados)
- **Lombok** (Produtividade)
- **Maven** (Gerenciamento de dependências)

---

## 🛤️ Endpoints Principais

A API expõe o recurso `/api/erp` para consulta de itens:

| Método | Endpoint | Descrição | Parâmetros |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/erp` | Lista todos os itens cadastrados. | - |
| **GET** | `/api/erp/find-by-code` | Busca um item específico pelo código exato. | `code` (String) |
| **GET** | `/api/erp/find-by-description` | Busca um item específico pela descrição exata. | `desc` (String) |
| **GET** | `/api/erp/search-code` | Busca parcial por código (contém). | `q` (String) |
| **GET** | `/api/erp/search-desc` | Busca parcial por descrição (contém). | `q` (String) |

### Exemplos de Requisição

- **Buscar por Código (Exato):**
  `GET http://localhost:8080/api/erp/find-by-code?code=12345`

- **Buscar por Descrição (Exato):**
  `GET http://localhost:8080/api/erp/find-by-description?desc=armario`

- **Busca Parcial por Código:**
  `GET http://localhost:8080/api/erp/search-code?q=10.01`

- **Busca Parcial por Descrição:**
  `GET http://localhost:8080/api/erp/search-desc?q=branco`

---

## 🚀 Como Executar

### Pré-requisitos
- **Java 21 LTS** ou superior.
- Banco de dados **IBM DB2** configurado e acessível.

### Passos para rodar localmente
1. Clone o repositório.
2. Configure as credenciais do banco em `src/main/resources/application.properties`.
3. Navegue até a pasta raiz e execute:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(No Windows, utilize `mvnw.cmd`)*

---

## 📄 Estrutura de Dados

A API mapeia a tabela `ITEM` com os seguintes atributos principais:
- `codeItem`: Código único do item.
- `description`: Descrição detalhada do produto.
- `refComercial`: Referência comercial do item.

