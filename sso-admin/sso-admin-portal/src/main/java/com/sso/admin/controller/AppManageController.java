package com.sso.admin.controller;

import com.sso.admin.client.AppFeignClient;
import com.sso.app.dto.AppDTO;
import com.sso.app.dto.CreateAppRequest;
import com.sso.app.dto.UpdateAppRequest;
import com.sso.common.core.result.PageResult;
import com.sso.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 应用管理控制器
 */
@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
public class AppManageController {

    private final AppFeignClient appFeignClient;

    /**
     * 分页查询应用列表
     */
    @GetMapping
    public Result<PageResult<AppDTO>> list(
            @RequestParam(required = false) String appName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer appType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return appFeignClient.list(appName, status, appType, page, size);
    }

    /**
     * 根据ID查询应用详情
     */
    @GetMapping("/{id}")
    public Result<AppDTO> getById(@PathVariable Long id) {
        return appFeignClient.getById(id);
    }

    /**
     * 创建应用
     */
    @PostMapping
    public Result<AppDTO> create(@RequestBody CreateAppRequest request) {
        return appFeignClient.create(request);
    }

    /**
     * 更新应用
     */
    @PutMapping("/{id}")
    public Result<AppDTO> update(@PathVariable Long id, @RequestBody UpdateAppRequest request) {
        return appFeignClient.update(id, request);
    }

    /**
     * 删除应用
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return appFeignClient.delete(id);
    }

    /**
     * 更新应用状态（启用/禁用）
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        return appFeignClient.updateStatus(id, status);
    }

    /**
     * 重置 Client Secret
     */
    @PutMapping("/{id}/reset-secret")
    public Result<Void> resetSecret(@PathVariable Long id) {
        return appFeignClient.resetSecret(id);
    }

    /**
     * 同步应用到授权服务器
     */
    @PostMapping("/{id}/sync")
    public Result<Void> sync(@PathVariable Long id) {
        return appFeignClient.sync(id);
    }
}
