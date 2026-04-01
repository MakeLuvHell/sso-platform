package com.sso.app.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用 DTO
 */
@Data
public class AppDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String appName;
    private String appCode;
    private String clientId;
    private String clientIdShow;
    private Integer appType;
    private String description;
    private String logoUrl;
    private String homeUrl;
    private String redirectUris;
    private String postLogoutRedirectUris;
    private String scopes;
    private String grantTypes;
    private String authMethods;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private Integer status;
    private Long tenantId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
