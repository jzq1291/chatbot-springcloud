#!/bin/bash

echo "Starting Chatbot Spring Cloud Microservices..."

echo ""
echo "Starting infrastructure services..."
docker-compose up -d

echo ""
echo "Waiting for infrastructure services to start..."
sleep 10

echo ""
echo "Starting microservices..."

echo "Starting gateway-service..."
gnome-terminal --title="Gateway Service" -- bash -c "cd gateway-service && mvn spring-boot:run; exec bash" &

echo "Starting auth-service..."
gnome-terminal --title="Auth Service" -- bash -c "cd auth-service && mvn spring-boot:run; exec bash" &

echo "Starting user-service..."
gnome-terminal --title="User Service" -- bash -c "cd user-service && mvn spring-boot:run; exec bash" &

echo "Starting chat-service..."
gnome-terminal --title="Chat Service" -- bash -c "cd chat-service && mvn spring-boot:run; exec bash" &

echo "Starting knowledge-service..."
gnome-terminal --title="Knowledge Service" -- bash -c "cd knowledge-service && mvn spring-boot:run; exec bash" &

echo "Starting chatbot-backend (legacy)..."
gnome-terminal --title="Chatbot Backend" -- bash -c "cd chatbot-backend && mvn spring-boot:run; exec bash" &

echo ""
echo "All services are starting..."
echo ""
echo "Service URLs:"
echo "- Gateway: http://localhost:8080"
echo "- Auth Service: http://localhost:8082"
echo "- User Service: http://localhost:8081"
echo "- Chat Service: http://localhost:8083"
echo "- Knowledge Service: http://localhost:8085"
echo "- Chatbot Backend (Legacy): http://localhost:8084"
echo "- Frontend: http://localhost:5173"
echo ""
echo "Infrastructure:"
echo "- Nacos: http://localhost:8848/nacos (nacos/nacos)"
echo "- Seata: http://localhost:8091"
echo "- RocketMQ Console: http://localhost:8080"
echo "- Milvus: http://localhost:9091"
echo ""
echo "Press Ctrl+C to stop all services"
wait 