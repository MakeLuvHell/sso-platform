package com.sso.audit.controller;

import com.sso.common.core.result.PageResult;
import com.sso.common.core.result.Result;
import com.sso.audit.dto.AuditLogDTO;
import com.sso.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 审计日志控制器
 */
@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    /**
     * 根据ID查询审计日志
     */
    @GetMapping("/{id}")
    public Result<AuditLogDTO> getById(@PathVariable Long id) {
        return Result.success(auditService.getById(id));
    }

    /**
     * 分页查询审计日志
     */
    @GetMapping
    public Result<PageResult<AuditLogDTO>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String username) {
        return Result.success(auditService.page(current, size, module, operation, username));
    }
}
