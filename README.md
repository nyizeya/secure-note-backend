# Secure Note Backend

This project is a **Spring Boot backend application** built while following a **Udemy course** to enhance my skills in **Spring Security** and modern backend security practices.

The main goal of this project is to **practice and understand authentication, authorization, and OAuth2-based security** in a real-world–style application.

---

## 🚀 Features

* Spring Boot REST API
* Spring Security core concepts
* Authentication & Authorization
* Role-based access control (RBAC)
* OAuth2 integration with github and google
* Secure endpoint protection
* PostgreSQL database integration
* Docker & Docker Compose support

---

## 🛠️ Tech Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **OAuth2**
* **PostgreSQL**
* **Maven**
* **Docker & Docker Compose**

---

## 📁 Project Structure

```
secure-note-backend/
├── docker-compose.yml   # Docker services configuration
├── Dockerfile           # Application Docker image
├── pom.xml              # Maven dependencies
├── postgres/            # PostgreSQL configuration/data
├── logs/                # Application logs
├── src/                 # Application source code
├── target/              # Build output
```

---

## 🔐 Security Concepts Practiced

This project focuses heavily on **backend security**, including:

* Authentication flow
* Role-based authorization
* Securing REST APIs
* OAuth2 login and token-based security
* Protecting endpoints based on user roles
* Secure password handling

---

## 🐳 Running with Docker

To run the application using Docker:

```bash
docker-compose up --build
```

This will start:

* The Spring Boot application
* PostgreSQL database

---

## ▶️ Running Locally

Prerequisites:

* Java 17+ (or compatible version used in the project)
* Maven
* PostgreSQL

Run the application:

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🎯 Purpose of This Project

* Strengthen understanding of **Spring Security**
* Practice **OAuth2 authentication flows**
* Learn how to secure APIs properly
* Apply theoretical concepts in a hands-on project

This project is **for learning and skill enhancement purposes**.

---

## 📌 Notes

* This is a course-based project adapted and implemented by me
* Focus is on **security concepts**, not UI
* Suitable as a **reference project** for Spring Security learners

---

## 👤 Author

**Nyi Zeya**

Java / Spring Boot Developer

---

## 📄 License

This project is for educational purposes.
