# 🖼️ ImageProject – Backend

A robust **Spring Boot backend** for managing user authentication, profile handling, and image upload/viewing features.  
Designed for scalability, clarity, and performance, the project follows clean architecture principles and RESTful best practices.

---

## 🚀 Features

- **User Management**
  - Secure registration and login
  - Profile editing and retrieval

- **Image Handling**
  - Upload images with metadata
  - Retrieve and display uploaded images
  - Organized storage and efficient retrieval logic

- **Logging**
  - Integrated with Log4j2 for structured logging
  - Tracks requests, authentication events, and exceptions

- **Error Handling**
  - Centralized exception management
  - Descriptive HTTP responses

---

## 🧱 Tech Stack

| Layer                  | Technology                          |
|------------------------|-------------------------------------|
| **Backend Framework**  | Spring Boot                         |
| **Language**           | Java                                |
| **Database**           | MySQL / PostgreSQL (configurable)   |
| **Authentication**     | Spring Security + JWT               |
| **Logging**            | Log4j2                              |
| **Build Tool**         | Maven                               |
| **IDE Recommendation** | Spring Tool Suite (STS) / IntelliJ IDEA |

---

## 📁 Project Structure

```
ImageProject/
│
├── src/
│ ├── main/
│ │ ├── java/com/learn/ImageProject/
│ │ │ ├── controller/ # REST Controllers
│ │ │ ├── service/ # Business logic
│ │ │ ├── model/ # Entity classes
│ │ │ ├── repository/ # Data access layer
│ │ │ ├── config/ # Security & App Configurations
│ │ │ └── ImageProjectApplication.java
│ │ └── resources/
│ │ ├── application.properties
│ │ └── log4j2.xml
│ └── test/ # Unit and integration tests
│
├── pom.xml
└── README.md
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/<your-username>/ImageProject.git
cd ImageProject
```

### 2. Configure Database
Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/imageproject
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### 3. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The server will start at: http://localhost:8080

---

## 🧠 API Overview

| Endpoint      | Method | Description                        |
|---------------|--------|------------------------------------|
| /register     | POST   | Register a new user                |
| /signin       | POST   | Authenticate user and return JWT   |
| /profile      | GET    | Fetch user profile                 |
| /editProfile  | PUT    | Edit user details                  |
| /upload       | POST   | Upload image                       |
| /images       | GET    | Fetch all uploaded images          |

---

## 🧾 Example Request (Image Upload)

```bash
POST /upload
Content-Type: multipart/form-data
Authorization: Bearer <JWT_TOKEN>

Body:
  image: <file>
```

**Response**

```json
{
  "message": "Image uploaded successfully",
  "fileName": "nature_photo.jpg",
  "url": "/uploads/nature_photo.jpg"
}
```

---

## 🛠️ Logging

Log4j2 captures structured logs in `/logs/`:

```
[INFO ] 2025-10-27 20:45:33 - User 'john_doe' uploaded image 'sunset.jpg'
[ERROR] 2025-10-27 21:00:12 - Unauthorized access attempt detected
```

You can adjust logging levels via `log4j2.xml`.
