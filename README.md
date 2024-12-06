# Role-Based Access Control using Spring Boot

## Project Overview

The Role-Based Access Control (RBAC) system is a Spring Boot application designed to handle user registration, authentication, and management. It provides a RESTful API that allows users to create accounts, log in, and manage their profiles. The system also supports role-based access control with different user roles such as USER, ADMIN, and MODERATOR.

### Key Features

- **User Registration**: Allows new users to create accounts with validation on input data.
- **User Authentication**: Supports login functionality with JWT (JSON Web Tokens) for secure session management.
- **Role Management**: Implements role-based access control to manage permissions for different user types.
- **Token Management**: Handles JWT token creation, validation, and revocation during user logout.

## Technology Stack

This project utilizes the following technologies:

- **Java 17**: The programming language used for developing the application.
- **Spring Boot**: A framework for building stand-alone, production-grade Spring-based applications.
- **Spring Security**: Provides authentication and authorization features.
- **JPA (Java Persistence API)**: For database interaction and ORM (Object Relational Mapping).
- **H2 Database**: An in-memory database for development and testing purposes.
- **JUnit 5**: For unit testing the application components.
- **Mockito**: For mocking dependencies in unit tests.
- **Lombok**: To reduce boilerplate code through annotations.

## Prerequisites

Before running the project, ensure you have the following installed:

1. **Java Development Kit (JDK) 17**
   - Download from [Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or [AdoptOpenJDK](https://adoptopenjdk.net/).
   
2. **Maven**
   - Download from [Maven's official website](https://maven.apache.org/download.cgi).

3. **IDE (Integrated Development Environment)**
   - Recommended: IntelliJ IDEA or Eclipse.
  
## Using Replit (Testing endpoints without cloning the repository)

### Import the Project into Replit

1. **Create a New Replit Project:**
   - Go to [Replit](https://replit.com/) and create a new project.
   - Choose the "Java" template.

2. **Import the Repository:**
   - Click on the "Add Files" button in the Replit sidebar.
   - Select "Import from Git".
   - Enter the following repository URL: `https://github.com/deedeecee/Role-Based-Access-Control.git`
   - Click "Import".

3. **Install Dependencies:**
   - Ideally, Replit recognoses the required dependencies and will automatically prompt to download them. If that does not happen, follow the next steps.
   - Open the terminal in Replit.
   - Run the following command to build the project and install dependencies:
     ```bash
     mvn clean install
     ```

5. **Run the Application:**
   - In the terminal, run the following command to start the application:
     ```bash
     mvn spring-boot:run
     ```

### API Endpoints

Once the application is running, you can test the API endpoints using your browser or a REST client like Postman. You will see a window similar to the following:
![image](https://github.com/user-attachments/assets/c8958310-ce1a-40fe-9d4b-599201739b50)

- Make sure you copy the Dev URL and set the ports to _:8080 → :8080_

#### User Registration
- **POST <dev_url_provided_by_replit:8080>/api/v1/public/register**

#### User Login
- **POST <dev_url_provided_by_replit:8080>/api/v1/public/login**

#### User Logout
- **POST <dev_url_provided_by_replit:8080>/api/v1/public/logout**

#### Other Endpoints
- Refer to the "API Endpoints" section in the README.md for a complete list of endpoints and their details.

## Getting Started

### Clone the Repository

Clone this repository to your local machine using the following command:
```bash
git clone https://github.com/deedeecee/Role-Based-Access-Control.git
cd Role-Based-Access-Control
```

### Build the Project

Navigate to the project directory and build the project using Maven:
```bash
mvn clean install
```

### Run the Application

You can run the application using the following command:
```bash
mvn spring-boot:run
```

Alternatively, you can run it directly from your IDE by executing the main method in the RoleBasedAccessControlApplication class.

### Database Setup

The application uses an H2 in-memory database by default. You can access the H2 console by searching the following URL in your browser:
```text
http://localhost:8080/api/v1/public/h2-console
```

Use the following credentials to log in:
- JDBC URL: jdbc:h2:mem:mydb
- User Name: sa
- Password: password

## API Endpoints

The following are the main **public** API endpoints available in this application:

### User Registration
- **POST http://localhost:8080/api/v1/public/register**
  - Request Body:
  ```json
  {
    "username": "Jack",
    "email": "jack@mail.com",
    "password": "jackjack123",
    "roles": [
        "MODERATOR",
        "ADMIN"
    ]
  }
  ```

  If you omit writing "roles", then the default role `USER` will be accepted.

  - Response:
    - Status Code: `201 Created`
    - Body:
    ```json
    {
      "id": 1,
      "username": "Jack",
      "email": "jack@mail.com",
      "roles": [
          "MODERATOR",
          "ADMIN"
      ]
    }
    ```

### User Login
- **POST http://localhost:8080/api/v1/public/login**
  - Request Parameters:
    - `email`: Registered user's email address
    - `password`: Registered user's password
  - Response:
    - Status Code: `200 OK`
    - Body:
    ```text
    Authentication successful for email: <Email_Address>
    JWT Token: <Generated_JWT_Token>
    ```

### User Logout
- **POST http://localhost:8080/api/v1/public/logout**
  - Headers:
    - `Authorization`: Bearer token received during login
  - Response:
    - Status Code: `200 OK`

The following are the main API endpoints accessible to authorized users with one or more of the following roles:
1. **USER**
2. **MODERATOR**
3. **ADMIN**

These endpoints are currently placeholders and can be extended to implement actual business logic in the future.
- **GET http://localhost:8080/api/v1/user**
  - Response:
    - Status Code: `200 OK`
    ```text
    GET:: user controller
    ```
- **POST http://localhost:8080/api/v1/user**
  - Response:
    - Status Code: `200 OK`
    ```text
    POST:: user controller
    ```
- **PUT http://localhost:8080/api/v1/user**
  - Response:
    - Status Code: `200 OK`
    ```text
    PUT:: user controller
    ```
- **DELETE http://localhost:8080/api/v1/user**
  - Response:
    - Status Code: `200 OK`
    ```text
    DELETE:: user controller
    ```
> **_NOTE:_**  The above endpoints can be accessed by authorized _users_, _moderators_ and _admins_.

The following are the main API endpoints accessible to authorized users with one or more of the following roles:
1. **MODERATOR**
2. **ADMIN**

These endpoints are currently placeholders and can be extended to implement actual business logic in the future.
- **GET http://localhost:8080/api/v1/moderator**
  - Response:
    - Status Code: `200 OK`
    ```text
    GET:: moderator controller
    ```
- **POST http://localhost:8080/api/v1/moderator**
  - Response:
    - Status Code: `200 OK`
    ```text
    POST:: moderator controller
    ```
- **PUT http://localhost:8080/api/v1/moderator**
  - Response:
    - Status Code: `200 OK`
    ```text
    PUT:: moderator controller
    ```
- **DELETE http://localhost:8080/api/v1/moderator**
  - Response:
    - Status Code: `200 OK`
    ```text
    DELETE:: moderator controller
    ```
> **_NOTE:_**  The above endpoints can be accessed by authorized _moderators_ and _admins_ only.

The following are the main API endpoints accessible to authorized users with **ADMIN** role:

These endpoints are currently placeholders and can be extended to implement actual business logic in the future.
- **GET http://localhost:8080/api/v1/admin**
  - Response:
    - Status Code: `200 OK`
    ```text
    GET:: admin controller
    ```
- **POST http://localhost:8080/api/v1/admin**
  - Response:
    - Status Code: `200 OK`
    ```text
    POST:: admin controller
    ```
- **PUT http://localhost:8080/api/v1/admin**
  - Response:
    - Status Code: `200 OK`
    ```text
    PUT:: admin controller
    ```
- **DELETE http://localhost:8080/api/v1/admin**
  - Response:
    - Status Code: `200 OK`
    ```text
    DELETE:: admin controller
    ```
> **_NOTE:_**  The above endpoints can be accessed by authorized _admins_ only.

### Testing the Application

This project includes unit tests for service classes using JUnit and Mockito. To run all tests, execute:

```bash
mvn test
```
