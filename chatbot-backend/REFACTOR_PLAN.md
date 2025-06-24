# Chatbot-Backend 重构计划

## 当前状态
原始的chatbot-backend是一个单体应用，包含以下功能：
- 用户管理
- 认证授权
- 聊天对话
- 知识库管理
- 通用工具类

## 重构目标
将chatbot-backend重构为API聚合服务，移除已迁移到微服务的功能。

## 需要删除的代码

### 1. 用户管理相关
- `controller/UserController.java` - 已迁移到user-service
- `service/UserService.java` - 已迁移到user-service
- `service/impl/UserServiceImpl.java` - 已迁移到user-service
- `mapper/UserMapper.java` - 已迁移到user-service
- `mapper/UserRoleMapper.java` - 已迁移到user-service
- `entity/User.java` - 已迁移到user-service
- `entity/UserRole.java` - 已迁移到user-service

### 2. 认证相关
- `controller/AuthController.java` - 已迁移到auth-service
- `service/AuthService.java` - 已迁移到auth-service
- `service/impl/AuthServiceImpl.java` - 已迁移到auth-service
- `security/JwtService.java` - 已迁移到auth-service
- `security/UserDetailsServiceImpl.java` - 已迁移到auth-service
- `security/JwtAuthenticationFilter.java` - 已迁移到auth-service
- `security/JwtTokenProvider.java` - 已迁移到auth-service
- `service/TokenBlacklistService.java` - 已迁移到auth-service
- `service/impl/TokenBlacklistServiceImpl.java` - 已迁移到auth-service

### 3. 聊天相关
- `controller/ChatController.java` - 已迁移到chat-service
- `service/ChatService.java` - 已迁移到chat-service
- `service/impl/ChatServiceImpl.java` - 已迁移到chat-service
- `entity/ChatMessage.java` - 已迁移到chat-service
- `mapper/ChatMessageMapper.java` - 已迁移到chat-service

### 4. 知识库相关
- `controller/KnowledgeBaseController.java` - 已迁移到knowledge-service
- `service/KnowledgeService.java` - 已迁移到knowledge-service
- `client/KnowledgeClient.java` - 已迁移到knowledge-service

## 需要保留的代码

### 1. 通用工具类
- `util/KeywordExtractor.java` - 关键词提取工具
- `util/KeywordExtractorTest.java` - 测试类

### 2. 配置类
- `config/` - 保留通用配置
- `properties/` - 保留通用属性类

### 3. 异常处理
- `exception/` - 保留通用异常类

### 4. 数据库迁移脚本
- `src/main/resources/db/migration/` - 保留历史迁移脚本

## 需要修改的配置

### 1. application.yaml
- 移除用户、认证、聊天相关的配置
- 保留知识库相关配置（如果需要）
- 更新数据库连接配置

### 2. pom.xml
- 移除已迁移服务的依赖
- 保留通用依赖
- 添加OpenFeign依赖（用于调用其他微服务）

## 重构后的职责

### 1. API聚合
- 提供统一的API接口
- 聚合多个微服务的响应
- 处理跨服务的业务逻辑

### 2. 通用服务
- 提供通用工具类
- 处理文件上传下载
- 提供通用配置

### 3. 前端适配
- 为前端提供兼容的API
- 处理API版本兼容性
- 提供API文档

## 重构步骤

1. **备份当前代码**
2. **删除已迁移的功能代码**
3. **修改配置文件**
4. **更新依赖**
5. **测试功能**
6. **更新文档**

## 注意事项

- 保留数据库迁移脚本，避免数据丢失
- 确保删除代码前已完全迁移到微服务
- 测试所有功能正常后再删除
- 保留必要的日志和监控配置 