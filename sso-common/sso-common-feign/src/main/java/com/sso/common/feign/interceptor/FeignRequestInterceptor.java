package com.sso.common.feign.interceptor;

import com.sso.common.core.constant.CommonConstants;
import com.sso.common.core.constant.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign 请求拦截器 - 传递用户上下文信息
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        // 传递 Authorization
        String authorization = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (authorization != null && !authorization.isEmpty()) {
            template.header(SecurityConstants.AUTHORIZATION_HEADER, authorization);
        }

        // 传递用户ID
        String userId = request.getHeader(CommonConstants.HEADER_USER_ID);
        if (userId != null && !userId.isEmpty()) {
            template.header(CommonConstants.HEADER_USER_ID, userId);
        }

        // 传递用户名
        String username = request.getHeader(CommonConstants.HEADER_USERNAME);
        if (username != null && !username.isEmpty()) {
            template.header(CommonConstants.HEADER_USERNAME, username);
        }

        // 传递租户ID
        String tenantId = request.getHeader(CommonConstants.HEADER_TENANT_ID);
        if (tenantId != null && !tenantId.isEmpty()) {
            template.header(CommonConstants.HEADER_TENANT_ID, tenantId);
        }

        // 传递角色信息
        String roles = request.getHeader(CommonConstants.HEADER_ROLES);
        if (roles != null && !roles.isEmpty()) {
            template.header(CommonConstants.HEADER_ROLES, roles);
        }
    }
}
