package com.sso.admin.client;

import com.sso.common.core.result.Result;
import com.sso.user.dto.CreateUserRequest;
import com.sso.user.dto.UpdateUserRequest;
import com.sso.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务 Feign 客户端
 */
@FeignClient(name = "sso-user-service", path = "/users", contextId = "userFeignClient")
public interface UserFeignClient {

    @GetMapping
    Result<List<UserDTO>> list();

    @GetMapping("/{id}")
    Result<UserDTO> getById(@PathVariable("id") Long id);

    @PostMapping
    Result<UserDTO> create(@RequestBody CreateUserRequest request);

    @PutMapping("/{id}")
    Result<UserDTO> update(@PathVariable("id") Long id, @RequestBody UpdateUserRequest request);

    @DeleteMapping("/{id}")
    Result<Void> delete(@PathVariable("id") Long id);
}
