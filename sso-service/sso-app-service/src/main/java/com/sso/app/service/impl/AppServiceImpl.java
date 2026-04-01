package com.sso.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.app.dto.AppDTO;
import com.sso.app.dto.CreateAppRequest;
import com.sso.app.dto.UpdateAppRequest;
import com.sso.app.entity.SsoApp;
import com.sso.app.mapper.AppMapper;
import com.sso.app.service.AppService;
import com.sso.common.core.exception.BusinessException;
import com.sso.common.core.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 应用服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper, SsoApp> implements AppService {

    private final PasswordEncoder passwordEncoder;

    @Qualifier("authJdbcTemplate")
    private final JdbcTemplate authJdbcTemplate;

    @Override
    public PageResult<AppDTO> list(String appName, Integer status, Integer appType, int page, int size) {
        Page<SsoApp> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SsoApp> wrapper = new LambdaQueryWrapper<>();

        if (appName != null && !appName.isBlank()) {
            wrapper.like(SsoApp::getAppName, appName);
        }
        if (status != null) {
            wrapper.eq(SsoApp::getStatus, status);
        }
        if (appType != null) {
            wrapper.eq(SsoApp::getAppType, appType);
        }
        wrapper.orderByDesc(SsoApp::getCreateTime);

        Page<SsoApp> result = baseMapper.selectPage(pageParam, wrapper);

        return PageResult.of(
                result.getRecords().stream().map(this::convertToDTO).toList(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
    }

    @Override
    public AppDTO getById(Long id) {
        SsoApp app = baseMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }
        return convertToDTO(app);
    }

    @Override
    public AppDTO getByAppCode(String appCode) {
        SsoApp app = lambdaQuery()
                .eq(SsoApp::getAppCode, appCode)
                .one();
        if (app == null) {
            throw new BusinessException("应用不存在");
        }
        return convertToDTO(app);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppDTO create(CreateAppRequest request) {
        // 检查 appCode 唯一性
        boolean exists = lambdaQuery()
                .eq(SsoApp::getAppCode, request.getAppCode())
                .exists();
        if (exists) {
            throw new BusinessException("应用编码已存在");
        }

        // 自动生成 clientId
        String clientId = "app_" + UUID.randomUUID().toString().replace("-", "");

        // 自动生成 clientSecret 并 BCrypt 加密
        String rawSecret = generateRandomSecret(32);
        String encodedSecret = passwordEncoder.encode(rawSecret);

        SsoApp app = new SsoApp();
        app.setAppName(request.getAppName());
        app.setAppCode(request.getAppCode());
        app.setClientId(clientId);
        app.setClientSecret(encodedSecret);
        app.setAppType(request.getAppType() != null ? request.getAppType() : 1);
        app.setDescription(request.getDescription());
        app.setLogoUrl(request.getLogoUrl());
        app.setHomeUrl(request.getHomeUrl());
        app.setRedirectUris(request.getRedirectUris());
        app.setPostLogoutRedirectUris(request.getPostLogoutRedirectUris());
        app.setScopes(request.getScopes() != null ? request.getScopes() : "openid,profile");
        app.setGrantTypes(request.getGrantTypes() != null ? request.getGrantTypes() : "authorization_code,refresh_token");
        app.setAuthMethods(request.getAuthMethods() != null ? request.getAuthMethods() : "client_secret_basic");
        app.setAccessTokenValidity(request.getAccessTokenValidity() != null ? request.getAccessTokenValidity() : 7200);
        app.setRefreshTokenValidity(request.getRefreshTokenValidity() != null ? request.getRefreshTokenValidity() : 2592000);
        app.setStatus(0);
        app.setTenantId(0L);

        baseMapper.insert(app);
        log.info("创建应用成功: {} (clientId={})", app.getAppName(), clientId);
        log.info("应用 Client Secret（请妥善保管）: {}", rawSecret);

        return convertToDTO(app);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppDTO update(Long id, UpdateAppRequest request) {
        SsoApp app = baseMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }

        if (request.getAppName() != null) {
            app.setAppName(request.getAppName());
        }
        if (request.getDescription() != null) {
            app.setDescription(request.getDescription());
        }
        if (request.getLogoUrl() != null) {
            app.setLogoUrl(request.getLogoUrl());
        }
        if (request.getHomeUrl() != null) {
            app.setHomeUrl(request.getHomeUrl());
        }
        if (request.getRedirectUris() != null) {
            app.setRedirectUris(request.getRedirectUris());
        }
        if (request.getPostLogoutRedirectUris() != null) {
            app.setPostLogoutRedirectUris(request.getPostLogoutRedirectUris());
        }
        if (request.getScopes() != null) {
            app.setScopes(request.getScopes());
        }
        if (request.getGrantTypes() != null) {
            app.setGrantTypes(request.getGrantTypes());
        }
        if (request.getAuthMethods() != null) {
            app.setAuthMethods(request.getAuthMethods());
        }
        if (request.getAccessTokenValidity() != null) {
            app.setAccessTokenValidity(request.getAccessTokenValidity());
        }
        if (request.getRefreshTokenValidity() != null) {
            app.setRefreshTokenValidity(request.getRefreshTokenValidity());
        }
        if (request.getStatus() != null) {
            app.setStatus(request.getStatus());
        }

        baseMapper.updateById(app);
        log.info("更新应用成功: {}", app.getAppName());

        return convertToDTO(app);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SsoApp app = baseMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }
        baseMapper.deleteById(id);
        log.info("删除应用成功: {}", app.getAppName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetSecret(Long id) {
        SsoApp app = baseMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }

        String rawSecret = generateRandomSecret(32);
        String encodedSecret = passwordEncoder.encode(rawSecret);

        app.setClientSecret(encodedSecret);
        baseMapper.updateById(app);

        log.info("重置应用密钥成功: {} (clientId={})", app.getAppName(), app.getClientId());
        log.info("新 Client Secret（请妥善保管）: {}", rawSecret);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        SsoApp app = baseMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }

        app.setStatus(status);
        baseMapper.updateById(app);
        log.info("更新应用状态成功: {} -> status={}", app.getAppName(), status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncToAuthServer(Long id) {
        SsoApp app = baseMapper.selectById(id);
        if (app == null) {
            throw new BusinessException("应用不存在");
        }

        // 检查是否已存在
        Integer count = authJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM oauth2_registered_client WHERE client_id = ?",
                Integer.class, app.getClientId()
        );

        if (count != null && count > 0) {
            // 更新
            authJdbcTemplate.update(
                    "UPDATE oauth2_registered_client SET client_secret = ?, client_name = ?, " +
                            "redirect_uri = ?, post_logout_redirect_uri = ?, scope = ?, " +
                            "client_authentication_method = ?, authorization_grant_type = ?, " +
                            "token_settings = ?, client_settings = ? WHERE client_id = ?",
                    app.getClientSecret(),
                    app.getAppName(),
                    app.getRedirectUris(),
                    app.getPostLogoutRedirectUris(),
                    app.getScopes(),
                    app.getAuthMethods(),
                    app.getGrantTypes(),
                    buildTokenSettings(app),
                    buildClientSettings(app),
                    app.getClientId()
            );
            log.info("同步应用到授权服务器（更新）: {}", app.getAppName());
        } else {
            // 插入
            String registeredId = UUID.randomUUID().toString();
            authJdbcTemplate.update(
                    "INSERT INTO oauth2_registered_client (id, client_id, client_id_issued_at, " +
                            "client_secret, client_name, redirect_uri, post_logout_redirect_uri, " +
                            "scope, client_authentication_method, authorization_grant_type, " +
                            "token_settings, client_settings) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    registeredId,
                    app.getClientId(),
                    LocalDateTime.now(),
                    app.getClientSecret(),
                    app.getAppName(),
                    app.getRedirectUris(),
                    app.getPostLogoutRedirectUris(),
                    app.getScopes(),
                    app.getAuthMethods(),
                    app.getGrantTypes(),
                    buildTokenSettings(app),
                    buildClientSettings(app)
            );
            log.info("同步应用到授权服务器（新增）: {}", app.getAppName());
        }
    }

    /**
     * 构建 SAS token_settings JSON
     */
    private String buildTokenSettings(SsoApp app) {
        return String.format(
                "{\"%s\":{\"access-token-ttl\":\"PT%dS\",\"refresh-token-ttl\":\"PT%dS\",\"reuse-refresh-tokens\":true}}",
                "settings",
                app.getAccessTokenValidity(),
                app.getRefreshTokenValidity()
        );
    }

    /**
     * 构建 SAS client_settings JSON
     */
    private String buildClientSettings(SsoApp app) {
        return String.format(
                "{\"%s\":{\"require-authorization-consent\":true,\"require-proof-key\":false}}",
                "settings"
        );
    }

    /**
     * 生成随机密钥
     */
    private String generateRandomSecret(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (chars.length() * Math.random())));
        }
        return sb.toString();
    }

    /**
     * Entity -> DTO 转换，clientId 脱敏显示
     */
    private AppDTO convertToDTO(SsoApp app) {
        AppDTO dto = new AppDTO();
        dto.setId(app.getId());
        dto.setAppName(app.getAppName());
        dto.setAppCode(app.getAppCode());
        dto.setClientId(app.getClientId());
        dto.setClientIdShow(maskClientId(app.getClientId()));
        dto.setAppType(app.getAppType());
        dto.setDescription(app.getDescription());
        dto.setLogoUrl(app.getLogoUrl());
        dto.setHomeUrl(app.getHomeUrl());
        dto.setRedirectUris(app.getRedirectUris());
        dto.setPostLogoutRedirectUris(app.getPostLogoutRedirectUris());
        dto.setScopes(app.getScopes());
        dto.setGrantTypes(app.getGrantTypes());
        dto.setAuthMethods(app.getAuthMethods());
        dto.setAccessTokenValidity(app.getAccessTokenValidity());
        dto.setRefreshTokenValidity(app.getRefreshTokenValidity());
        dto.setStatus(app.getStatus());
        dto.setTenantId(app.getTenantId());
        dto.setCreateTime(app.getCreateTime());
        dto.setUpdateTime(app.getUpdateTime());
        return dto;
    }

    /**
     * Client ID 脱敏：前8位+****+后4位
     */
    private String maskClientId(String clientId) {
        if (clientId == null || clientId.length() <= 12) {
            return clientId;
        }
        return clientId.substring(0, 8) + "****" + clientId.substring(clientId.length() - 4);
    }
}
