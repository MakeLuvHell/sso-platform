package com.sso.admin.client;

import com.sso.app.dto.AppDTO;
import com.sso.app.dto.CreateAppRequest;
import com.sso.app.dto.UpdateAppRequest;
import com.sso.common.core.result.PageResult;
import com.sso.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 应用服务 Feign 客户端
 */
@FeignClient(name = "sso-app-service", path = "/apps", contextId = "appFeignClient")
public interface AppFeignClient {

    @GetMapping
    Result<PageResult<AppDTO>> list(
            @RequestParam(value = "appName", required = false) String appName,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "appType", required = false) Integer appType,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @GetMapping("/{id}")
    Result<AppDTO> getById(@PathVariable("id") Long id);

    @PostMapping
    Result<AppDTO> create(@RequestBody CreateAppRequest request);

    @PutMapping("/{id}")
    Result<AppDTO> update(@PathVariable("id") Long id, @RequestBody UpdateAppRequest request);

    @DeleteMapping("/{id}")
    Result<Void> delete(@PathVariable("id") Long id);

    @PutMapping("/{id}/status")
    Result<Void> updateStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status);

    @PutMapping("/{id}/reset-secret")
    Result<Void> resetSecret(@PathVariable("id") Long id);

    @PostMapping("/{id}/sync")
    Result<Void> sync(@PathVariable("id") Long id);
}
