package com.sso.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建应用请求
 */
@Data
public class CreateAppRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "应用名称不能为空")
    @Size(max = 100, message = "应用名称长度不能超过100个字符")
    private String appName;

    @NotBlank(message = "应用编码不能为空")
    @Size(max = 50, message = "应用编码长度不能超过50个字符")
    private String appCode;

    private Integer appType;

    private String description;

    private String logoUrl;

    private String homeUrl;

    @NotBlank(message = "授权回调地址不能为空")
    private String redirectUris;

    private String postLogoutRedirectUris;

    private String scopes;

    private String grantTypes;

    private String authMethods;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;
}
