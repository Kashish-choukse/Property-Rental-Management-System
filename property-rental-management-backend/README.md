# Property Rental Management System - Backend

## Technology
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA / Hibernate
- Oracle Database
- Maven

## Current module
Property CRUD + search/filter.

## Database setup
1. Start Oracle Database.
2. Open SQL Developer.
3. Create/use an Oracle user.
4. Update `src/main/resources/application.properties`:
   - username
   - password
   - database URL if your Oracle service is different.
5. Run the Spring Boot application.

Hibernate will create/update the `PROPERTIES` table automatically because `ddl-auto=update`.

## Run
From the project root:

mvn spring-boot:run

Or run `PropertyRentalManagementApplication.java` from Eclipse.

## Test with Postman

### 1. Add property
POST http://localhost:8080/api/properties

Body -> raw -> JSON:
{
  "name": "2 BHK Apartment",
  "location": "Pune",
  "propertyType": "Apartment",
  "bedrooms": 2,
  "bathrooms": 2,
  "area": 950,
  "rent": 18000,
  "status": "AVAILABLE",
  "description": "Spacious apartment near IT park"
}

### 2. Get all
GET http://localhost:8080/api/properties

### 3. Get by ID
GET http://localhost:8080/api/properties/1

### 4. Update
PUT http://localhost:8080/api/properties/1

### 5. Delete
DELETE http://localhost:8080/api/properties/1

### 6. Search by location
GET http://localhost:8080/api/properties/search?location=Pune

### 7. Filter by status
GET http://localhost:8080/api/properties/filter/status?status=AVAILABLE

### 8. Filter by type
GET http://localhost:8080/api/properties/filter/type?type=Apartment

### 9. Filter by maximum rent
GET http://localhost:8080/api/properties/filter/rent?maxRent=20000

## Architecture

Frontend (HTML/CSS/JS)
        |
        | HTTP / JSON
        v
Spring Boot REST API
        |
        v
Service Layer
        |
        v
Spring Data JPA / Hibernate
        |
        v
Oracle Database

## Next modules to add
- Owner
- Tenant
- Rental Agreement
- Rent Payment
- Login / Registration
- Admin dashboard
