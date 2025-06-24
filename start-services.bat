@echo off
echo Starting Chatbot Spring Cloud Microservices...

echo.
echo Starting infrastructure services...
docker-compose up -d

echo.
echo Waiting for infrastructure services to start...
timeout /t 10 /nobreak > nul

echo.
echo Starting microservices...

echo Starting gateway-service...
start "Gateway Service" cmd /k "cd gateway-service && mvn spring-boot:run"

echo Starting auth-service...
start "Auth Service" cmd /k "cd auth-service && mvn spring-boot:run"

echo Starting user-service...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"

echo Starting chat-service...
start "Chat Service" cmd /k "cd chat-service && mvn spring-boot:run"

echo Starting knowledge-service...
start "Knowledge Service" cmd /k "cd knowledge-service && mvn spring-boot:run"

echo Starting chatbot-backend (legacy)...
start "Chatbot Backend" cmd /k "cd chatbot-backend && mvn spring-boot:run"

echo.
echo All services are starting...
echo.
echo Service URLs:
echo - Gateway: http://localhost:8080
echo - Auth Service: http://localhost:8082
echo - User Service: http://localhost:8081
echo - Chat Service: http://localhost:8083
echo - Knowledge Service: http://localhost:8085
echo - Chatbot Backend (Legacy): http://localhost:8084
echo - Frontend: http://localhost:5173
echo.
echo Infrastructure:
echo - Nacos: http://localhost:8848/nacos (nacos/nacos)
echo - Seata: http://localhost:8091
echo - RocketMQ Console: http://localhost:8080
echo - Milvus: http://localhost:9091
echo.
echo Press any key to exit...
pause > nul 