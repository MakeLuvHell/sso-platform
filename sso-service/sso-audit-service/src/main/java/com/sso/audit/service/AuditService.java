package com.sso.audit.service;

import com.sso.common.core.result.PageResult;
import com.sso.audit.dto.AuditLogDTO;

/**
 * 审计服务接口
 */
public interface AuditService {

    /**
     * 根据ID查询审计日志
     */
    AuditLogDTO getById(Long id);

    /**
     * 分页查询审计日志
     */
    PageResult<AuditLogDTO> page(int current, int size, String module, String operation, String username);

    /**
     * 记录审计日志
     */
    void log(String module, String operation, String description, String username,
             String ip, String method, String url, String params, Long duration);
}
