package com.jiang.user_center.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员更新用户:
 */
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = -8467346396836993463L;
    /**
     * 用户id
     */

    private Long id;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 账户
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;

    /**
     *
     */
    private Integer userRole;
}
