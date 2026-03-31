package com.sso.admin.controller;

import com.sso.admin.client.AuditFeignClient;
import com.sso.audit.dto.AuditLogDTO;
import com.sso.common.core.result.PageResult;
import com.sso.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 审计管理控制器
 */
@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditManageController {

    private final AuditFeignClient auditFeignClient;

    /**
     * 根据ID查询审计日志
     */
    @GetMapping("/{id}")
    public Result<AuditLogDTO> getById(@PathVariable Long id) {
        return auditFeignClient.getById(id);
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
        return auditFeignClient.page(current, size, module, operation, username);
    }
}
