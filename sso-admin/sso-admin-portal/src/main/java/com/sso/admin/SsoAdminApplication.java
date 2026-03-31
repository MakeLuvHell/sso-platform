package com.sso.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * SSO 管理后台启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.sso.admin.client")
public class SsoAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoAdminApplication.class, args);
    }
}
