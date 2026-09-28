# FarmStock - Farmer's Produce Inventory Tracker

This project is built from the requirements in the supplied assessment sheet.

## Architecture

Frontend (HTML/CSS/JavaScript)
        |
        v
REST Controllers
        |
        v
Service Layer  <-- business rules / stock validation
        |
        v
Spring Data JPA Repositories
        |
        v
MySQL Database

## Main entities

- Crop
- HarvestBatch
- Sale

Relationships:
- One Crop -> many HarvestBatch records
- One Crop -> many Sale records

## Business rule

Current stock = total harvested quantity - total sold quantity.

A sale is rejected when the requested quantity is greater than current stock. The API returns HTTP 400 with a clear error message.
The crop row is locked while a sale is checked and saved, so simultaneous sales cannot oversell the stock.

## Running the tests

Tests use an in-memory H2 database, so MySQL is not required:

```bash
./mvnw test
```

## API endpoints

### Crops
- POST `/api/crops`
- GET `/api/crops`

Example:
```json
{
  "name": "Tomato",
  "unit": "kg"
}
```

### Harvests
- POST `/api/harvests`

Example:
```json
{
  "cropId": 1,
  "quantity": 100,
  "harvestDate": "2026-09-28",
  "notes": "Morning harvest"
}
```

### Sales
- POST `/api/sales`

Example:
```json
{
  "cropId": 1,
  "quantity": 20,
  "pricePerUnit": 40,
  "saleDate": "2026-09-28",
  "buyer": "Local Market"
}
```

### Stock
- GET `/api/stock`
- GET `/api/stock/{cropId}`

### Revenue
- GET `/api/reports/revenue?startDate=2026-09-01&endDate=2026-09-30`

## MySQL setup

The `farmstock` database is created automatically on first run (`createDatabaseIfNotExist=true`).
If you prefer to create it yourself:

```sql
CREATE DATABASE farmstock;
```

Default configuration expects:
- username: `root`
- password: `root`

If your MySQL password is different, set it in `application.properties` or use environment variables:

```text
DB_USERNAME=root
DB_PASSWORD=your_password
```

## Run

From the project folder:

```bash
mvnw.cmd spring-boot:run
```

or on Linux/macOS:

```bash
./mvnw spring-boot:run
```

Then open:

`http://localhost:8080/`

The HTML frontend is served directly by Spring Boot.

## Suggested Postman testing order

1. POST `/api/crops`
2. POST `/api/harvests`
3. GET `/api/stock`
4. POST `/api/sales` with a quantity within stock
5. POST `/api/sales` with a quantity greater than stock to verify rejection
6. GET `/api/reports/revenue?...`

