package com.jiang.user_center.contant;

/**
 * @author Lenovo
 * @date 2023/4/17
 * @time 21:49
 * @project user_center
 **/


/**
 * 定义常量，通常使用接口定义；
 */
public interface UserContant {

    /**
     * 用户登录态定义
     */

    String USE_LOGIN_STATE ="userLoginState";

    /**
     * 角色定义：
     * 0 :普通用户
     * 1 :管理员
     */
    int DEFAULT_ROLE=0;
    int ADMIN_ROLE=1;

}
