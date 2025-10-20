#!/bin/bash
mvn -q -DskipTests package
echo "Built all modules. Now run with docker compose or run each module via mvn -f <module> spring-boot:run"
