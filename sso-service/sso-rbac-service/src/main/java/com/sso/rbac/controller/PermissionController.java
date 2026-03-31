package com.sso.rbac.controller;

import com.sso.common.core.result.Result;
import com.sso.rbac.dto.PermissionDTO;
import com.sso.rbac.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 权限控制器
 */
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 根据ID查询权限
     */
    @GetMapping("/{id}")
    public Result<PermissionDTO> getById(@PathVariable Long id) {
        return Result.success(permissionService.getById(id));
    }

    /**
     * 创建权限
     */
    @PostMapping
    public Result<PermissionDTO> create(@Valid @RequestBody PermissionDTO request) {
        return Result.success(permissionService.create(request));
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    public Result<PermissionDTO> update(@PathVariable Long id, @Valid @RequestBody PermissionDTO request) {
        return Result.success(permissionService.update(id, request));
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return Result.success();
    }

    /**
     * 查询角色的权限列表
     */
    @GetMapping("/role/{roleId}")
    public Result<java.util.List<PermissionDTO>> getByRoleId(@PathVariable Long roleId) {
        return Result.success(permissionService.getByRoleId(roleId));
    }
}
