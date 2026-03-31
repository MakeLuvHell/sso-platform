package com.sso.gateway.filter;

import com.sso.common.core.constant.CommonConstants;
import com.sso.common.core.constant.SecurityConstants;
import com.sso.common.core.util.JsonUtils;
import com.sso.common.core.util.JwtUtils;
import com.sso.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 全局认证过滤器
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String JWT_SECRET = "sso-platform-jwt-secret-key-must-be-at-least-256-bits-long-for-hs256";

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/auth/token",
            "/auth/oauth2/**",
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/doc.html",
            "/webjars/**",
            "/favicon.ico"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 白名单路径放行
        if (isWhiteListed(path)) {
            return chain.filter(exchange);
        }

        // 获取 Authorization 头
        String authHeader = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return unauthorizedResponse(exchange, "缺少或无效的 Authorization 头");
        }

        String token = authHeader.substring(SecurityConstants.BEARER_PREFIX.length());

        try {
            // 验证 Token
            if (!JwtUtils.validateToken(token, JWT_SECRET)) {
                return unauthorizedResponse(exchange, "Token 已过期或无效");
            }

            // 提取用户信息并传递到下游服务
            String userId = JwtUtils.getUserId(token, JWT_SECRET);
            String username = JwtUtils.getUsername(token, JWT_SECRET);
            Long tenantId = JwtUtils.getTenantId(token, JWT_SECRET);
            List<String> roles = JwtUtils.getRoles(token, JWT_SECRET);

            ServerHttpRequest.Builder requestBuilder = request.mutate();
            requestBuilder.header(CommonConstants.HEADER_USER_ID, userId);
            requestBuilder.header(CommonConstants.HEADER_USERNAME, username != null ? username : "");
            requestBuilder.header(CommonConstants.HEADER_TENANT_ID,
                    tenantId != null ? tenantId.toString() : String.valueOf(CommonConstants.DEFAULT_TENANT_ID));
            if (roles != null && !roles.isEmpty()) {
                requestBuilder.header(CommonConstants.HEADER_ROLES, String.join(",", roles));
            }

            return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
        } catch (Exception e) {
            log.error("Token 解析异常: {}", e.getMessage());
            return unauthorizedResponse(exchange, "Token 解析失败");
        }
    }

    private boolean isWhiteListed(String path) {
        return WHITE_LIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setAccessControlAllowOrigin("*");

        String body = JsonUtils.toJson(Result.fail(HttpStatus.UNAUTHORIZED.value(), message));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
