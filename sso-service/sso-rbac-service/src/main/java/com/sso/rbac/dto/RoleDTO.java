package com.sso.rbac.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色 DTO
 */
@Data
public class RoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer dataScope;
    private Long tenantId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
