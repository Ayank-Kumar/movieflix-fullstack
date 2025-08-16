# 🎬 MovieFlix Full-Stack Application

MovieFlix is a full-stack web application for discovering, searching, and exploring movies. It provides detailed movie information, user authentication, and an interactive analytics dashboard.

---

## ✨ Features

* **Search & Filtering**: Find movies by title, genre, year, and rating with advanced sorting and pagination.
* **Detailed Movie Pages**: Access comprehensive details including cast, crew, plot, ratings, budget, revenue, runtime, release date, language, and images.
* **Interactive Dashboard**: Visualize movie data with dynamic charts for genre distribution, ratings by genre, runtime trends, and top-rated movies.
* **Secure Authentication**: JWT-based authentication with role-based access control (admin vs. user) and protected routes.
* **Admin Capabilities**: Manage the application with features like cache management and database statistics.
* **Responsive Design**: A mobile-first, responsive UI built with React, complete with loading and error states for a smooth user experience.
* **External API Integration**: Leverages the TMDB API for up-to-date movie data.

---

## 🛠️ Prerequisites

Before you begin, ensure you have the following installed:

* **Java**: Version 17 or higher
* **Maven**: For building the backend
* **Node.js**: Version 16 or higher
* **npm**: For managing frontend packages
* **MongoDB**: A running instance (local or on Atlas)
* **TMDB API Key**: A free API key from [The Movie Database (TMDB)](https://www.themoviedb.org/signup).

---

## 🚀 Setup: Local Environment

Follow these steps to get your development environment running.

### 1. Clone the Repository

```bash
git clone https://github.com/Ayank-Kumar/movieflix-fullstack.git
cd movieflix-fullstack
```

## 2) Backend Configuration

Edit `backend/src/main/resources/application.yml` with your environment values:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/movieflix

movieflix:
  tmdb:
    api-key: YOUR_TMDB_API_KEY
    base-url: https://api.themoviedb.org/3

jwt:
  secret: YOUR_JWT_SECRET
  expiration-ms: 3600000
```

### Build and run the backend

```bash
cd backend
./mvnw clean package
java -jar target/movieflix-backend.jar
```

### The backend runs at

```bash
http://localhost:8080/api
```

## 3) Frontend Configuration

### Create your environment file from the example:
```bash
cd ../frontend
cp .env.example .env
```

### Edit `frontend/.env`
```text
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_TMDB_IMAGE_BASE_URL=https://image.tmdb.org/t/p/w500
```
### Install dependencies and start the development server
```bash
npm install
npm start
```

###  The frontend runs at:

```bash
http://localhost:3000
```
