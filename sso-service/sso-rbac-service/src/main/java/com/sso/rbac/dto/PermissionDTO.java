package com.sso.rbac.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限 DTO
 */
@Data
public class PermissionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String code;
    private Integer type;
    private Long parentId;
    private String path;
    private String icon;
    private Integer sort;
    private Long tenantId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
