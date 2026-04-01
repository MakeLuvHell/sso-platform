package com.sso.app.controller;

import com.sso.app.dto.AppDTO;
import com.sso.app.dto.CreateAppRequest;
import com.sso.app.dto.UpdateAppRequest;
import com.sso.app.service.AppService;
import com.sso.common.core.result.PageResult;
import com.sso.common.core.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 应用管理控制器
 */
@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;

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
        return Result.success(appService.list(appName, status, appType, page, size));
    }

    /**
     * 根据ID查询应用详情
     */
    @GetMapping("/{id}")
    public Result<AppDTO> getById(@PathVariable Long id) {
        return Result.success(appService.getById(id));
    }

    /**
     * 创建应用
     */
    @PostMapping
    public Result<AppDTO> create(@Valid @RequestBody CreateAppRequest request) {
        return Result.success(appService.create(request));
    }

    /**
     * 更新应用
     */
    @PutMapping("/{id}")
    public Result<AppDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateAppRequest request) {
        return Result.success(appService.update(id, request));
    }

    /**
     * 删除应用
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        appService.delete(id);
        return Result.success();
    }

    /**
     * 更新应用状态（启用/禁用）
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        appService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 重置 Client Secret
     */
    @PutMapping("/{id}/reset-secret")
    public Result<Void> resetSecret(@PathVariable Long id) {
        appService.resetSecret(id);
        return Result.success();
    }

    /**
     * 同步应用到授权服务器
     */
    @PostMapping("/{id}/sync")
    public Result<Void> syncToAuthServer(@PathVariable Long id) {
        appService.syncToAuthServer(id);
        return Result.success();
    }
}
