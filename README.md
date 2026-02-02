# 🐾 PetSafe QR - Sistema de Identificação de Pets com QR Code

Sistema completo de identificação e gerenciamento de pets utilizando QR Code. Desenvolvido com Spring Boot (Java 17+) no backend e React no frontend, o PetSafe QR permite que donos de pets criem perfis completos com informações médicas e de contato, gerando um QR Code único que pode ser usado em coleiras. Possui funcionalidade especial de "Pet Perdido" com alertas visuais destacados.

## 📋 Índice

- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Funcionalidades Principais](#funcionalidades-principais)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Como Executar](#como-executar)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Screenshots e Descrição das Telas](#screenshots-e-descrição-das-telas)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Autor](#autor)

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **PostgreSQL** - Banco de dados relacional
- **JWT (jjwt 0.12.3)** - Autenticação e autorização
- **Lombok** - Redução de boilerplate
- **BCrypt** - Hash de senhas
- **Maven** - Gerenciamento de dependências

### Frontend
- **React 18.2.0**
- **React Router DOM 6.20.1** - Navegação
- **Tailwind CSS 3.3.6** - Estilização
- **Axios 1.6.2** - Requisições HTTP
- **qrcode.react 3.1.0** - Geração de QR Codes
- **Context API** - Gerenciamento de estado

## ✨ Funcionalidades Principais

1. **Autenticação Segura**
   - Cadastro de usuários com validação
   - Login com JWT
   - Proteção de rotas

2. **Gerenciamento de Pets**
   - CRUD completo de pets (apenas o dono pode editar)
   - Upload de fotos
   - Campos completos: nome, espécie, raça, idade, cor, peso
   - Informações médicas: condições, alergias, medicações
   - Contato do veterinário
   - Notas personalizadas do dono

3. **QR Code Único**
   - Geração automática de QR Code para cada pet
   - Link para página pública acessível por qualquer pessoa
   - Possibilidade de impressão para coleira

4. **Modo "Pet Perdido"**
   - Botão para marcar pet como perdido
   - Modal de confirmação antes de ativar/desativar
   - Página pública com transformação visual drástica:
     - Bordas vermelhas pulsantes (animação CSS)
     - Banner grande: "🚨 ESTOU PERDIDO - POR FAVOR ME AJUDE 🚨"
     - Botão de contato do dono destacado e animado
     - Informações de saúde em destaque

5. **Página Pública Responsiva**
   - Design fofo com cores pastéis
   - Exibição de foto, informações básicas e de saúde
   - Contato direto com o dono via telefone
   - Modo diferenciado para pets perdidos

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17 ou superior**
  ```bash
  java -version
  ```

- **Maven 3.6+**
  ```bash
  mvn -version
  ```

- **Node.js 16+ e npm**
  ```bash
  node -v
  npm -v
  ```

- **PostgreSQL 12+**
  ```bash
  psql --version
  ```

## 🔧 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone  https://github.com/Valdemar-Andrade/PetSafe-QR-backend.git
cd petsafe_qr
```

### 2. Configuração do Banco de Dados

**Crie o banco de dados PostgreSQL:**

```bash
# Acesse o PostgreSQL
psql -U postgres

# Crie o banco
CREATE DATABASE petsafe_db;

# Saia do psql
\q
```

**Configure as credenciais em `backend/src/main/resources/application.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/petsafe_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Configuração do Backend

```bash
cd backend

# Instale as dependências
mvn clean install

# (Opcional) Execute os testes
mvn test
```

### 4. Configuração do Frontend

```bash
cd frontend

# Instale as dependências
npm install
```

**Configure a URL da API em `frontend/.env`:**

```env
REACT_APP_API_URL=http://localhost:8080
```

## ▶️ Como Executar

### Executar o Backend

**Opção 1: Usando Maven**
```bash
cd backend
mvn spring-boot:run
```

**Opção 2: Usando o JAR compilado**
```bash
cd backend
mvn clean package
java -jar target/petsafe-qr-1.0.0.jar
```

O backend estará rodando em: `http://localhost:8080`

### Executar o Frontend

```bash
cd frontend
npm start
```

O frontend estará rodando em: `http://localhost:3000`

**Nota:** O frontend abrirá automaticamente no navegador.

## 📁 Estrutura do Projeto

```
petsafe_qr/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/petsafe/qr/
│   │   │   │   ├── config/          # Configurações (Security, Web)
│   │   │   │   ├── controller/      # Controllers REST
│   │   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── entity/           # Entidades JPA
│   │   │   │   ├── exception/        # Exceções customizadas e Handler
│   │   │   │   ├── repository/       # Repositories JPA
│   │   │   │   ├── security/         # JWT, UserDetails, Filters
│   │   │   │   └── service/          # Lógica de negócio
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── pom.xml
│   └── uploads/                      # Diretório de uploads (criado em runtime)
│
├── frontend/
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── components/              # Componentes reutilizáveis
│   │   │   ├── AlertModal.js
│   │   │   ├── LoadingSpinner.js
│   │   │   ├── Navbar.js
│   │   │   ├── PetCard.js
│   │   │   └── ProtectedRoute.js
│   │   ├── context/                 # Context API
│   │   │   └── AuthContext.js
│   │   ├── pages/                   # Páginas principais
│   │   │   ├── AddPet.js
│   │   │   ├── Dashboard.js
│   │   │   ├── EditPet.js
│   │   │   ├── Login.js
│   │   │   ├── PetDetails.js
│   │   │   ├── PublicPetPage.js
│   │   │   └── Register.js
│   │   ├── services/                # Serviços de API
│   │   │   ├── api.js
│   │   │   ├── authService.js
│   │   │   └── petService.js
│   │   ├── App.js
│   │   ├── index.js
│   │   └── index.css
│   ├── .env
│   ├── package.json
│   ├── tailwind.config.js
│   └── postcss.config.js
│
└── README.md
```

## 🔌 Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/auth/register` | Cadastro de usuário | Não |
| POST | `/api/auth/login` | Login de usuário | Não |

**Exemplo de Request - Register:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123",
  "phone": "(11) 99999-9999"
}
```

**Exemplo de Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "João Silva",
  "email": "joao@email.com"
}
```

### Pets (Protegidos - Requer JWT)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/pets` | Criar novo pet | Sim |
| GET | `/api/pets` | Listar pets do usuário | Sim |
| GET | `/api/pets/{id}` | Detalhes de um pet | Sim (apenas dono) |
| PUT | `/api/pets/{id}` | Atualizar pet | Sim (apenas dono) |
| DELETE | `/api/pets/{id}` | Deletar pet | Sim (apenas dono) |
| PATCH | `/api/pets/{id}/missing` | Toggle status perdido | Sim (apenas dono) |
| POST | `/api/pets/{id}/photo` | Upload de foto | Sim (apenas dono) |

**Exemplo de Request - Criar Pet:**
```json
{
  "name": "Thor",
  "species": "Cão",
  "breed": "Golden Retriever",
  "age": 3,
  "color": "Dourado",
  "weight": 30.5,
  "medicalInfo": "Saudável, vacinado",
  "allergies": "Nenhuma",
  "medications": "Nenhuma",
  "vetContact": "Clínica Bichos & Cia - (11) 3333-3333",
  "ownerNotes": "Muito dócil e brincalhão"
}
```

### Público (Sem Autenticação)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/api/public/pet/{uuid}` | Página pública do pet | Não |

## 🖼️ Screenshots e Descrição das Telas

### 1. Login / Registro
- Design moderno com gradientes coloridos
- Formulários com validação
- Feedback de erros

### 2. Dashboard
- Lista de cards dos pets do usuário
- Botão "Adicionar Pet"
- Badge visual para pets perdidos
- Design responsivo em grid

### 3. Adicionar/Editar Pet
- Formulário completo dividido em seções:
  - Informações Básicas (nome, espécie, raça, idade, cor, peso)
  - Informações de Saúde (médicas, alergias, medicações, veterinário)
  - Observações (notas do dono)
- Validações no frontend e backend

### 4. Detalhes do Pet
- **Coluna Esquerda:**
  - Foto do pet com upload
  - QR Code gerado com qrcode.react
  - Link para página pública
- **Coluna Direita:**
  - Informações completas
  - Botões de ação: Editar, Marcar como Perdido, Deletar
  - Modais de confirmação

### 5. Página Pública (/p/{uuid})
- **Modo Normal:**
  - Design fofo com gradientes pastéis
  - Foto do pet
  - Informações básicas e de saúde
  - Contato do dono destacado

- **Modo Pet Perdido:**
  - Bordas vermelhas pulsantes (animação CSS)
  - Banner vermelho grande: "🚨 ESTOU PERDIDO - POR FAVOR ME AJUDE 🚨"
  - Botão de contato gigante e animado
  - Background vermelho claro
  - Informações de saúde com destaque especial

## 🔐 Variáveis de Ambiente

### Backend (`application.properties`)

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/petsafe_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
app.jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
app.jwt.expiration=86400000

# Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

**⚠️ Importante:** Altere o `app.jwt.secret` para um valor secreto seguro em produção.

### Frontend (`.env`)

```env
REACT_APP_API_URL=http://localhost:8080
```

## 🎨 Recursos Visuais Especiais

### Animações CSS

**Bordas Pulsantes (Pet Perdido):**
```css
@keyframes pulseBorder {
  0%, 100% { 
    borderColor: #ef4444; 
    boxShadow: 0 0 0 0 rgba(239, 68, 68, 0.7);
  }
  50% { 
    borderColor: #dc2626; 
    boxShadow: 0 0 0 10px rgba(239, 68, 68, 0);
  }
}
```

Aplicado via Tailwind: `animate-pulse-border`

## 🧪 Testes

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend
npm test
```

## 🚀 Deploy

### Backend

**Gerar JAR:**
```bash
cd backend
mvn clean package
```

O arquivo será gerado em `target/petsafe-qr-1.0.0.jar`

**Executar em produção:**
```bash
java -jar target/petsafe-qr-1.0.0.jar --spring.profiles.active=prod
```

### Frontend

**Build para produção:**
```bash
cd frontend
npm run build
```

Os arquivos estáticos serão gerados em `build/`

## 📝 Boas Práticas Implementadas

- ✅ **SOLID Principles**
- ✅ **Clean Code**
- ✅ **DTOs para transferência de dados**
- ✅ **Global Exception Handler**
- ✅ **Validações com Bean Validation**
- ✅ **Segurança com Spring Security e JWT**
- ✅ **Bcrypt para senhas**
- ✅ **Interceptors no Axios**
- ✅ **Context API para estado global**
- ✅ **Componentes reutilizáveis**
- ✅ **Design responsivo (mobile-first)**
- ✅ **Separação de responsabilidades**

## Projeto Relacionado
- [PetSafe-QR-front](https://github.com/Valdemar-Andrade/PetSafe-QR-front) - PetSafe-QR-frontend

## 👤 Autor
- GitHub: [@Valdemar-Andrade]
- LinkedIn: [Valdemar Valdemar](https://www.linkedin.com/in/valdemar-andrade-8b0b45189)

Desenvolvido como projeto de portfólio demonstrando habilidades em:
- **Backend:** Java, Spring Boot, Spring Security, JWT, JPA/Hibernate, PostgreSQL
- **Frontend:** React, Tailwind CSS, Context API, React Router
- **Arquitetura:** REST API, Autenticação JWT, Upload de arquivos
- **Boas Práticas:** SOLID, Clean Code, Exception Handling, Validações

---

**PetSafe QR** - Mantendo seus pets seguros com tecnologia! 🐾❤️
