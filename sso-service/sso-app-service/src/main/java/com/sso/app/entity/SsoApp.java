package com.sso.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用注册实体
 */
@Data
@TableName("sso_app")
public class SsoApp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用编码（唯一标识）
     */
    private String appCode;

    /**
     * OAuth2 Client ID
     */
    private String clientId;

    /**
     * OAuth2 Client Secret（BCrypt加密）
     */
    private String clientSecret;

    /**
     * 应用类型：1-Web应用 2-移动应用 3-SPA单页应用 4-服务端应用
     */
    private Integer appType;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 应用Logo URL
     */
    private String logoUrl;

    /**
     * 应用首页地址
     */
    private String homeUrl;

    /**
     * 授权回调地址（多个用逗号分隔）
     */
    private String redirectUris;

    /**
     * 登出回调地址（多个用逗号分隔）
     */
    private String postLogoutRedirectUris;

    /**
     * 授权范围（多个用逗号分隔）
     */
    private String scopes;

    /**
     * 授权类型（多个用逗号分隔）
     */
    private String grantTypes;

    /**
     * 客户端认证方式
     */
    private String authMethods;

    /**
     * 访问令牌有效期（秒）
     */
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效期（秒）
     */
    private Integer refreshTokenValidity;

    /**
     * 状态：0-启用 1-禁用
     */
    private Integer status;

    /**
     * 所属租户ID
     */
    private Long tenantId;

    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
}
