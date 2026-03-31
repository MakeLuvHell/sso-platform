package com.sso.admin.controller;

import com.sso.admin.client.RbacFeignClient;
import com.sso.common.core.result.Result;
import com.sso.rbac.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleManageController {

    private final RbacFeignClient rbacFeignClient;

    /**
     * 查询角色列表
     */
    @GetMapping
    public Result<List<RoleDTO>> list() {
        return rbacFeignClient.listRoles();
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public Result<RoleDTO> getById(@PathVariable Long id) {
        return rbacFeignClient.getRoleById(id);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<RoleDTO> create(@RequestBody RoleDTO request) {
        return rbacFeignClient.createRole(request);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public Result<RoleDTO> update(@PathVariable Long id, @RequestBody RoleDTO request) {
        return rbacFeignClient.updateRole(id, request);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return rbacFeignClient.deleteRole(id);
    }
}
