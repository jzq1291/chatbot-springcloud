#!/bin/bash

# Chatbot微服务Docker镜像构建脚本

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 镜像仓库地址
REGISTRY="chatbot"
VERSION=${1:-latest}

echo -e "${BLUE}开始构建Chatbot微服务Docker镜像...${NC}"
echo -e "${BLUE}版本: ${VERSION}${NC}"
echo ""

# 检查Docker是否运行
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}错误: Docker未运行，请先启动Docker${NC}"
    exit 1
fi

# 构建函数
build_service() {
    local service_name=$1
    local dockerfile_path=$2
    local image_name="${REGISTRY}/${service_name}:${VERSION}"
    
    echo -e "${YELLOW}构建 ${service_name} 服务...${NC}"
    
    if [ -f "$dockerfile_path" ]; then
        docker build -t "$image_name" -f "$dockerfile_path" .
        echo -e "${GREEN}✓ ${service_name} 构建成功: ${image_name}${NC}"
    else
        echo -e "${RED}✗ ${service_name} Dockerfile不存在: ${dockerfile_path}${NC}"
        return 1
    fi
    
    echo ""
}

# 推送函数
push_image() {
    local service_name=$1
    local image_name="${REGISTRY}/${service_name}:${VERSION}"
    
    echo -e "${YELLOW}推送 ${service_name} 镜像...${NC}"
    
    if docker push "$image_name"; then
        echo -e "${GREEN}✓ ${service_name} 推送成功${NC}"
    else
        echo -e "${RED}✗ ${service_name} 推送失败${NC}"
        return 1
    fi
    
    echo ""
}

# 构建所有服务
echo -e "${BLUE}=== 构建阶段 ===${NC}"

build_service "gateway" "gateway-service/Dockerfile"
build_service "auth" "auth-service/Dockerfile"
build_service "user" "user-service/Dockerfile"
build_service "chat" "chat-service/Dockerfile"
build_service "knowledge" "knowledge-service/Dockerfile"
build_service "legacy" "chatbot-backend/Dockerfile"

echo -e "${GREEN}所有镜像构建完成！${NC}"
echo ""

# 显示构建的镜像
echo -e "${BLUE}=== 构建的镜像列表 ===${NC}"
docker images | grep "${REGISTRY}/"

echo ""

# 询问是否推送镜像
read -p "是否推送镜像到仓库? (y/N): " -n 1 -r
echo

if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${BLUE}=== 推送阶段 ===${NC}"
    
    push_image "gateway"
    push_image "auth"
    push_image "user"
    push_image "chat"
    push_image "knowledge"
    push_image "legacy"
    
    echo -e "${GREEN}所有镜像推送完成！${NC}"
else
    echo -e "${YELLOW}跳过镜像推送${NC}"
fi

echo ""
echo -e "${BLUE}=== 构建完成 ===${NC}"
echo -e "${GREEN}镜像版本: ${VERSION}${NC}"
echo -e "${GREEN}镜像仓库: ${REGISTRY}${NC}"
echo ""
echo -e "${BLUE}部署到Kubernetes:${NC}"
echo "kubectl apply -f k8s/namespace.yaml"
echo "kubectl apply -f k8s/configmap.yaml"
echo "kubectl apply -f k8s/secret.yaml"
echo "kubectl apply -f k8s/deployments/"
echo "kubectl apply -f k8s/services/"
echo "kubectl apply -f k8s/hpa/" 