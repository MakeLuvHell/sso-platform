package com.sso.admin.client;

import com.sso.common.core.result.Result;
import com.sso.tenant.dto.TenantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 租户服务 Feign 客户端
 */
@FeignClient(name = "sso-tenant-service", path = "/tenants", contextId = "tenantFeignClient")
public interface TenantFeignClient {

    @GetMapping("/{id}")
    Result<TenantDTO> getById(@PathVariable("id") Long id);

    @PostMapping
    Result<TenantDTO> create(@RequestBody TenantDTO request);

    @PutMapping("/{id}")
    Result<TenantDTO> update(@PathVariable("id") Long id, @RequestBody TenantDTO request);

    @DeleteMapping("/{id}")
    Result<Void> delete(@PathVariable("id") Long id);
}
