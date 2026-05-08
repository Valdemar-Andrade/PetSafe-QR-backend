# 🐾 PetSafe QR - Pet Identification System with QR Code

A complete pet identification and management system using QR codes. Developed with Spring Boot (Java 17+) on the backend and React on the frontend, PetSafe QR allows pet owners to create complete profiles with medical and contact information, generating a unique QR code that can be used on collars. It features a special "Lost Pet" functionality with highlighted visual alerts.

## 📋 Index

- [Technologies Used](#technologies-used)
- [Main Features](#main-features)
- [Prerequisites](#prerequisites)
- [Installation and Configuration](#installation-and-configuration)
- [How to Run](#how-to-run)
- [Project Structure](#project-structure)
- [API Endpoints](#API-endpoints)
- [Screenshots and Descriptions](#screenshots-and-descriptions)
- [Environment Variables](#environment-variables)
- [Author](#author)

## 🚀 Technologies Used

### Backend
- **Java 17+**
- **Spring Boot 3.2.0**

- Spring Web

- Spring Data JPA

- Spring Security
- Spring Validation
- **PostgreSQL** - Relational Database
- **JWT (jjwt 0.12.3)** - Authentication and Authorization
- **Lombok** - Boilerplate Reduction
- **BCrypt** - Password Hashing
- **Maven** - Dependency Management

### Frontend
- **React 18.2.0**
- **React Router DOM 6.20.1** - Navigation
- **Tailwind CSS 3.3.6** - Styling
- **Axios 1.6.2** - HTTP Requests
- **qrcode.react 3.1.0** - QR Code Generation
- **Context API** - State Management

## ✨ Main Features

1. **Secure Authentication**

- User Registration with Validation

- Login with JWT

- Route Protection

2. **Pet Management**

- Complete pet CRUD (only the owner can edit)

- Photo upload

- Complete fields: name, species, breed, age, color, weight

- Medical information: conditions, allergies, medications

- Veterinarian contact

- Personalized owner notes

3. **Unique QR Code**

- Automatic QR code generation for each pet

- Link to public page accessible to anyone

- Possibility of printing for collar

4. **"Lost Pet" Mode**

- Button to mark pet as lost

- Confirmation modal before activating/deactivating

- Public page with drastic visual transformation:

- Pulsating red borders (CSS animation)

- Large banner: "🚨 I'M LOST - PLEASE HELP ME 🚨"

- Highlighted and animated owner contact button

- Highlighted health information

5. **Responsive Public Page**

- Cute design with pastel colors

- Display of Photo, basic and health information

- Direct contact with the owner via telephone

- Special mode for lost pets

## 📦 Prerequisites

Before starting, make sure you have installed:

- **Java 17 or higher**

``bash
java -version

```

- **Maven 3.6+**

``bash
mvn -version
```

- **Node.js 16+ and npm**

``bash
node -v
npm -v
```

- **PostgreSQL 12+**

``bash
psql --version
```

## 🔧 Installation and Configuration

### 1. Clone the repository

```bash
git clone https://github.com/Valdemar-Andrade/PetSafe-QR-backend.git
cd petsafe_qr
````

### 2. Database Configuration Data Management

**Create the PostgreSQL database:**

```bash
# Access PostgreSQL
psql -U postgres

# Create the database
CREATE DATABASE petsafe_db;

# Exit psql
\q
```

**Configure the credentials in `backend/src/main/resources/application.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/petsafe_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Backend Configuration

```bash
cd backend

# Install dependencies
mvn clean install

# (Optional) Run the tests
mvn test
```

### 4. Frontend Configuration

```bash
cd frontend

# Install dependencies
npm install
```

**Configure the API URL in `frontend/.env`:**

```env REACT_APP_API_URL=http://localhost:8080

```

## ▶️ How to Run

### Running the Backend

**Option 1: Using Maven**
```bash
cd backend
mvn spring-boot:run
```

**Option 2: Using a compiled JAR**
```bash
cd backend
mvn clean package
java -jar target/petsafe-qr-1.0.0.jar
```

The backend will be running at: `http://localhost:8080`

### Running the Frontend

```bash
cd frontend
npm start
```

The frontend will be running at: `http://localhost:3000`

**Note:** The frontend will open automatically in the browser. ## 📁 Project Structure

```
petsafe_qr/
├── backend/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/petsafe/qr/
│ │ │ │ ├── config/ # Configurations (Security, Web)
│ │ │ │ ├── controller/ # REST Controllers
│ │ │ │ ├── dto/ # Data Transfer Objects
│ │ │ │ ├── entity/ # Entities JPA
│ │ │ │ ├── exception/ # Custom exceptions and handler
│ │ │ │ ├── repository/ # JPA repositories
│ │ │ │ ├── security/ # JWT, UserDetails, Filters
││ │ │ └── service/ # Business logic
│ │ │ └── resources/
│ │ │ └── application.properties
│ │ └── test/
│ ├── pom.xml
│ └── uploads/ # Uploads directory (created at runtime)
│
├── frontend/
│ ├── public/
│ │ └── index.html
│ ├── src/
│ │ ├── components/ # Components Reusable
│ │ │ ├── AlertModal.js
│ │ │ ├── LoadingSpinner.js
│ │ │ ├── Navbar.js
│ │ │ ├── PetCard.js
│ │ │ └── ProtectedRoute.js
│ │ ├── context/ # Context API
│ │ │ └── AuthContext.js
│ │ ├── pages/ # Main pages
│ │ │ ├── AddPet.js
│ │ │ ├── Dashboard.js
│ │ │ ├── EditPet.js
│ │ │ ├── Login.js
│ │ │ ├── PetDetails.js
│ │ │ ├── PublicPetPage.js
│ │ │ └── Register.js
│ │ ├── services/ # API Services
│ │ │ ├── api.js
│ │ │ ├── authService.js
│ │ │ └── petService.js
│ │ ├── App.js
│ │ ├── index.js
│ │ └── index.css
│ ├── .env
│ ├── package.json
│ ├── tailwind.config.js
│ └── postcss.config.js
│
└── README.md

```

## 🔌 API Endpoints

### Authentication

| Method | Endpoint | Description | Authentication |

|--------|----------|-----------|--------------|

| POST | `/api/auth/register` | User registration | No |

| POST | `/api/auth/login` | User login | No |

**Example of Request - Register:**
```json
{
"name": "João Silva",
"email": "joao@email.com",
"password": "password123",
"phone": "(11) 99999-9999"
}
```

**Example of Response:**
```json
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",

"type": "Bearer",

"userId": "123e4567-e89b-12d3-a456-426614174000",
"name": "João Silva",

"email": "joao@email.com"
}
```

### Pets (Protected - Requires JWT)

| Method | Endpoint | Description | Authentication |

|--------|----------|-----------|--------------|

| POST | `/api/pets` | Create new pet | Yes |

| GET | `/api/pets` | List user's pets | Yes |

| GET | `/api/pets/{id}` | Pet details | Yes (owner only) |

| PUT | `/api/pets/{id}` | Update pet | Yes (owner only) |

| DELETE | `/api/pets/{id}` | Delete pet | Yes (owner only) |

| PATCH | `/api/pets/{id}/missing` | Missing Toggle status | Yes (owner only) |

| POST | `/api/pets/{id}/photo` | Upload photo | Yes (owner only) |

**Example Request - Create Pet:**
```json
{
"name": "Thor",
"species": "Dog",
"mermaid": "Golden Retriever",
"age": 3,
"color": "Golden",
"weight": 30.5,
"medicalInfo": "Healthy, vaccinated",
"allergies": "None",
"medications": "None",
"vetContact": "Bichos & Cia Clinic - (11) 3333-3333",

"ownerNotes": "Very docile and playful"

}
```

### Public (Without Authentication)

| Method | Endpoint | Description | Authentication |

|--------|----------|-----------|--------------|

| GET | `/api/public/pet/{uuid}` | Pet's public page | No |

## 🖼️ Screenshots and Screen Descriptions

### 1. Login / Registration
- Modern design with colorful gradients
- Forms with validation
- Error feedback

### 2. Dashboard
- List of user's pet cards
- "Add Pet" button
- Visual badge for lost pets
- Responsive grid design

### 3. Add/Edit Pet
- Complete form divided into sections:

- Basic Information (name, species, breed, age, color, weight)

- Health Information (medical, allergies, medications, veterinarian)

- Observations (owner's notes)
- Frontend and backend validations

### 4. Pet Details
- **Left Column:**

- Pet photo uploaded

- QR Code generated with qrcode.react

- Link to public page
- **Right Column:**

- Complete information
- Action buttons: Edit, Mark as Lost, Delete

- Confirmation Modals

### 5. Public Page (/p/{uuid})
- **Normal Mode:**

- Cute design with pastel gradients

- Pet photo

- Basic and health information

- Owner contact highlighted

- **Lost Pet Mode:**

- Pulsating red borders (CSS animation)

- Large red banner: "🚨 I'M LOST - PLEASE HELP ME 🚨"

- Giant animated contact button

- Light red background

- Health information with special emphasis

## 🔐 Environment Variables

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

**⚠️ Important:** Change `app.jwt.secret` to a production-safe secret value.

### Frontend (`.env`)

```env
REACT_APP_API_URL=http://localhost:8080
```

## 🎨 Special Visual Resources

### CSS Animations

**Pulsating Borders (Lost Pet):**
```css
@keyframes pulseBorder {

0%, 100% {borderColor: #ef4444; 
boxShadow: 0 0 0 0 rgba(239, 68, 68, 0.7); 
}
50% {
borderColor: #dc2626; 
boxShadow: 0 0 0 10px rgba(239, 68, 68, 0); 
}

```

Applied via Tailwind: `animate-pulse-border`

## 🧪 Tests

### Backend
```bash
cd backend
test mvn
```

### Frontend
```bash
cd frontend
test npm
```

## 🚀 Deployment

### Backend

**Generate JAR:**
```bash
cd backend
package mvn clean
```

The file will be generated in `target/petsafe-qr-1.0.0.jar`

**Run in production:**
```bash
java -jar target/petsafe-qr-1.0.0.jar --spring.profiles.active=prod
```

### Frontend

**Production Build:**
```bash
cd frontend
npm run build

Static files will be generated in `build/`

## 📝 Implemented Best Practices

- ✅ **SOLID Principles**
- ✅ **Clean Code**
- ✅ **DTOs for data transfer**
- ✅ **Global Exception Handler**
- ✅ **Validations with Bean Validation**
- ✅ **Security with Spring Security and JWT**
- ✅ **Bcrypt for passwords**
- ✅ **Interceptors in Axios**
- ✅ **Context API for global state**
- ✅ **Reusable components**
- ✅ **Responsive design (mobile-first)**
- ✅ **Separation of responsibilities**

## Related Project

- [PetSafe-QR-front](https://github.com/Valdemar-Andrade/PetSafe-QR-front) - PetSafe-QR-frontend

## 👤 Author
- GitHub: [@Valdemar-Andrade]
- LinkedIn: [Valdemar Valdemar](https://www.linkedin.com/in/valdemar-andrade-8b0b45189)

Developed as a portfolio project demonstrating skills in:
- **Backend:** Java, Spring Boot, Spring Security, JWT, JPA/Hibernate, PostgreSQL
- **Frontend:** React, Tailwind CSS, Context API, React Router
- **Architecture:** REST API, JWT Authentication, File Upload
- **Best Practices:** SOLID, Clean Code, Exception Handling, Validations

---

**PetSafe QR** - Keeping your pets safe with technology! 🐾❤️
