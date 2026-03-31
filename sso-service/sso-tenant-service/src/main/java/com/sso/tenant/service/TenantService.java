package com.sso.tenant.service;

import com.sso.tenant.dto.TenantDTO;

/**
 * 租户服务接口
 */
public interface TenantService {

    /**
     * 根据ID查询租户
     */
    TenantDTO getById(Long id);

    /**
     * 创建租户
     */
    TenantDTO create(TenantDTO request);

    /**
     * 更新租户
     */
    TenantDTO update(Long id, TenantDTO request);

    /**
     * 删除租户
     */
    void delete(Long id);
}
