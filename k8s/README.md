# Kubernetes 部署指南

## 概述

本目录包含Chatbot微服务在Kubernetes集群中的完整部署配置。

## 目录结构

```
k8s/
├── README.md                    # 部署说明
├── namespace.yaml               # 命名空间配置
├── configmap.yaml              # 配置映射
├── secret.yaml                 # 密钥配置
├── deployments/                # 部署配置
│   ├── gateway-deployment.yaml
│   ├── auth-deployment.yaml
│   ├── user-deployment.yaml
│   ├── chat-deployment.yaml
│   ├── knowledge-deployment.yaml
│   └── legacy-deployment.yaml
├── services/                   # 服务配置
│   ├── gateway-service.yaml
│   ├── auth-service.yaml
│   ├── user-service.yaml
│   ├── chat-service.yaml
│   ├── knowledge-service.yaml
│   └── legacy-service.yaml
├── hpa/                       # 自动扩缩容配置
│   ├── gateway-hpa.yaml
│   ├── chat-hpa.yaml
│   └── knowledge-hpa.yaml
├── infrastructure/            # 基础设施服务
│   ├── nacos-statefulset.yaml
│   ├── postgresql-statefulset.yaml
│   ├── redis-statefulset.yaml
│   ├── rocketmq-statefulset.yaml
│   ├── seata-statefulset.yaml
│   └── milvus-statefulset.yaml
├── monitoring/               # 监控配置
│   ├── prometheus.yaml
│   ├── grafana.yaml
│   └── alertmanager.yaml
└── ingress/                 # 入口配置
    └── chatbot-ingress.yaml
```

## 部署架构

### 微服务部署

每个微服务都部署为独立的Deployment：

1. **Gateway Service** (2-3个副本)
   - 端口：8080
   - 资源：512Mi/500m
   - 类型：LoadBalancer

2. **Auth Service** (1-2个副本)
   - 端口：8082
   - 资源：256Mi/250m
   - 类型：ClusterIP

3. **User Service** (1-2个副本)
   - 端口：8081
   - 资源：256Mi/250m
   - 类型：ClusterIP

4. **Chat Service** (2-5个副本，可自动扩缩容)
   - 端口：8083
   - 资源：1Gi/1000m
   - 类型：ClusterIP

5. **Knowledge Service** (1-3个副本)
   - 端口：8085
   - 资源：512Mi/500m
   - 类型：ClusterIP

6. **Legacy Service** (1个副本)
   - 端口：8084
   - 资源：1Gi/1000m
   - 类型：ClusterIP

### 基础设施服务

使用StatefulSet部署有状态服务：

1. **Nacos** - 服务发现和配置中心
2. **PostgreSQL** - 主数据库
3. **Redis** - 缓存和会话存储
4. **RocketMQ** - 消息队列
5. **Seata** - 分布式事务
6. **Milvus** - 向量数据库

## 部署步骤

### 1. 准备环境

```bash
# 确保kubectl已配置
kubectl cluster-info

# 创建命名空间
kubectl apply -f namespace.yaml
```

### 2. 部署配置

```bash
# 部署ConfigMap和Secret
kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml
```

### 3. 部署基础设施

```bash
# 按顺序部署基础设施服务
kubectl apply -f infrastructure/postgresql-statefulset.yaml
kubectl apply -f infrastructure/redis-statefulset.yaml
kubectl apply -f infrastructure/nacos-statefulset.yaml
kubectl apply -f infrastructure/rocketmq-statefulset.yaml
kubectl apply -f infrastructure/seata-statefulset.yaml
kubectl apply -f infrastructure/milvus-statefulset.yaml
```

### 4. 部署微服务

```bash
# 部署所有微服务
kubectl apply -f deployments/
kubectl apply -f services/
```

### 5. 部署自动扩缩容

```bash
# 部署HPA配置
kubectl apply -f hpa/
```

### 6. 部署监控

```bash
# 部署监控组件
kubectl apply -f monitoring/
```

### 7. 部署入口

```bash
# 部署Ingress配置
kubectl apply -f ingress/
```

## 验证部署

### 检查Pod状态

```bash
kubectl get pods -n chatbot
```

### 检查服务状态

```bash
kubectl get services -n chatbot
```

### 检查HPA状态

```bash
kubectl get hpa -n chatbot
```

### 查看日志

```bash
# 查看Gateway服务日志
kubectl logs -f deployment/gateway-service -n chatbot

# 查看Chat服务日志
kubectl logs -f deployment/chat-service -n chatbot
```

## 访问服务

### 通过LoadBalancer

```bash
# 获取Gateway服务的外部IP
kubectl get service gateway-service -n chatbot

# 访问API
curl http://<EXTERNAL-IP>/api/users
```

### 通过Ingress

```bash
# 获取Ingress地址
kubectl get ingress -n chatbot

# 访问服务
curl http://<INGRESS-IP>/api/users
```

## 扩缩容

### 手动扩缩容

```bash
# 扩展Chat服务到5个副本
kubectl scale deployment chat-service --replicas=5 -n chatbot

# 扩展Gateway服务到3个副本
kubectl scale deployment gateway-service --replicas=3 -n chatbot
```

### 自动扩缩容

HPA会根据CPU和内存使用率自动扩缩容：

```bash
# 查看HPA状态
kubectl describe hpa chat-service-hpa -n chatbot
```

## 更新部署

### 滚动更新

```bash
# 更新镜像版本
kubectl set image deployment/gateway-service gateway=chatbot/gateway:v1.1.0 -n chatbot

# 查看更新状态
kubectl rollout status deployment/gateway-service -n chatbot
```

### 回滚

```bash
# 回滚到上一个版本
kubectl rollout undo deployment/gateway-service -n chatbot

# 回滚到指定版本
kubectl rollout undo deployment/gateway-service --to-revision=2 -n chatbot
```

## 故障排除

### 常见问题

1. **Pod启动失败**
   ```bash
   # 查看Pod事件
   kubectl describe pod <pod-name> -n chatbot
   
   # 查看Pod日志
   kubectl logs <pod-name> -n chatbot
   ```

2. **服务无法访问**
   ```bash
   # 检查服务配置
   kubectl describe service <service-name> -n chatbot
   
   # 检查端点
   kubectl get endpoints <service-name> -n chatbot
   ```

3. **配置问题**
   ```bash
   # 检查ConfigMap
   kubectl describe configmap chatbot-config -n chatbot
   
   # 检查Secret
   kubectl describe secret chatbot-secret -n chatbot
   ```

### 资源监控

```bash
# 查看资源使用情况
kubectl top pods -n chatbot

# 查看节点资源使用情况
kubectl top nodes
```

## 清理部署

```bash
# 删除所有资源
kubectl delete namespace chatbot

# 或者逐个删除
kubectl delete -f deployments/
kubectl delete -f services/
kubectl delete -f infrastructure/
kubectl delete -f monitoring/
kubectl delete -f ingress/
kubectl delete -f hpa/
kubectl delete -f configmap.yaml
kubectl delete -f secret.yaml
kubectl delete -f namespace.yaml
```

## 最佳实践

1. **资源管理**
   - 合理设置资源请求和限制
   - 使用HPA自动扩缩容
   - 监控资源使用情况

2. **健康检查**
   - 配置liveness和readiness探针
   - 设置合适的检查间隔
   - 监控服务健康状态

3. **配置管理**
   - 使用ConfigMap管理配置
   - 使用Secret管理敏感信息
   - 支持配置热更新

4. **日志管理**
   - 使用ELK或EFK收集日志
   - 配置日志轮转
   - 设置日志级别

5. **监控告警**
   - 配置Prometheus监控
   - 设置Grafana仪表板
   - 配置告警规则 