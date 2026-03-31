-- =============================================
-- SSO Platform - 初始化数据
-- =============================================

USE `sso_platform`;

-- -------------------------------------------
-- 初始化默认租户
-- -------------------------------------------
INSERT INTO `tenant` (`id`, `name`, `code`, `domain`, `contact_name`, `contact_email`, `contact_phone`, `status`, `expire_time`)
VALUES (0, '默认租户', 'DEFAULT', NULL, '系统管理员', 'admin@sso-platform.com', '13800000000', 0, '2099-12-31 23:59:59');

-- -------------------------------------------
-- 初始化默认角色
-- -------------------------------------------
INSERT INTO `role` (`id`, `name`, `code`, `description`, `data_scope`, `tenant_id`, `status`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限', 1, 0, 0),
(2, '租户管理员', 'TENANT_ADMIN', '管理租户内资源', 2, 0, 0),
(3, '普通用户', 'USER', '普通用户', 4, 0, 0);

-- -------------------------------------------
-- 初始化默认权限
-- -------------------------------------------
INSERT INTO `permission` (`id`, `name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`, `tenant_id`, `status`) VALUES
-- 菜单权限
(1, '用户管理', 'system:user', 1, 0, '/system/user', 'user', 1, 0, 0),
(2, '租户管理', 'system:tenant', 1, 0, '/system/tenant', 'tenant', 2, 0, 0),
(3, '角色管理', 'system:role', 1, 0, '/system/role', 'role', 3, 0, 0),
(4, '权限管理', 'system:permission', 1, 0, '/system/permission', 'permission', 4, 0, 0),
(5, '审计日志', 'system:audit', 1, 0, '/system/audit', 'audit', 5, 0, 0),
(6, '系统配置', 'system:config', 1, 0, '/system/config', 'config', 6, 0, 0),
-- 按钮权限
(101, '用户新增', 'user:create', 2, 1, NULL, NULL, 1, 0, 0),
(102, '用户编辑', 'user:update', 2, 1, NULL, NULL, 2, 0, 0),
(103, '用户删除', 'user:delete', 2, 1, NULL, NULL, 3, 0, 0),
(104, '用户查询', 'user:read', 2, 1, NULL, NULL, 4, 0, 0),
(201, '租户新增', 'tenant:create', 2, 2, NULL, NULL, 1, 0, 0),
(202, '租户编辑', 'tenant:update', 2, 2, NULL, NULL, 2, 0, 0),
(203, '租户删除', 'tenant:delete', 2, 2, NULL, NULL, 3, 0, 0),
(204, '租户查询', 'tenant:read', 2, 2, NULL, NULL, 4, 0, 0),
(301, '角色新增', 'role:create', 2, 3, NULL, NULL, 1, 0, 0),
(302, '角色编辑', 'role:update', 2, 3, NULL, NULL, 2, 0, 0),
(303, '角色删除', 'role:delete', 2, 3, NULL, NULL, 3, 0, 0),
(304, '角色查询', 'role:read', 2, 3, NULL, NULL, 4, 0, 0);

-- -------------------------------------------
-- 初始化超级管理员角色权限关联
-- -------------------------------------------
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 101), (1, 102), (1, 103), (1, 104),
(1, 201), (1, 202), (1, 203), (1, 204),
(1, 301), (1, 302), (1, 303), (1, 304);

-- =============================================
-- Authorization Server 初始化数据
-- =============================================

USE `sso_auth`;

-- -------------------------------------------
-- 初始化 OAuth2 客户端
-- 注意: client_secret 需要使用 BCrypt 加密
-- 默认密码: sso-client-secret
-- -------------------------------------------
INSERT INTO `oauth2_registered_client` (
    `id`, `client_id`, `client_id_issued_at`, `client_secret`,
    `client_name`, `redirect_uri`, `post_logout_redirect_uri`,
    `scope`, `client_authentication_method`, `authorization_grant_type`,
    `client_settings`, `token_settings`
) VALUES (
    'sso-web-client',
    'sso-web-client',
    CURRENT_TIMESTAMP,
    '$2a$10$VMjEzNzY2MjQ1NjY2NjY2eWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY',
    'SSO Web Client',
    'http://localhost:3000/login/oauth2/code/sso',
    'http://localhost:3000',
    'openid,profile,email,read,write',
    'client_secret_basic',
    'authorization_code,refresh_token',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration","PT2H"],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration","PT720H"],"settings.token.authorization-code-time-to-live":["java.time.Duration","PT5M"]}'
);

INSERT INTO `oauth2_registered_client` (
    `id`, `client_id`, `client_id_issued_at`, `client_secret`,
    `client_name`, `redirect_uri`, `post_logout_redirect_uri`,
    `scope`, `client_authentication_method`, `authorization_grant_type`,
    `client_settings`, `token_settings`
) VALUES (
    'sso-resource-server',
    'sso-resource-server',
    CURRENT_TIMESTAMP,
    '$2a$10$VMjEzNzY2MjQ1NjY2NjY2eWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY',
    'SSO Resource Server',
    NULL,
    NULL,
    'openid,profile,email,read,write',
    'client_secret_basic',
    'client_credentials',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.access-token-time-to-live":["java.time.Duration","PT2H"],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"}}'
);
