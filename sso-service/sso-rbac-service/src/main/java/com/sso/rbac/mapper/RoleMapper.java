package com.sso.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.rbac.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色 Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
