package com.sso.common.core.constant;

/**
 * 安全相关常量
 */
public final class SecurityConstants {

    private SecurityConstants() {
    }

    /** Bearer Token 前缀 */
    public static final String BEARER_PREFIX = "Bearer ";

    /** Authorization 请求头 */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /** JWT 签发者 */
    public static final String JWT_ISSUER = "sso-platform";

    /** JWT 主题前缀 */
    public static final String JWT_SUBJECT_PREFIX = "user:";

    /** JWT 声明 - 用户ID */
    public static final String CLAIM_USER_ID = "user_id";

    /** JWT 声明 - 用户名 */
    public static final String CLAIM_USERNAME = "username";

    /** JWT 声明 - 租户ID */
    public static final String CLAIM_TENANT_ID = "tenant_id";

    /** JWT 声明 - 角色列表 */
    public static final String CLAIM_ROLES = "roles";

    /** JWT 声明 - 权限列表 */
    public static final String CLAIM_AUTHORITIES = "authorities";

    /** 资源服务器客户端ID */
    public static final String RESOURCE_CLIENT_ID = "sso-resource-server";

    /** 默认密码 */
    public static final String DEFAULT_PASSWORD = "123456";

    /** 不需要认证的路径 */
    public static final String[] PERMIT_ALL_URLS = {
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/doc.html",
            "/webjars/**",
            "/favicon.ico"
    };
}
