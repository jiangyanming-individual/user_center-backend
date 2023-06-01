package com.jiang.user_center.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员添加用户:
 */

@Data
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = -6288378197341024513L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 账户
     */
    private String userAccount;
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
     * 角色:
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;

    //默认的四个不用传;
}
