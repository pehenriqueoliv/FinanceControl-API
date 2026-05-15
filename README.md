# FinanceControl API

API REST de controle de finanças pessoais desenvolvida com Java 21, Spring Boot e PostgreSQL. Possui gerenciamento de transações com filtros e paginação, organização por categorias e resumo de saldo.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Data JPA
- PostgreSQL
- Lombok
- Bean Validation

## Como executar

### Pré-requisitos

- Java 21
- Maven
- PostgreSQL

### Configuração do banco de dados

Crie um banco de dados no PostgreSQL e configure o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financecontrol
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Rodando o projeto

```bash
./mvnw spring:boot run
```

A API estará disponível em `http://localhost:8080`.

---

## Endpoints

### Usuários `/api/users`

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/users` | Criar usuário |
| GET | `/api/users` | Listar usuários |
| GET | `/api/users/{id}` | Buscar por ID |
| DELETE | `/api/users/{id}` | Deletar usuário |

### Categorias `/api/categories`

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/categories` | Criar categoria |
| GET | `/api/categories` | Listar categorias |
| GET | `/api/categories?type=INCOME` | Filtrar por tipo |
| GET | `/api/categories/{id}` | Buscar por ID |
| DELETE | `/api/categories/{id}` | Deletar categoria |

### Transações `/api/transactions`

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/transactions` | Criar transação |
| GET | `/api/transactions?userId=1` | Listar com filtros |
| GET | `/api/transactions/{id}` | Buscar por ID |
| PUT | `/api/transactions/{id}` | Atualizar transação |
| DELETE | `/api/transactions/{id}` | Deletar transação |
| GET | `/api/transactions/summary?userId=1` | Resumo de saldo |

#### Filtros disponíveis em `GET /api/transactions`

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `userId` | Long | **Obrigatório** |
| `type` | String | `INCOME` ou `EXPENSE` |
| `categoryId` | Long | ID da categoria |
| `startDate` | Date | Formato `YYYY-MM-DD` |
| `endDate` | Date | Formato `YYYY-MM-DD` |
| `page` | int | Número da página (padrão: 0) |
| `size` | int | Itens por página (padrão: 10) |

---

## Exemplos de requisição

### Criar usuário
```json
POST /api/users
{
  "name": "Pedro Henrique",
  "email": "pedro@email.com"
}
```

### Criar categoria
```json
POST /api/categories
{
  "name": "Salário",
  "type": "INCOME"
}
```

### Criar transação
```json
POST /api/transactions
{
  "description": "Salário de maio",
  "amount": 3500.00,
  "type": "INCOME",
  "date": "2026-05-01",
  "categoryId": 1,
  "userId": 1
}
```
