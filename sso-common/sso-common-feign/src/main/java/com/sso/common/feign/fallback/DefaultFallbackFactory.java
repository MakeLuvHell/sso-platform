package com.sso.common.feign.fallback;

import com.sso.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign 降级工厂基类
 */
@Slf4j
public class DefaultFallbackFactory {

    protected DefaultFallbackFactory() {
    }

    /**
     * 获取默认降级结果
     */
    protected <T> Result<T> defaultFallback(String serviceName, Throwable cause) {
        log.error("调用服务 [{}] 失败: {}", serviceName, cause.getMessage(), cause);
        return Result.fail(500, "服务 [" + serviceName + "] 暂时不可用");
    }
}
