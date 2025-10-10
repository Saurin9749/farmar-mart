Spring Fault Tolerance - Clean Multi-module Demo (Simple)

Java: 17
Spring Boot: 3.3.2
Spring Cloud: 2023.0.3 (Leyton)
Resilience4j: 2.0.x

Modules:
- common
- eureka-server (port 8761)
- api-gateway (port 8080)
- inventory-service (port 8092)
- order-service (port 8091)

Run (per module):
1. mvn -T 1C clean install
2. In separate terminals run:
   cd eureka-server && mvn spring-boot:run
   cd inventory-service && mvn spring-boot:run
   cd order-service && mvn spring-boot:run
   (optional) cd api-gateway && mvn spring-boot:run

Demo endpoints:
- GET http://localhost:8091/orders/order/1
- GET http://localhost:8091/orders/order/slow
- GET http://localhost:8091/orders/order/down
- Async: GET http://localhost:8091/orders/order/async/slow

Notes:
- This is a simple, presentation-ready demo without Docker.
- If you want Docker Compose later, I can add it.
