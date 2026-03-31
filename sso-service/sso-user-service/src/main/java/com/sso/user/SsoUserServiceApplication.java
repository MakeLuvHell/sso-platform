package com.sso.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SSO 用户服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.sso.user.mapper")
public class SsoUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoUserServiceApplication.class, args);
    }
}
