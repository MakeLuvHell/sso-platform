package com.sso.admin.controller;

import com.sso.common.core.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统配置控制器
 */
@RestController
@RequestMapping("/system-config")
public class SystemConfigController {

    /**
     * 获取系统配置
     */
    @GetMapping
    public Result<Map<String, String>> getConfig() {
        // TODO: 从数据库或配置中心获取系统配置
        return Result.success(Map.of());
    }

    /**
     * 更新系统配置
     */
    @PutMapping
    public Result<Void> updateConfig(@RequestBody Map<String, String> config) {
        // TODO: 更新系统配置
        return Result.success();
    }
}
