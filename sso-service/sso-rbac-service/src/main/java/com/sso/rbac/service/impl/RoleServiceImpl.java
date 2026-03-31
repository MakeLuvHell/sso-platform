package com.sso.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.common.core.exception.BusinessException;
import com.sso.rbac.dto.RoleDTO;
import com.sso.rbac.entity.Role;
import com.sso.rbac.entity.RolePermission;
import com.sso.rbac.mapper.RoleMapper;
import com.sso.rbac.mapper.RolePermissionMapper;
import com.sso.rbac.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public RoleDTO getById(Long id) {
        Role role = baseMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return convertToDTO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO create(RoleDTO request) {
        // 检查同租户下角色编码唯一性
        boolean exists = lambdaQuery()
                .eq(Role::getCode, request.getCode())
                .eq(Role::getTenantId, request.getTenantId())
                .exists();
        if (exists) {
            throw new BusinessException("同租户下角色编码已存在");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setDescription(request.getDescription());
        role.setDataScope(request.getDataScope());
        role.setTenantId(request.getTenantId());
        role.setStatus(request.getStatus() != null ? request.getStatus() : 0);

        baseMapper.insert(role);
        log.info("创建角色成功: {}", role.getName());

        return convertToDTO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO update(Long id, RoleDTO request) {
        Role role = baseMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        if (request.getName() != null) {
            role.setName(request.getName());
        }
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
        if (request.getDataScope() != null) {
            role.setDataScope(request.getDataScope());
        }
        if (request.getStatus() != null) {
            role.setStatus(request.getStatus());
        }

        baseMapper.updateById(role);
        log.info("更新角色成功: {}", role.getName());

        return convertToDTO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Role role = baseMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 检查是否为系统预置角色（编码以 SYS_ 开头视为系统预置角色）
        if (role.getCode() != null && role.getCode().startsWith("SYS_")) {
            throw new BusinessException("系统预置角色不允许删除");
        }

        baseMapper.deleteById(id);
        log.info("删除角色成功: {}", role.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = baseMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 先删除旧的权限关联
        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermission>()
                        .eq(RolePermission::getRoleId, roleId)
        );

        // 批量插入新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermissionMapper.insert(rolePermission);
            }
        }

        log.info("角色[{}]分配权限成功，共{}个权限", role.getName(),
                permissionIds != null ? permissionIds.size() : 0);
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setCode(role.getCode());
        dto.setDescription(role.getDescription());
        dto.setDataScope(role.getDataScope());
        dto.setTenantId(role.getTenantId());
        dto.setStatus(role.getStatus());
        dto.setCreateTime(role.getCreateTime());
        dto.setUpdateTime(role.getUpdateTime());
        return dto;
    }
}
