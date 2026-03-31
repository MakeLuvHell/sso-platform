package com.sso.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.common.core.exception.BusinessException;
import com.sso.user.dto.CreateUserRequest;
import com.sso.user.dto.UpdateUserRequest;
import com.sso.user.dto.UserDTO;
import com.sso.user.entity.AuthUser;
import com.sso.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, AuthUser> implements UserService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getById(Long id) {
        AuthUser user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO getByUsername(String username) {
        AuthUser user = lambdaQuery()
                .eq(AuthUser::getUsername, username)
                .one();
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToDTO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(CreateUserRequest request) {
        // 检查用户名是否存在
        if (checkUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        AuthUser user = new AuthUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname());
        user.setStatus(0);
        user.setTenantId(request.getTenantId());

        baseMapper.insert(user);
        log.info("创建用户成功: {}", user.getUsername());

        return convertToDTO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO update(Long id, UpdateUserRequest request) {
        AuthUser user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        baseMapper.updateById(user);
        log.info("更新用户成功: {}", user.getUsername());

        return convertToDTO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AuthUser user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        baseMapper.deleteById(id);
        log.info("删除用户成功: {}", user.getUsername());
    }

    @Override
    public Boolean checkUsername(String username) {
        return lambdaQuery()
                .eq(AuthUser::getUsername, username)
                .exists();
    }

    private UserDTO convertToDTO(AuthUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setNickname(user.getNickname());
        dto.setStatus(user.getStatus());
        dto.setTenantId(user.getTenantId());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        return dto;
    }
}
