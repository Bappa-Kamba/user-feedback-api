# 📝 User Feedback API

This is a simple Spring Boot application that provides a **User Feedback** feature. It allows users to submit feedback (message and rating), and allows admins to view submitted feedback.

---

## 🚀 Features

- Submit feedback with a message and rating.
- Admin-only endpoint to view all submitted feedback.
- Input validation for rating and message.
- JSON-based API responses.
- In-memory storage (for simplicity).
- Unit tests using JUnit and MockMvc.

---

## 📦 Endpoints

### ➕ `POST /api/feedback`

Submit user feedback.

#### ✅ Request Body

```json
{
  "userId": "user123",
  "message": "Love the app!",
  "rating": 5
}
```
##### ✅ Successful response:
```json
{
  "status": "success",
  "data": {
    "userId": "user123",
    "message": "Love the app!",
    "rating": 5,
    "timestamp": "2025-05-21T16:42:00"
  }
}
```
##### ❌ Erroneous response(e.g. invalid rating):
```json
{
  "status": "error",
  "data": "Rating must be between 1 and 5"
}
```

### 🔒 `GET /api/admin/feedback`
Get all submitted feedback (admin-only).

#### ✅ Request Headers
```json
{
  "x-admin": true
}
```

##### ✅ Successful Response
```json
{
  "status": "success",
  "data": [
    {
      "userId": "user123",
      "message": "Love the app!",
      "rating": 5,
      "timestamp": "2025-05-21T16:42:00"
    },
    {
      "userId": "user456",
      "message": "Could be faster",
      "rating": 3,
      "timestamp": "2025-05-21T17:00:00"
    }
  ]
}
```

##### ❌ Unauthorized Response
```json
{
  "status": "error",
  "data": "Access denied: admins only"
}
```

## 🛠️ How to Run Locally
1. Clone the repository:
```bash
  git clone https://github.com/yourusername/user-feedback-api.git
  cd user-feedback-api
```

2. Run the application using Maven:
```bash
  ./mvnw spring-boot:run
```

3. The API will be available at:

  `http://localhost:8080`

You can test the endpoints using Postman, curl, or any HTTP client.

### ✅ Running Tests

To execute unit tests:
```bash
./mvnw test
```

Tests cover:
- Valid and invalid feedback submission
- Admin access to feedback list
- Unauthorized access errors

### 🧱 Tech Stack
- Java 17+
- Spring Boot
- Jakarta Validation
- JUnit 5
- MockMvc
- Maven

## 👨‍💻 Author
- [Attahiru Kamba](https://github.com/Bappa-Kamba)
