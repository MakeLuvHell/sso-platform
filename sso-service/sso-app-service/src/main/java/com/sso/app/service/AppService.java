package com.sso.app.service;

import com.sso.app.dto.AppDTO;
import com.sso.app.dto.CreateAppRequest;
import com.sso.app.dto.UpdateAppRequest;
import com.sso.common.core.result.PageResult;

/**
 * 应用服务接口
 */
public interface AppService {

    /**
     * 分页查询应用列表
     */
    PageResult<AppDTO> list(String appName, Integer status, Integer appType, int page, int size);

    /**
     * 根据ID查询应用
     */
    AppDTO getById(Long id);

    /**
     * 根据应用编码查询应用
     */
    AppDTO getByAppCode(String appCode);

    /**
     * 创建应用
     */
    AppDTO create(CreateAppRequest request);

    /**
     * 更新应用
     */
    AppDTO update(Long id, UpdateAppRequest request);

    /**
     * 删除应用
     */
    void delete(Long id);

    /**
     * 重置 Client Secret
     */
    void resetSecret(Long id);

    /**
     * 启用/禁用应用
     */
    void updateStatus(Long id, Integer status);

    /**
     * 同步到授权服务器（SAS oauth2_registered_client 表）
     */
    void syncToAuthServer(Long id);
}
