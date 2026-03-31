package com.sso.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新用户请求
 */
@Data
public class UpdateUserRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private String nickname;

    private Integer status;
}
