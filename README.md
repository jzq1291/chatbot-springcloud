# Chatbot SpringCloud 微服务架构

## 项目概述

这是一个基于Spring Cloud Alibaba的微服务架构聊天机器人系统，将原有的单体应用拆分为多个独立的微服务。

## 微服务架构

### 服务列表

1. **gateway-service** (端口: 8080) - API网关服务
   - 统一入口，路由转发
   - 限流、熔断、认证
   - 基于Spring Cloud Gateway

2. **user-service** (端口: 8081) - 用户管理服务
   - 用户CRUD操作
   - 用户角色管理
   - 用户信息查询

3. **auth-service** (端口: 8082) - 认证授权服务
   - JWT token生成和验证
   - 用户登录/注册
   - 权限验证

4. **chat-service** (端口: 8083) - 聊天对话服务
   - AI对话处理
   - 聊天历史管理
   - 流式响应

5. **knowledge-service** (端口: 8085) - 知识库服务
   - 知识库管理
   - 向量搜索
   - 文档索引

6. **chatbot-backend** (端口: 8084) - 遗留单体应用
   - 保留原有功能
   - 用于数据迁移和兼容性
   - 逐步迁移到微服务

### 技术栈

- **Spring Boot 3.2.5** - 基础框架
- **Spring Cloud 2023.0.1** - 微服务框架
- **Spring Cloud Alibaba 2023.0.1.0** - 阿里微服务组件
- **Nacos** - 服务发现和配置中心
- **Spring Cloud Gateway** - API网关
- **OpenFeign** - 服务间调用
- **Sentinel** - 流量控制
- **Seata** - 分布式事务
- **RocketMQ** - 消息队列
- **PostgreSQL** - 关系型数据库
- **Redis** - 缓存
- **Milvus** - 向量数据库

## 环境要求

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 15+
- Redis 7+
- Nacos 2.2.3+

## 快速开始

### 1. 启动基础设施服务

```bash
# 启动所有基础设施服务
docker-compose up -d

# 或者单独启动某个服务
docker-compose up -d postgres redis nacos
```

### 2. 初始化数据库

```bash
# 创建数据库schema
psql -h localhost -U postgres -d chatbotspringcloud -c "
CREATE SCHEMA IF NOT EXISTS user;
CREATE SCHEMA IF NOT EXISTS chat;
CREATE SCHEMA IF NOT EXISTS knowledge;
CREATE SCHEMA IF NOT EXISTS chatbotmb;
"
```

### 3. 启动微服务

```bash
# 使用启动脚本（推荐）
# Windows
start-services.bat

# Linux/Mac
chmod +x start-services.sh
./start-services.sh

# 或者手动启动各个服务
mvn spring-boot:run -pl gateway-service
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl auth-service
mvn spring-boot:run -pl chat-service
mvn spring-boot:run -pl knowledge-service
mvn spring-boot:run -pl chatbot-backend
```

### 4. 启动前端

```bash
cd chatbot-ui
npm install
npm run dev
```

### 5. 验证服务状态

访问以下地址验证服务状态：

- **前端应用**: http://localhost:5173
- **网关**: http://localhost:8080
- **Nacos控制台**: http://localhost:8848/nacos (用户名/密码: nacos/nacos)
- **Seata控制台**: http://localhost:8091
- **RocketMQ控制台**: http://localhost:8080
- **Milvus控制台**: http://localhost:9091

各服务健康检查：
- 网关健康检查: http://localhost:8080/actuator/health
- 用户服务: http://localhost:8081/actuator/health
- 认证服务: http://localhost:8082/actuator/health
- 聊天服务: http://localhost:8083/actuator/health
- 知识库服务: http://localhost:8085/actuator/health
- 遗留后端: http://localhost:8084/actuator/health

## 服务间调用

### 通过网关访问

所有API请求都通过网关进行：

```bash
# 用户管理
GET http://localhost:8080/api/users
POST http://localhost:8080/api/users

# 认证
POST http://localhost:8080/api/auth/login
POST http://localhost:8080/api/auth/register

# 聊天
POST http://localhost:8080/api/chat/send

# 知识库
GET http://localhost:8080/api/knowledge
POST http://localhost:8080/api/knowledge
```

### 服务间直接调用

使用OpenFeign进行服务间调用：

```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/{id}")
    ResponseEntity<User> getUserById(@PathVariable Long id);
}
```

## 配置管理

### Nacos配置

在Nacos控制台中创建以下配置文件：

1. **gateway-service.yaml** - 网关配置
2. **user-service.yaml** - 用户服务配置
3. **auth-service.yaml** - 认证服务配置
4. **chat-service.yaml** - 聊天服务配置
5. **knowledge-service.yaml** - 知识库服务配置

### 配置示例

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/chatbotspringcloud?currentSchema=user
    username: postgres
    password: 123456
  
  redis:
    host: localhost
    port: 6379
    database: 1

jwt:
  secret: your-jwt-secret
  expiration: 86400000
```

## 监控和运维

### 健康检查

每个服务都提供了健康检查端点：

```bash
curl http://localhost:8080/actuator/health
```

### 服务发现

在Nacos控制台中可以看到所有注册的服务实例。

### 配置热更新

通过Nacos控制台可以动态修改配置，无需重启服务。

## 开发指南

### 添加新服务

1. 创建新的Maven模块
2. 添加Spring Cloud Alibaba依赖
3. 配置Nacos服务发现
4. 实现业务逻辑
5. 在网关中添加路由配置

### 数据迁移

chatbot-backend作为遗留服务保留，用于：
- 数据迁移和验证
- 功能兼容性测试
- 逐步迁移到微服务架构

## 故障排除

### 常见问题

1. **端口冲突**
   - 检查各服务端口配置
   - 确保端口未被占用

2. **服务注册失败**
   - 确认Nacos服务运行正常
   - 检查网络连接
   - 验证服务配置

3. **数据库连接失败**
   - 确认PostgreSQL服务运行
   - 检查数据库配置
   - 验证schema是否存在

### 日志查看

```bash
# 查看服务日志
tail -f logs/application.log

# 查看Docker容器日志
docker-compose logs -f [service-name]
```

## 部署

### 开发环境
```bash
# 使用Maven运行
mvn spring-boot:run -pl [service-name]

# 使用Docker运行
docker build -t [service-name] .
docker run -p [port]:[port] [service-name]
```

### 生产环境
```bash
# 构建JAR包
mvn clean package -DskipTests

# 运行JAR包
java -jar target/[service-name]-0.0.1-SNAPSHOT.jar
```

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交更改
4. 创建Pull Request

## 许可证

MIT License

## 联系方式

如有问题，请提交Issue或联系开发团队。

---

## 未来工作计划

### 第一阶段：基础完善
- [ ] 完善服务监控和链路追踪
- [ ] 添加分布式事务支持
- [ ] 实现服务熔断和限流
- [ ] 优化数据库连接池配置

### 第二阶段：功能增强
- [ ] 添加消息推送功能
- [ ] 实现文件上传下载
- [ ] 支持多租户架构
- [ ] 添加数据导入导出

### 第三阶段：性能优化
- [ ] 实现缓存策略
- [ ] 优化数据库查询
- [ ] 添加负载均衡
- [ ] 实现服务降级

### 第四阶段：运维支持
- [ ] 容器化部署
- [ ] CI/CD流水线
- [ ] 自动化测试
- [ ] 性能监控

### 第五阶段：业务扩展
- [ ] 支持多语言
- [ ] 添加插件系统
- [ ] 实现工作流引擎
- [ ] 集成第三方服务