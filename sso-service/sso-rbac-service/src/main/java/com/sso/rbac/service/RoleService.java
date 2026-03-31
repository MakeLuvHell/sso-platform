package com.sso.rbac.service;

import com.sso.rbac.dto.RoleDTO;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 根据ID查询角色
     */
    RoleDTO getById(Long id);

    /**
     * 创建角色
     */
    RoleDTO create(RoleDTO request);

    /**
     * 更新角色
     */
    RoleDTO update(Long id, RoleDTO request);

    /**
     * 删除角色
     */
    void delete(Long id);

    /**
     * 为角色分配权限
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);
}
