package com.sso.user.controller;

import com.sso.common.core.result.Result;
import com.sso.user.dto.CreateUserRequest;
import com.sso.user.dto.UpdateUserRequest;
import com.sso.user.dto.UserDTO;
import com.sso.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<UserDTO> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    public Result<UserDTO> getByUsername(@PathVariable String username) {
        return Result.success(userService.getByUsername(username));
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<UserDTO> create(@Valid @RequestBody CreateUserRequest request) {
        return Result.success(userService.create(request));
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return Result.success(userService.update(id, request));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        return Result.success(userService.checkUsername(username));
    }
}
