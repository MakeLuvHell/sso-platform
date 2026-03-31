package com.sso.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.user.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<AuthUser> {
}
