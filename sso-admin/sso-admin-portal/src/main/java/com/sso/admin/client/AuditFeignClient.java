package com.sso.admin.client;

import com.sso.audit.dto.AuditLogDTO;
import com.sso.common.core.result.PageResult;
import com.sso.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 审计服务 Feign 客户端
 */
@FeignClient(name = "sso-audit-service", path = "/audit-logs", contextId = "auditFeignClient")
public interface AuditFeignClient {

    @GetMapping("/{id}")
    Result<AuditLogDTO> getById(@PathVariable("id") Long id);

    @GetMapping
    Result<PageResult<AuditLogDTO>> page(
            @RequestParam("current") int current,
            @RequestParam("size") int size,
            @RequestParam(value = "module", required = false) String module,
            @RequestParam(value = "operation", required = false) String operation,
            @RequestParam(value = "username", required = false) String username
    );
}
