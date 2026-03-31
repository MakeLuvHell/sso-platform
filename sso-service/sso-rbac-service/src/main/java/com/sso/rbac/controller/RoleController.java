package com.sso.rbac.controller;

import com.sso.common.core.result.Result;
import com.sso.rbac.dto.RoleDTO;
import com.sso.rbac.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public Result<RoleDTO> getById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<RoleDTO> create(@Valid @RequestBody RoleDTO request) {
        return Result.success(roleService.create(request));
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public Result<RoleDTO> update(@PathVariable Long id, @Valid @RequestBody RoleDTO request) {
        return Result.success(roleService.update(id, request));
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/{roleId}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long roleId, @RequestBody java.util.List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
        return Result.success();
    }
}
