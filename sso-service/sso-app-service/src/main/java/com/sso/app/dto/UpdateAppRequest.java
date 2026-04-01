package com.sso.app.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新应用请求
 */
@Data
public class UpdateAppRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String appName;

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
}
