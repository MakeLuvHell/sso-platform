package com.sso.tenant.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.common.core.exception.BusinessException;
import com.sso.tenant.dto.TenantDTO;
import com.sso.tenant.entity.Tenant;
import com.sso.tenant.mapper.TenantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Override
    public TenantDTO getById(Long id) {
        Tenant tenant = baseMapper.selectById(id);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }
        return convertToDTO(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantDTO create(TenantDTO request) {
        Tenant tenant = new Tenant();
        tenant.setName(request.getName());
        tenant.setCode(request.getCode());
        tenant.setDomain(request.getDomain());
        tenant.setContactName(request.getContactName());
        tenant.setContactEmail(request.getContactEmail());
        tenant.setContactPhone(request.getContactPhone());
        tenant.setStatus(0);
        tenant.setExpireTime(request.getExpireTime());

        baseMapper.insert(tenant);
        log.info("创建租户成功: {}", tenant.getName());

        return convertToDTO(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantDTO update(Long id, TenantDTO request) {
        Tenant tenant = baseMapper.selectById(id);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }

        if (request.getName() != null) {
            tenant.setName(request.getName());
        }
        if (request.getDomain() != null) {
            tenant.setDomain(request.getDomain());
        }
        if (request.getContactName() != null) {
            tenant.setContactName(request.getContactName());
        }
        if (request.getContactEmail() != null) {
            tenant.setContactEmail(request.getContactEmail());
        }
        if (request.getContactPhone() != null) {
            tenant.setContactPhone(request.getContactPhone());
        }
        if (request.getStatus() != null) {
            tenant.setStatus(request.getStatus());
        }
        if (request.getExpireTime() != null) {
            tenant.setExpireTime(request.getExpireTime());
        }

        baseMapper.updateById(tenant);
        log.info("更新租户成功: {}", tenant.getName());

        return convertToDTO(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Tenant tenant = baseMapper.selectById(id);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }
        baseMapper.deleteById(id);
        log.info("删除租户成功: {}", tenant.getName());
    }

    private TenantDTO convertToDTO(Tenant tenant) {
        TenantDTO dto = new TenantDTO();
        dto.setId(tenant.getId());
        dto.setName(tenant.getName());
        dto.setCode(tenant.getCode());
        dto.setDomain(tenant.getDomain());
        dto.setContactName(tenant.getContactName());
        dto.setContactEmail(tenant.getContactEmail());
        dto.setContactPhone(tenant.getContactPhone());
        dto.setStatus(tenant.getStatus());
        dto.setExpireTime(tenant.getExpireTime());
        dto.setCreateTime(tenant.getCreateTime());
        dto.setUpdateTime(tenant.getUpdateTime());
        return dto;
    }
}
