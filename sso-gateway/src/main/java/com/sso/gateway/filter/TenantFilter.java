package com.sso.gateway.filter;

import com.sso.common.core.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 租户过滤器
 */
@Slf4j
@Component
public class TenantFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 如果请求头中没有租户ID，设置默认值
        String tenantId = request.getHeaders().getFirst(CommonConstants.HEADER_TENANT_ID);
        if (tenantId == null || tenantId.isEmpty()) {
            ServerHttpRequest.Builder requestBuilder = request.mutate();
            requestBuilder.header(CommonConstants.HEADER_TENANT_ID,
                    String.valueOf(CommonConstants.DEFAULT_TENANT_ID));
            return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
