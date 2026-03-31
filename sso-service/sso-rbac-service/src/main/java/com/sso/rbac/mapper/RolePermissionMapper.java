package com.sso.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.rbac.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联 Mapper
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
}
