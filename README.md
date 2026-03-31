# SSO Platform

基于 Spring Cloud 的企业级单点登录平台。

## 技术栈

- Java 21
- Spring Boot 3.4.2
- Spring Cloud 2024.0.0
- Spring Authorization Server 1.5.x
- Spring Cloud Gateway (WebFlux)
- MyBatis-Plus 3.5.x
- MySQL 8
- Redis 7
- Nacos 2.x

## 模块说明

| 模块 | 说明 |
|------|------|
| sso-common | 公共模块 (core, security, redis, mybatis, feign) |
| sso-gateway | API 网关 (Spring Cloud Gateway) |
| sso-auth | 认证授权服务 (Spring Authorization Server) |
| sso-service | 业务微服务 (user, tenant, rbac, audit) |
| sso-admin | 管理后台聚合服务 |

## 快速开始

### 1. 启动基础设施

```bash
cd docker
docker-compose up -d
```

### 2. 初始化数据库

执行 `sql/` 目录下的 SQL 脚本。

### 3. 启动服务

按以下顺序启动：

1. Nacos (docker-compose)
2. sso-gateway
3. sso-auth-server
4. sso-user-service
5. sso-tenant-service
6. sso-rbac-service
7. sso-audit-service
8. sso-admin-portal

## 端口规划

| 服务 | 端口 |
|------|------|
| Nacos | 8848 |
| Gateway | 8080 |
| Auth Server | 9000 |
| User Service | 9100 |
| Tenant Service | 9200 |
| RBAC Service | 9300 |
| Audit Service | 9400 |
| Admin Portal | 9500 |
