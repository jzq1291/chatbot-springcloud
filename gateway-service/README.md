# Gateway Service

## 概述

Gateway Service是微服务架构的API网关，负责统一入口、路由转发、负载均衡、限流熔断等功能。

## 路由机制详解

### 1. 路由配置

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
```

### 2. 转发流程示例

**请求流程：**
```
客户端 → Gateway(8080) → User Service(8081)
```

**详细步骤：**

1. **客户端发送请求**
   ```
   GET http://localhost:8080/api/users/1
   ```

2. **Gateway接收请求**
   - 解析请求路径：`/api/users/1`
   - 匹配路由规则：`Path=/api/users/**`

3. **服务发现**
   - 查找服务：`user-service`
   - 获取实例地址：`http://localhost:8081`

4. **路径处理**
   - 应用过滤器：`StripPrefix=1`
   - 原始路径：`/api/users/1`
   - 处理后：`/users/1`

5. **请求转发**
   ```
   GET http://localhost:8081/users/1
   ```

6. **响应返回**
   - 接收User Service响应
   - 返回给客户端

### 3. 负载均衡

当有多个服务实例时：

```yaml
uri: lb://user-service
```

Gateway会自动在以下实例中选择：
- `http://localhost:8081` (实例1)
- `http://localhost:8082` (实例2)
- `http://localhost:8083` (实例3)

### 4. 过滤器链

```yaml
filters:
  - StripPrefix=1                    # 路径过滤器
  - name: RequestRateLimiter         # 限流过滤器
  - name: Sentinel                   # 熔断过滤器
```

**执行顺序：**
1. **Pre过滤器**：请求转发前
2. **Route过滤器**：路由转发时
3. **Post过滤器**：响应返回时

## 服务路由配置

### 用户服务
- **路径**: `/api/users/**`
- **目标**: `user-service:8081`
- **限流**: 10 req/s, 突发20

### 认证服务
- **路径**: `/api/auth/**`
- **目标**: `auth-service:8082`
- **限流**: 20 req/s, 突发40

### 聊天服务
- **路径**: `/api/chat/**`
- **目标**: `chat-service:8083`
- **限流**: 5 req/s, 突发10

### 知识库服务
- **路径**: `/api/knowledge/**`
- **目标**: `knowledge-service:8085`
- **限流**: 15 req/s, 突发30

## 监控端点

- **健康检查**: `http://localhost:8080/actuator/health`
- **网关信息**: `http://localhost:8080/actuator/gateway`
- **服务信息**: `http://localhost:8080/actuator/info`

## 配置热更新

Gateway支持通过Nacos配置中心动态更新路由配置，无需重启服务。

## 故障排除

### 常见问题

1. **服务无法访问**
   - 检查目标服务是否启动
   - 验证Nacos服务发现是否正常
   - 查看Gateway日志

2. **路由不匹配**
   - 检查路径配置是否正确
   - 验证predicates配置
   - 查看路由匹配日志

3. **限流触发**
   - 检查限流配置
   - 查看限流日志
   - 调整限流参数 