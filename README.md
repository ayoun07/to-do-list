# 📝 Todolist App

Todolist est une application web fullstack qui permet aux utilisateurs de gérer leurs tâches au quotidien avec un système de listes personnalisées, d’authentification sécurisée et d’une interface moderne.

## 🚀 Technologies utilisées

### 🧠 Backend (Java Spring Boot)
- Spring Boot 3+
- Spring Security + JWT
- Spring Data JPA (MySQL)
- Java Mail Sender (reset mot de passe)
- JUnit (tests)
- Maven

### 🎨 Frontend (React)
- React + TypeScript
- Axios (requêtes API)
- React Router
- CSS modules / custom styles
- Validation dynamique (mot de passe, email)

## 🔐 Fonctionnalités

### Authentification :
- Inscription avec validation dynamique de mot de passe
- Connexion sécurisée avec JWT
- Réinitialisation du mot de passe par email

### Gestion de tâches :
- Création de tâches
- Suppression et modification
- Complétion des tâches
- Création de listes
- Renommage et suppression de listes

### Bonus :
- Avatars dynamiques selon le prénom
- Interface responsive
- Sécurité des variables via fichiers `.env`

## 📁 Structure du projet

backend/
├── src/
│ └── main/java/com/todo
│ ├── controller/
│ ├── model/
│ ├── repository/
│ ├── service/
│ └── security/
├── resources/
│ ├── application.properties
│ └── static/
├── .env
└── pom.xml

frontend/
├── src/
│ ├── components/
│ ├── pages/
│ ├── api.ts
│ └── App.tsx
├── .env
└── package.json

## ⚙️ Installation

### 1. Cloner le projet

```bash
git clone https://github.com/ton-utilisateur/todolist.git
cd todolist

cd backend
cp .env.example .env # Crée ton fichier de config
./mvnw spring-boot:run

cd frontend
npm install
npm run dev

# Backend
./mvnw test

# Frontend (si setup)
npm test
