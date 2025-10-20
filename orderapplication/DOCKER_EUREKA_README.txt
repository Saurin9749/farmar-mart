This project has been augmented to include:
- eureka-server (port 8761)
- api-gateway (port 8080) routing via Eureka (discovery client)
- order-service (container port 8080, mapped to host 8081)
- inventory-service (container port 8080, mapped to host 8082)
- kafka and zookeeper containers for messaging

How to run:
1. From the project root (where docker-compose.yml is located):
   docker compose up --build

Notes:
- Services are configured to register with Eureka at http://eureka-server:8761/eureka
- The API Gateway uses discovery locator enabled to route by service name.