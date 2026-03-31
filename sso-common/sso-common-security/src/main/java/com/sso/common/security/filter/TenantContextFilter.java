package com.sso.common.security.filter;

import com.sso.common.core.constant.CommonConstants;
import com.sso.common.security.model.TenantContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * 租户上下文过滤器
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class TenantContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenantId = httpRequest.getHeader(CommonConstants.HEADER_TENANT_ID);

        try {
            if (tenantId != null && !tenantId.isEmpty()) {
                TenantContext.setTenantId(Long.parseLong(tenantId));
            } else {
                TenantContext.setTenantId(CommonConstants.DEFAULT_TENANT_ID);
            }
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
