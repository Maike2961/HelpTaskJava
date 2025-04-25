# 🛠️ Sistema de Chamados - Spring Boot + Docker + AWS LocalStack

Este projeto é uma API REST de gerenciamento de chamados desenvolvida com Spring Boot, utilizando MySQL em Docker, autenticação JWT e integração com serviços simulados da AWS via LocalStack (SNS).

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
    - Spring Security (JWT)
    - Spring Data JPA
    - Validation
- **MySQL** (via Docker Compose)
- **Docker / Docker Compose**
- **AWS SDK v2 (SNS)**
- **LocalStack** (simulando AWS)
- **JUnit + Mockito** para testes

---

## 🧩 Funcionalidades

- Cadastro e login de usuários com autenticação JWT
- Criação, listagem, atualização e filtragem de chamados
- Controle de acesso por roles (`user`, `admin`, `tecnical`)
- Notificação via SNS quando um chamado é concluído, cancelado ou apresenta erro

---

## 🧪 Requisitos

- Java 17+
- Docker e Docker Compose
- Localstack

---

## ⚙️ Executando o Projeto

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio
