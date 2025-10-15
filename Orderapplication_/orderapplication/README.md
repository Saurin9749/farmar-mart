# Kafka Microservices Example (multi-module)
Java 17 + Spring Boot 3.3.4

Modules:
- order-service (port 8081) - produces messages to Kafka topic `order-topic`
- inventory-service (port 8082) - consumes from `order-topic` and updates in-memory inventory

How to run:
1. Start Kafka & Zookeeper (e.g., docker-compose or local install)
2. From project root run: `mvn -pl order-service spring-boot:run` and `mvn -pl inventory-service spring-boot:run`
3. POST an order:
   POST http://localhost:8081/orders
   Body JSON: {"orderId":"O1","product":"Laptop","quantity":2}
4. Check inventory via GET http://localhost:8082/inventory
