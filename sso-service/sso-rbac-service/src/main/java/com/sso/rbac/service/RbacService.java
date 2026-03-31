package com.sso.rbac.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RBAC 聚合服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RbacService {

    private final RoleService roleService;
    private final PermissionService permissionService;

    /**
     * 获取用户的角色列表
     */
    public List<String> getUserRoles(Long userId) {
        // TODO: 从 user_role 表查询用户的角色
        return List.of();
    }

    /**
     * 获取用户的权限列表
     */
    public List<String> getUserPermissions(Long userId) {
        // TODO: 通过角色关联查询用户的权限
        return List.of();
    }

    /**
     * 检查用户是否有指定权限
     */
    public boolean hasPermission(Long userId, String permissionCode) {
        List<String> permissions = getUserPermissions(userId);
        return permissions.contains(permissionCode);
    }
}
