package com.sso.admin.controller;

import com.sso.common.core.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        // TODO: 通过 Feign 聚合各服务数据
        return Result.success(Map.of(
                "userCount", 0,
                "tenantCount", 0,
                "roleCount", 0,
                "todayLoginCount", 0
        ));
    }
}
