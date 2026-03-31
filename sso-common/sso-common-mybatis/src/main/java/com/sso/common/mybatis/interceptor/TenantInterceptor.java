package com.sso.common.mybatis.interceptor;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineInnerInterceptor;
import com.sso.common.mybatis.handler.TenantLineHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 租户拦截器配置
 */
@Configuration
public class TenantInterceptor {

    /**
     * 多租户拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptorWithTenant(MybatisPlusInterceptor mybatisPlusInterceptor) {
        TenantLineHandlerImpl tenantLineHandler = new TenantLineHandlerImpl();
        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(tenantLineHandler);
        // 在分页插件之前添加租户插件
        mybatisPlusInterceptor.getInterceptors().add(0, tenantLineInnerInterceptor);
        return mybatisPlusInterceptor;
    }
}
