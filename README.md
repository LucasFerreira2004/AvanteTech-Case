# Case Técnico — Controle de Produtos e Categorias

Este projeto é uma aplicação simples para **controle de produtos e categorias**, desenvolvida como parte de um case técnico.  
A aplicação permite o **cadastro, listagem, atualização e exclusão (soft delete)** de produtos e categorias, com autenticação JWT, cache, paginação e migrações de banco de dados automatizadas.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**  
- **Spring Boot**
- **PostgreSQL** — Banco de dados relacional  
- **Maven** — Gerenciador de dependências
- **Spring Data JPA**  
- **Spring Security** — Autenticação via **JWT Tokens**  
- **Flyway** — Gerenciamento de Migrations  
- **Caffeine** — Cache para o endpoint de listagem de produtos  
- **Testes de Integração** — testes de integração implementados com MockMVC
- **Paginacação** — implementada para as rotas de get através do recurso Pageable

---

## ⚙️ Instalação e Execução Local

### Pré-requisitos
- Java 21
- Docker
- Docker Compose

### Passos para executar localmente

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com//LucasFerreira2004/AvanteTech-Case.git
   cd avante-case
   ```

2. **Configurar variáveis de ambiente** (veja seção abaixo).

3. **Executar o docker compose - docker-compose.dev.yaml**
   ```bash
   docker-compose -f docker/docker-compose.dev.yaml --env-file .env up --build
   ```
4. **Executar o projeto java na IDE de sua escolha**
  
5. A aplicação estará disponível em:  
   ```
   http://localhost:8080
   ```
---
## 🌱 Variáveis de Ambiente Necessárias

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis (ou configure-as diretamente no sistema):

```env
# Banco de dados
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/avante_case
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=12345678

POSTGRES_DB=case
POSTGRES_USER=postgres
POSTGRES_PASSWORD=12345678

#PgAdmin
PGADMIN_DEFAULT_EMAIL=me@example.com
PGADMIN_DEFAULT_PASSWORD=12345678

# JWT
JWT_SECRET=seuSegredoJWT
```

---

## 🔑 Autenticação

A aplicação utiliza **JWT Tokens** para autenticação.  
Um usuário padrão está disponível para testes:

```
email: avante@gmail.com
password: avante
```

Use esses dados para gerar o token de autenticação e acessar as rotas protegidas.
**As únicas rotas não protegidas da aplicação são:**
- get /produtos
- get /categorias
- get /swagger-ui/index.html
- post /auth/login

---

## 📜 Documentação dos Endpoints

A documentação completa pode ser acessada via Swagger:

👉 (https://avantetech-case-production.up.railway.app/swagger-ui/index.html)

---

## 📄 Exemplos de Requisições

### 🔐 Login

**POST** `/auth/login`
```json
{
  "email": "avante@gmail.com",
  "password": "avante"
}
```

**Resposta**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI..."
}
```

---

### 📦 Listar Produtos

**GET** `/produtos?page=0&size=1`

> As páginas são **0-indexadas**, ou seja, a primeira página é `page=0`.

**Resposta**
```json
{
  "content": [
    {
      "id": 1,
      "nome": "Camiseta Avante",
      "preco": 59.90,
      "categoria": {
        "id": 1,
        "nome": "Roupas"
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1
  },
  "totalPages": 5,
  "totalElements": 5
}
```

---

### 🗑️ Soft Delete de Produto

**DELETE** `/produtos/{id}`  
Marca o produto como inativo, sem removê-lo do banco de dados.

**Resposta**
```json
  //json vazio e status 200 OK  
```
---



## ☁️ Aplicação em Produção

A aplicação está hospedada na **Railway** e pode ser acessada em:
- 👉 (https://avantetech-case-production.up.railway.app/swagger-ui/index.html)

Para enviar requisições a API o caminho base é:
- 👉 (https://avantetech-case-production.up.railway.app)


---

## 🧠 Recursos Implementados

- ✅ **Autenticação JWT** com usuários padrão  
- ✅ **Testes de integração** com MockMVC  
- ✅ **Paginação** com `Pageable` (`page=0&size=1`)  
- ✅ **Cache Caffeine** para listagem de produtos  
- ✅ **Flyway** para controle de versão do banco de dados  
- ✅ **Soft Delete** para produtos  
