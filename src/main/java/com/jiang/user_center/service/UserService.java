package com.jiang.user_center.service;

import com.jiang.user_center.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiang.user_center.model.request.UserAddRequest;
import com.jiang.user_center.model.request.UserDeleteRequest;
import com.jiang.user_center.model.request.UserUpdateRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service
* @createDate 2023-04-16 17:57:46
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */

    Long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);



    User userLogin(String userAccount,String userPassword, HttpServletRequest request);

    User getSaftyUser(User OriginUser);

    User getCurrentUser(HttpServletRequest request);

    Integer userLogout(HttpServletRequest request);

    /**
     * 管理添加用户
     * @param request
     * @return
     */
    Long addUser(UserAddRequest userAddRequest, HttpServletRequest request);

    /**
     * 管理员删除一个用户
     * @param
     * @param request
     * @return
     */
    boolean deleteUser(UserDeleteRequest userDeleteRequest, HttpServletRequest request);

    /**
     * 管理员更新用户或者用户自己更新自己
     * @param
     * @param request
     * @return
     */
    boolean updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 是否为管理员:
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);
    boolean isAdmin(User loginUser);
}
