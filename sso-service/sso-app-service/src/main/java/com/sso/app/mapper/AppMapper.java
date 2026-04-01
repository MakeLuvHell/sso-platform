package com.sso.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.app.entity.SsoApp;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用 Mapper
 */
@Mapper
public interface AppMapper extends BaseMapper<SsoApp> {
}
