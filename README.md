# Bookings App

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen)

> Web application for booking class sessions by date, built with Spring Boot, JPA/Hibernate and Thymeleaf.

A small reservations app: a visitor picks a date, sees the sessions scheduled that day with their
teacher, timetable and remaining places, and books one by logging in with an existing account. Data
lives in MySQL and is mapped with JPA/Hibernate; the same service layer is also exposed through a
small REST controller.

## Stack

- Java 17
- Spring Boot 3.4.5 (Web, Data JPA, Thymeleaf, DevTools)
- Hibernate / JPA with MySQL 8 (`mysql-connector-j`)
- Lombok, Jakarta Bean Validation API
- Maven (Maven Wrapper 3.3.2 / Maven 3.9.9)

## What it does

- Search sessions by date from a Thymeleaf form (`GET /`, `POST /buscar`) and list name, teacher,
  start and end time and available places for that day.
- Book a session from the results list: `GET /login?id={sessionId}` shows the login form and
  `POST /login` checks the e-mail and password against the `users` table, books the session on
  success and shows a confirmation page, or re-renders the form with an error.
- Booking a session links the user to the session through the `bookings` join table
  (`Session` ↔ `AppUser`, many-to-many) and decrements its number of places; it refuses the booking
  when no places are left.
- REST endpoints over the same service:
  - `GET /api/sesiones/disponibles?fecha=YYYY-MM-DD` — sessions on that date as JSON.
  - `POST /api/sesiones/reservar/{id}?email=...` — books a session for that user.
- Entities `Session` (name, teacher, date, start/end time, places, booked users) and `AppUser`
  (name, unique e-mail, password, role) are created automatically by Hibernate
  (`spring.jpa.hibernate.ddl-auto=update`).

## Getting started

Prerequisites: JDK 17 or newer and a running MySQL 8 server.

1. Create the database (Hibernate creates the tables on startup):

   ```sql
   CREATE DATABASE bookings_app CHARACTER SET utf8mb4;
   ```

2. Provide the database credentials as environment variables. They are read by
   `src/main/resources/application.properties`, which defaults to user `root` and an empty password
   if they are not set:

   | Variable      | Meaning                | Default |
   | ------------- | ---------------------- | ------- |
   | `DB_USERNAME` | MySQL user             | `root`  |
   | `DB_PASSWORD` | Password for that user | *empty* |

   ```bash
   export DB_USERNAME=myuser
   export DB_PASSWORD=mypassword
   ```

3. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows use `mvnw.cmd spring-boot:run`.

4. Open <http://localhost:8080>.

There is no sign-up screen and no seed data, so insert at least one row in `users` and a few in
`sessions` (for example with a SQL client) before trying to book.

## Project structure

```
src/main/java/com/reservas/app/
├── AppReservasApplication.java   Spring Boot entry point
├── controller/                   WebController (Thymeleaf), SessionController (REST)
├── dto/                          SesionDTO
├── entity/                       Session, AppUser
├── repository/                   Spring Data JPA repositories
└── service/                      SessionService, UserService
src/main/resources/
├── application.properties        Datasource, JPA and server configuration
└── templates/                    buscar, resultado, login, exito
src/test/java/                    Spring Boot context test
```

## Status

Working prototype and not actively developed. Spring Security is present in the `pom.xml` only as a
commented-out dependency, so the login is a plain password comparison against the database and there
is no session handling, registration or password hashing yet.

## License

Released under the MIT License — see [LICENSE](LICENSE).
