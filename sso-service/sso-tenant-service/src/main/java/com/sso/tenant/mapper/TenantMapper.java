package com.sso.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.tenant.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户 Mapper
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
