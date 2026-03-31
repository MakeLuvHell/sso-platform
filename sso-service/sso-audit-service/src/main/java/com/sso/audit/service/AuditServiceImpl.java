package com.sso.audit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.common.core.result.PageResult;
import com.sso.common.core.util.StringUtils;
import com.sso.audit.dto.AuditLogDTO;
import com.sso.audit.entity.AuditLog;
import com.sso.audit.mapper.AuditLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 审计服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditService {

    @Override
    public AuditLogDTO getById(Long id) {
        AuditLog auditLog = baseMapper.selectById(id);
        if (auditLog == null) {
            return null;
        }
        return convertToDTO(auditLog);
    }

    @Override
    public PageResult<AuditLogDTO> page(int current, int size, String module, String operation, String username) {
        Page<AuditLog> page = new Page<>(current, size);
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(module)) {
            wrapper.like(AuditLog::getModule, module);
        }
        if (StringUtils.isNotBlank(operation)) {
            wrapper.like(AuditLog::getOperation, operation);
        }
        if (StringUtils.isNotBlank(username)) {
            wrapper.like(AuditLog::getUsername, username);
        }
        wrapper.orderByDesc(AuditLog::getCreateTime);

        Page<AuditLog> result = baseMapper.selectPage(page, wrapper);

        return PageResult.of(
                result.getRecords().stream().map(this::convertToDTO).toList(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
    }

    @Override
    @Async
    public void log(String module, String operation, String description, String username,
                    String ip, String method, String url, String params, Long duration) {
        AuditLog auditLog = new AuditLog();
        auditLog.setModule(module);
        auditLog.setOperation(operation);
        auditLog.setDescription(description);
        auditLog.setUsername(username);
        auditLog.setIp(ip);
        auditLog.setMethod(method);
        auditLog.setUrl(url);
        auditLog.setParams(params);
        auditLog.setDuration(duration);
        auditLog.setStatus(1);
        auditLog.setTenantId(0L);

        try {
            baseMapper.insert(auditLog);
        } catch (Exception e) {
            log.error("记录审计日志失败: {}", e.getMessage());
        }
    }

    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setModule(auditLog.getModule());
        dto.setOperation(auditLog.getOperation());
        dto.setDescription(auditLog.getDescription());
        dto.setUsername(auditLog.getUsername());
        dto.setIp(auditLog.getIp());
        dto.setMethod(auditLog.getMethod());
        dto.setUrl(auditLog.getUrl());
        dto.setParams(auditLog.getParams());
        dto.setResult(auditLog.getResult());
        dto.setDuration(auditLog.getDuration());
        dto.setStatus(auditLog.getStatus());
        dto.setTenantId(auditLog.getTenantId());
        dto.setCreateTime(auditLog.getCreateTime());
        return dto;
    }
}
