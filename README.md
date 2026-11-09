# Barbershop API

API REST para gestão de agendamentos de uma barbearia, construída com Java e Spring Boot. O sistema foi projetado para ser a espinha dorsal de um SaaS, permitindo que barbeiros gerenciem sua agenda, serviços e clientes, enquanto oferece uma experiência de agendamento inteligente e segura para o cliente final.

## 🎯 O Problema

Pequenas barbearias frequentemente gerenciam sua agenda de forma manual, o que leva a conflitos de horário (double booking), perda de histórico de clientes e falta de um sistema de fidelidade eficaz. Esta API resolve esses problemas ao centralizar a gestão da agenda e das regras de negócio em um backend robusto e seguro.

## ✨ Principais Funcionalidades

- **Gestão de Agendamentos**: Criação, listagem, cancelamento e conclusão de agendamentos.
- **Prevenção de Double Booking**: Utiliza transações com isolamento `SERIALIZABLE` e uma constraint de exclusão (`EXCLUDE USING GIST`) no PostgreSQL para garantir que um barbeiro não tenha dois clientes no mesmo horário.
- **Agenda Flexível**: O barbeiro pode configurar seus dias e horários de trabalho, intervalos de almoço e datas especiais (férias, feriados).
- **Catálogo de Serviços**: CRUD completo de serviços (nome, duração, preço), com suporte a ativação/inativação (soft delete) para preservar o histórico.
- **Sistema de Fidelidade**: Clientes acumulam pontos a cada atendimento concluído. A meta de pontos é configurável pelo barbeiro (ex: a cada 10 cortes, 1 grátis).
- **Segurança**: Autenticação stateless com JWT (JSON Web Tokens) e criptografia de senhas com BCrypt. Prevenção de ataques IDOR (Insecure Direct Object Reference) ao extrair a identidade do usuário diretamente do token.

## 🛠️ Stack Tecnológico

- **Linguagem**: Java 21
- **Framework**: Spring Boot 4
- **Banco de Dados**: PostgreSQL 16
- **Migrações**: Flyway
- **Segurança**: Spring Security, JWT (Biblioteca JJWT)
- **Mapeamento de Objetos**: MapStruct
- **Containerização**: Docker e Docker Compose
- **Build**: Maven

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
- Java 21 (JDK)
- Docker e Docker Compose
- Maven (ou use o `mvnw` incluído no projeto)

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/paulohscoelho/barbershop-api.git
    cd barbershop-api