package com.sso.rbac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SSO RBAC 服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.sso.rbac.mapper")
public class SsoRbacServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoRbacServiceApplication.class, args);
    }
}
