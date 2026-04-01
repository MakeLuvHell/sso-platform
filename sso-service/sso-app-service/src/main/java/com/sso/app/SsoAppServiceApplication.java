package com.sso.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SSO 应用服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.sso.app.mapper")
public class SsoAppServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoAppServiceApplication.class, args);
    }
}
