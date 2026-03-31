package com.sso.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.common.core.exception.BusinessException;
import com.sso.rbac.dto.PermissionDTO;
import com.sso.rbac.entity.Permission;
import com.sso.rbac.entity.RolePermission;
import com.sso.rbac.mapper.PermissionMapper;
import com.sso.rbac.mapper.RolePermissionMapper;
import com.sso.rbac.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final RolePermissionMapper rolePermissionMapper;

    @Override
    @Transactional(readOnly = true)
    public PermissionDTO getById(Long id) {
        Permission permission = baseMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        return convertToDTO(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionDTO create(PermissionDTO request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setCode(request.getCode());
        permission.setType(request.getType());
        permission.setParentId(request.getParentId());
        permission.setPath(request.getPath());
        permission.setIcon(request.getIcon());
        permission.setSort(request.getSort());
        permission.setTenantId(request.getTenantId());
        permission.setStatus(request.getStatus() != null ? request.getStatus() : 0);

        baseMapper.insert(permission);
        log.info("创建权限成功: {}", permission.getName());

        return convertToDTO(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionDTO update(Long id, PermissionDTO request) {
        Permission permission = baseMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        if (request.getName() != null) {
            permission.setName(request.getName());
        }
        if (request.getCode() != null) {
            permission.setCode(request.getCode());
        }
        if (request.getType() != null) {
            permission.setType(request.getType());
        }
        if (request.getParentId() != null) {
            permission.setParentId(request.getParentId());
        }
        if (request.getPath() != null) {
            permission.setPath(request.getPath());
        }
        if (request.getIcon() != null) {
            permission.setIcon(request.getIcon());
        }
        if (request.getSort() != null) {
            permission.setSort(request.getSort());
        }
        if (request.getStatus() != null) {
            permission.setStatus(request.getStatus());
        }

        baseMapper.updateById(permission);
        log.info("更新权限成功: {}", permission.getName());

        return convertToDTO(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Permission permission = baseMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        baseMapper.deleteById(id);
        log.info("删除权限成功: {}", permission.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> getByRoleId(Long roleId) {
        // 通过 RolePermissionMapper 查询关联的权限ID列表
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>()
                        .eq(RolePermission::getRoleId, roleId)
        );

        if (rolePermissions == null || rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }

        // 提取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // 批量查询权限
        List<Permission> permissions = baseMapper.selectBatchIds(permissionIds);

        return permissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setCode(permission.getCode());
        dto.setType(permission.getType());
        dto.setParentId(permission.getParentId());
        dto.setPath(permission.getPath());
        dto.setIcon(permission.getIcon());
        dto.setSort(permission.getSort());
        dto.setTenantId(permission.getTenantId());
        dto.setStatus(permission.getStatus());
        dto.setCreateTime(permission.getCreateTime());
        dto.setUpdateTime(permission.getUpdateTime());
        return dto;
    }
}
