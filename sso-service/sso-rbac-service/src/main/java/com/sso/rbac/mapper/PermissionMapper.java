package com.sso.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.rbac.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限 Mapper
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
