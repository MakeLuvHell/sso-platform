package com.sso.rbac.service;

import com.sso.rbac.dto.PermissionDTO;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {

    /**
     * 根据ID查询权限
     */
    PermissionDTO getById(Long id);

    /**
     * 创建权限
     */
    PermissionDTO create(PermissionDTO request);

    /**
     * 更新权限
     */
    PermissionDTO update(Long id, PermissionDTO request);

    /**
     * 删除权限
     */
    void delete(Long id);

    /**
     * 查询角色的权限列表
     */
    List<PermissionDTO> getByRoleId(Long roleId);
}
