package com.sso.tenant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SSO 租户服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.sso.tenant.mapper")
public class SsoTenantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoTenantServiceApplication.class, args);
    }
}
