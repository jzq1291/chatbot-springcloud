# 多阶段构建 - 构建阶段
FROM maven:3.8.6-openjdk-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制父POM文件
COPY pom.xml .

# 复制Gateway服务的POM文件
COPY gateway-service/pom.xml gateway-service/

# 下载依赖（利用Docker缓存层）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY gateway-service/src gateway-service/src

# 构建应用
RUN mvn clean package -pl gateway-service -DskipTests

# 运行阶段
FROM openjdk:17-jre-slim

# 设置工作目录
WORKDIR /app

# 创建非root用户
RUN groupadd -r chatbot && useradd -r -g chatbot chatbot

# 复制构建的JAR文件
COPY --from=builder /app/gateway-service/target/*.jar app.jar

# 创建配置目录
RUN mkdir -p /app/config && chown -R chatbot:chatbot /app

# 切换到非root用户
USER chatbot

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["java", \
  "-Xms256m", \
  "-Xmx512m", \
  "-XX:+UseG1GC", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"] 