package com.sso.tenant.controller;

import com.sso.common.core.result.Result;
import com.sso.tenant.dto.TenantDTO;
import com.sso.tenant.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 租户控制器
 */
@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    /**
     * 根据ID查询租户
     */
    @GetMapping("/{id}")
    public Result<TenantDTO> getById(@PathVariable Long id) {
        return Result.success(tenantService.getById(id));
    }

    /**
     * 创建租户
     */
    @PostMapping
    public Result<TenantDTO> create(@Valid @RequestBody TenantDTO request) {
        return Result.success(tenantService.create(request));
    }

    /**
     * 更新租户
     */
    @PutMapping("/{id}")
    public Result<TenantDTO> update(@PathVariable Long id, @Valid @RequestBody TenantDTO request) {
        return Result.success(tenantService.update(id, request));
    }

    /**
     * 删除租户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tenantService.delete(id);
        return Result.success();
    }
}
