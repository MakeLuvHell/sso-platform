package com.sso.user.service;

import com.sso.user.dto.CreateUserRequest;
import com.sso.user.dto.UpdateUserRequest;
import com.sso.user.dto.UserDTO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据ID查询用户
     */
    UserDTO getById(Long id);

    /**
     * 根据用户名查询用户
     */
    UserDTO getByUsername(String username);

    /**
     * 创建用户
     */
    UserDTO create(CreateUserRequest request);

    /**
     * 更新用户
     */
    UserDTO update(Long id, UpdateUserRequest request);

    /**
     * 删除用户
     */
    void delete(Long id);

    /**
     * 检查用户名是否存在
     */
    Boolean checkUsername(String username);
}
