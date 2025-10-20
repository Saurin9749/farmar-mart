# Order-Inventory Microservices (Feign + Eureka) - Ready to run

Modules:
- eureka-server (8761)
- api-gateway (9000)
- inventory-service (8081)
- order-service (8082)

Requirements:
- Java 17
- Maven
- Docker & Docker Compose (optional)

Build & run (Maven):
1. Start PostgreSQL (local) or use docker-compose which includes Postgres.
2. Start eureka-server:
   mvn -f eureka-server spring-boot:run
3. Start inventory-service:
   mvn -f inventory-service spring-boot:run
4. Start order-service:
   mvn -f order-service spring-boot:run
5. (Optional) start api-gateway:
   mvn -f api-gateway spring-boot:run

Run with Docker Compose:
  docker compose up --build

Test endpoints:
- Inventory GET:  http://localhost:8081/inventory
- Inventory POST: http://localhost:8081/inventory  (body: { "product":"Laptop","quantity":50 })
- Inventory reserve: POST http://localhost:8081/inventory/reserve  (body: { "product":"Laptop","quantity":2 })
- Place order: POST http://localhost:8082/orders  (body: { "orderId":"ORD-1001","product":"Laptop","quantity":2 })
- Through gateway: http://localhost:9000/order-service/orders  (discovery locator maps service ids)

