package com.sso.admin.controller;

import com.sso.admin.client.TenantFeignClient;
import com.sso.common.core.result.Result;
import com.sso.tenant.dto.TenantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 租户管理控制器
 */
@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantManageController {

    private final TenantFeignClient tenantFeignClient;

    /**
     * 根据ID查询租户
     */
    @GetMapping("/{id}")
    public Result<TenantDTO> getById(@PathVariable Long id) {
        return tenantFeignClient.getById(id);
    }

    /**
     * 创建租户
     */
    @PostMapping
    public Result<TenantDTO> create(@RequestBody TenantDTO request) {
        return tenantFeignClient.create(request);
    }

    /**
     * 更新租户
     */
    @PutMapping("/{id}")
    public Result<TenantDTO> update(@PathVariable Long id, @RequestBody TenantDTO request) {
        return tenantFeignClient.update(id, request);
    }

    /**
     * 删除租户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return tenantFeignClient.delete(id);
    }
}
