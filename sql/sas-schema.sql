-- =============================================
-- SSO Platform - Authorization Server Schema
-- Spring Authorization Server 1.5.x required tables
-- =============================================

CREATE DATABASE IF NOT EXISTS `sso_auth` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `sso_auth`;

-- -------------------------------------------
-- OAuth2 授权请求表
-- -------------------------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization` (
    `id` VARCHAR(100) NOT NULL,
    `registered_client_id` VARCHAR(100) NOT NULL,
    `principal_name` VARCHAR(200) NOT NULL,
    `authorization_grant_type` VARCHAR(100) NOT NULL,
    `authorized_scopes` VARCHAR(1000) DEFAULT NULL,
    `attributes` TEXT DEFAULT NULL,
    `state` VARCHAR(500) DEFAULT NULL,
    `authorization_code_value` TEXT DEFAULT NULL,
    `authorization_code_issued_at` TIMESTAMP DEFAULT NULL,
    `authorization_code_expires_at` TIMESTAMP DEFAULT NULL,
    `authorization_code_metadata` TEXT DEFAULT NULL,
    `access_token_value` TEXT DEFAULT NULL,
    `access_token_issued_at` TIMESTAMP DEFAULT NULL,
    `access_token_expires_at` TIMESTAMP DEFAULT NULL,
    `access_token_metadata` TEXT DEFAULT NULL,
    `access_token_type` VARCHAR(100) DEFAULT NULL,
    `access_token_scopes` VARCHAR(1000) DEFAULT NULL,
    `oidc_id_token_value` TEXT DEFAULT NULL,
    `oidc_id_token_issued_at` TIMESTAMP DEFAULT NULL,
    `oidc_id_token_expires_at` TIMESTAMP DEFAULT NULL,
    `oidc_id_token_metadata` TEXT DEFAULT NULL,
    `refresh_token_value` TEXT DEFAULT NULL,
    `refresh_token_issued_at` TIMESTAMP DEFAULT NULL,
    `refresh_token_expires_at` TIMESTAMP DEFAULT NULL,
    `refresh_token_metadata` TEXT DEFAULT NULL,
    `user_code_value` TEXT DEFAULT NULL,
    `user_code_issued_at` TIMESTAMP DEFAULT NULL,
    `user_code_expires_at` TIMESTAMP DEFAULT NULL,
    `user_code_metadata` TEXT DEFAULT NULL,
    `device_code_value` TEXT DEFAULT NULL,
    `device_code_issued_at` TIMESTAMP DEFAULT NULL,
    `device_code_expires_at` TIMESTAMP DEFAULT NULL,
    `device_code_metadata` TEXT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_registered_client_id` (`registered_client_id`),
    KEY `idx_principal_name` (`principal_name`),
    KEY `idx_authorization_code_value` (`authorization_code_value`(255)),
    KEY `idx_access_token_value` (`access_token_value`(255)),
    KEY `idx_refresh_token_value` (`refresh_token_value`(255)),
    KEY `idx_user_code_value` (`user_code_value`(255)),
    KEY `idx_device_code_value` (`device_code_value`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2 授权表';

-- -------------------------------------------
-- OAuth2 授权同意表
-- -------------------------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent` (
    `registered_client_id` VARCHAR(100) NOT NULL,
    `principal_name` VARCHAR(200) NOT NULL,
    `authorities` VARCHAR(1000) NOT NULL,
    PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2 授权同意表';

-- -------------------------------------------
-- OAuth2 注册客户端表
-- -------------------------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client` (
    `id` VARCHAR(100) NOT NULL,
    `client_id` VARCHAR(100) NOT NULL,
    `client_id_issued_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `client_secret` VARCHAR(200) DEFAULT NULL,
    `client_secret_expires_at` TIMESTAMP DEFAULT NULL,
    `client_name` VARCHAR(200) NOT NULL,
    `redirect_uri` VARCHAR(500) DEFAULT NULL,
    `post_logout_redirect_uri` VARCHAR(500) DEFAULT NULL,
    `scope` VARCHAR(1000) NOT NULL,
    `client_authentication_method` VARCHAR(100) NOT NULL,
    `authorization_grant_type` VARCHAR(100) NOT NULL,
    `token_settings` TEXT DEFAULT NULL,
    `token_endpoint_authentication_signing_algorithm` VARCHAR(100) DEFAULT NULL,
    `jwk_set_url` VARCHAR(500) DEFAULT NULL,
    `userinfo_endpoint_url` VARCHAR(500) DEFAULT NULL,
    `client_settings` TEXT DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2 注册客户端表';
