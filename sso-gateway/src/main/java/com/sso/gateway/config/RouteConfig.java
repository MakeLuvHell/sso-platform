package com.sso.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由配置
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 认证服务路由
                .route("sso-auth", r -> r
                        .path("/auth/**")
                        .filters(f -> f
                                .StripPrefix(0)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://sso-auth-server")
                )
                // 用户服务路由
                .route("sso-user", r -> r
                        .path("/api/user/**")
                        .filters(f -> f
                                .StripPrefix(2)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://sso-user-service")
                )
                // 租户服务路由
                .route("sso-tenant", r -> r
                        .path("/api/tenant/**")
                        .filters(f -> f
                                .StripPrefix(2)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://sso-tenant-service")
                )
                // RBAC服务路由
                .route("sso-rbac", r -> r
                        .path("/api/rbac/**")
                        .filters(f -> f
                                .StripPrefix(2)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://sso-rbac-service")
                )
                // 审计服务路由
                .route("sso-audit", r -> r
                        .path("/api/audit/**")
                        .filters(f -> f
                                .StripPrefix(2)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://sso-audit-service")
                )
                // 管理后台路由
                .route("sso-admin", r -> r
                        .path("/admin/**")
                        .filters(f -> f
                                .StripPrefix(1)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://sso-admin-portal")
                )
                .build();
    }
}
