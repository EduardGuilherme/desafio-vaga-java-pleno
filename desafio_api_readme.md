# Desafio API - Gestão de Usuários

[![Java](https://img.shields.io/badge/Java-17-blue?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)
[![Flyway](https://img.shields.io/badge/Flyway-Database-red)](https://flywaydb.org/)
[![Swagger](https://img.shields.io/badge/Swagger-API-orange?logo=swagger)](https://swagger.io/)

---

## Descrição

Esta é uma **API RESTful** desenvolvida com **Spring Boot** para gerenciamento de usuários.
Permite criar, listar, atualizar e excluir usuários, com validação de departamentos através de **enumeração**.

Funciona com **H2** para desenvolvimento e **PostgreSQL** para produção.
Utiliza **Flyway** para versionamento de banco de dados e **Swagger** para documentação das rotas.

O projeto também suporta **balanceamento de carga** via **Nginx**, permitindo distribuir requisições entre múltiplas instâncias da aplicação.

---

## Tecnologias utilizadas

- Java 17  
- Spring Boot 3  
- H2 Database (desenvolvimento)  
- PostgreSQL (produção)  
- Flyway (migrations)  
- Swagger (documentação)  
- Maven  
- Docker / Docker Compose  
- Nginx (Load Balancer)  

---

## Arquitetura com Load Balancer

```text
               +-----------------+
               |     Nginx LB    |
               +--------+--------+
                        |
         -------------------------------
         |                             |
+-----------------+           +-----------------+
|  App Instance 1 |           |  App Instance 2 |
+-----------------+           +-----------------+
         |                             |
     PostgreSQL                      PostgreSQL
```

- O **Nginx** distribui as requisições HTTP entre múltiplas instâncias da aplicação (`app1`, `app2`, etc.).  
- O banco de dados PostgreSQL é compartilhado entre as instâncias.

---

---

## Configuração do banco de dados

### H2 (desenvolvimento)
- Configuração automática pelo Spring Boot.  
- URL: `jdbc:h2:mem:desafio`

### PostgreSQL (produção)
- Flyway será responsável pelas migrations automáticas.

- Mapear enum com **`AttributeConverter`**:


---

## Rodando o projeto

### Com Maven
```bash
mvn clean install
```

### Em modo Dev (hot reload)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
> Perfil `dev` usa H2 e configurações de desenvolvimento.

---



## Rodando com Docker Compose

após ajustar o docker compose rode docker compose up --build
Exemplo de configuração com múltiplas instâncias da aplicação e Nginx:

```yaml
version: "3.9"

services:
  db:
    image: postgres:13
    container_name: desafio-db
    restart: always
    environment:
      POSTGRES_DB: desafio
      SPRING_DATASOURCE_USERNAME: usuario
      SPRING_DATASOURCE_PASSWORD: suasenha
    networks:
      - desafio-api
    volumes:
      - postgres-data:/var/lib/postgresql/data

  app1:
    build: .
    container_name: desafio-app1
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/desafio
      SPRING_DATASOURCE_USERNAME: usuario
      SPRING_DATASOURCE_PASSWORD: suasenha
      SPRING_JPA_HIBERNATE_DDL_AUTO: none
      FLYWAY_ENABLED: true
      SERVER_PORT: 8080
    networks:
      - desafio-api

  app2:
    build: .
    container_name: desafio-app2
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/desafio
      SPRING_DATASOURCE_USERNAME: usuario
      SPRING_DATASOURCE_PASSWORD: suasenha
      SPRING_JPA_HIBERNATE_DDL_AUTO: none
      FLYWAY_ENABLED: true
      SERVER_PORT: 8080
    networks:
      - desafio-api

  app3:
    build: .
    container_name: desafio-app3
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/desafio
      SPRING_DATASOURCE_USERNAME: usuario
      SPRING_DATASOURCE_PASSWORD: suasenha
      SPRING_JPA_HIBERNATE_DDL_AUTO: none
      FLYWAY_ENABLED: true
      SERVER_PORT: 8080
    networks:
      - desafio-api

  lb:
    image: nginx:latest
    container_name: desafio-lb
    ports:
      - "8080:80"   # Swagger acessado via http://localhost:8080
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
    depends_on:
      - app1
      - app2
      - app3
    networks:
      - desafio-api

networks:
  desafio-api:

volumes:
  postgres-data:

```



Exemplo de `nginx.conf`:

```nginx
events {}

http {
    upstream backend_servers {
        server app1:8080;
        server app2:8080;
        server app3:8080;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://backend_servers;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }
    }
}

```

---

## Rotas (Swagger)

Acesse a documentação do Swagger em:  
```
http://localhost:8080/swagger-ui/index.html
```

### Users
| Método | Endpoint       | Descrição               |
|--------|----------------|------------------------|
| POST   | /users         | Criar usuário          |
| GET    | /users         | Listar usuários        |
| GET    | /users/{id}    | Buscar usuário por ID  |
| PUT    | /users/{id}    | Atualizar usuário      |
| DELETE | /users/{id}    | Deletar usuário        |

### Modules
| Método | Endpoint         | Descrição                 |
|--------|-----------------|--------------------------|
| GET    | /modules       | Listar modulos            |


### accessrequest
| Método | Endpoint         | Descrição                  |
|--------|-----------------|---------------------------|
| GET    | /requests/{id}     | Listar pedidos por ID         |
| GET    | /requests/search     | Listar todos os pedidos         |
| POST   | /requests     | Criar pedidos           |
|PATCH  | /requests/{id}/renew| Buscar pedidos renovados   |
|PATCH  | /requests/{id}/cancel| Buscar pedidos cancelados   |
| GET | /requests/user/{id}| Deletar categoria         |

> Outras entidades seguem padrão CRUD similar.

---

## Considerações importantes

- H2 é recomendado apenas para **desenvolvimento**.  
- PostgreSQL garante **integridade de dados** com enum real.  
- Use **AttributeConverter** para mapear enums Java para enums PostgreSQL sem erros.  
- Flyway garante versionamento do banco de dados sem conflito entre ambientes.  
- Swagger facilita testes e documentação das rotas.  
- Docker Compose permite subir o ambiente completo (app + banco + Nginx).  
- Nginx distribui as requisições entre múltiplas instâncias da API, garantindo **load balancing** e maior disponibilidade.