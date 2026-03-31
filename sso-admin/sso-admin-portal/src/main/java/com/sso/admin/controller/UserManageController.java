package com.sso.admin.controller;

import com.sso.admin.client.UserFeignClient;
import com.sso.common.core.result.Result;
import com.sso.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserManageController {

    private final UserFeignClient userFeignClient;

    /**
     * 查询用户列表
     */
    @GetMapping
    public Result<List<UserDTO>> list() {
        return userFeignClient.list();
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<UserDTO> getById(@PathVariable Long id) {
        return userFeignClient.getById(id);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<UserDTO> create(@RequestBody com.sso.user.dto.CreateUserRequest request) {
        return userFeignClient.create(request);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<UserDTO> update(@PathVariable Long id, @RequestBody com.sso.user.dto.UpdateUserRequest request) {
        return userFeignClient.update(id, request);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return userFeignClient.delete(id);
    }
}
