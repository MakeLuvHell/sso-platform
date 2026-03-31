package com.sso.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SSO 网关启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SsoGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoGatewayApplication.class, args);
    }
}
