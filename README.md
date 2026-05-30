# Movie Ticket Booking System

A microservices-based movie ticket booking system built with Spring Boot,
Spring Cloud, Eureka, and Razorpay payment integration.

## Services
| Service | Description | Port |
|---|---|---|
| movie-services-registry | Eureka service discovery | 8761 |
| api-gateway | Spring Cloud Gateway | 8765 |
| booking-service | Handles movie bookings | 8080 |
| payment-service | Handles payments via Razorpay | 9090 |

## Prerequisites
- Java 17
- Maven
- Docker Desktop

## Setup & Run

### 1. Add your Razorpay credentials
update the payment-service environment variables:
```yaml
payment-service:
  environment:
    RAZORPAY_KEY_ID: your_razorpay_key_id_here
    RAZORPAY_KEY_SECRET: your_razorpay_key_secret_here
```

For running locally (without Docker), update `payment-service/src/main/resources/application.yaml`:
```yaml
razorpay:
  key-id: your_razorpay_key_id_here
  key-secret: your_razorpay_key_secret_here
```

### 2. Build and run with Docker
```bash
mvn clean package -DskipTests
docker compose up --build
```

### 3. Run locally (without Docker)
Start each service individually from your IDE or:
```bash
mvn spring-boot:run
```

## API Usage

### Create a Booking
**POST** `http://localhost:8765/bookings`

Set `Content-Type: application/json` in headers and send this as the request body:

Example:
```json
{
    "userId": "movieviewer2323",
    "movieId": 3333,
    "seatsSelected": ["B8", "B9"],
    "showDate": "2026-05-28",
    "showTime": "20:15",
    "bookingAmount": 505
}
```
A successful response will return a payment link — open it in your browser to complete the payment via Razorpay.

## Accessing the Application
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8765
- Bookings Test API: http://localhost:8765/bookings/test
- Bookings API: http://localhost:8765/bookings
