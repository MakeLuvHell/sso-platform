package com.sso.audit.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审计日志 DTO
 */
@Data
public class AuditLogDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String module;
    private String operation;
    private String description;
    private String username;
    private Long userId;
    private String ip;
    private String method;
    private String url;
    private String params;
    private String result;
    private Long duration;
    private Integer status;
    private String errorMsg;
    private Long tenantId;
    private LocalDateTime createTime;
}
