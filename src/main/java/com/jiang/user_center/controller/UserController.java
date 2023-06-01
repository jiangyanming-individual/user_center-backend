package com.jiang.user_center.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiang.user_center.common.BaseResponse;
import com.jiang.user_center.common.Error_Code;
import com.jiang.user_center.common.ResultUtils;
import com.jiang.user_center.exception.BusinessException;
import com.jiang.user_center.model.domain.User;
import com.jiang.user_center.model.request.UserAddRequest;
import com.jiang.user_center.model.request.UserLoginRequest;
import com.jiang.user_center.model.request.UserRegisterRequest;
import com.jiang.user_center.model.request.UserUpdateRequest;
import com.jiang.user_center.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 * @date 2023/4/17
 * @time 20:43
 * @project user_center
 **/

/**
 * 用户登录的接口：
 * 返回数据类型是json类型的数据：
 */


@RestController //返回的数据都是json类型的数据；
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){

        //如果请求体为空，直接返回为空：
        if (userRegisterRequest == null){
            throw new BusinessException(Error_Code.PARAM_ERROR);
        }

        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getUserPassword();
        String checkPassword=userRegisterRequest.getCheckPassword();
        String planetCode=userRegisterRequest.getPlanetCode();
        //如果有一个为空的话，就直接返回null; Controller层进行校验不涉及逻辑层；
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(Error_Code.NULL_ERROR,"请求参数不能为空！");
        }

        //如果都符合进行逻辑层校验：
        Long register = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(register);
    }

    //HttpServletRequest 是第三方的包，用来处理javaweb中的请求问题的： HttpRequest：是java自带的包

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){

        //如果请求体为空，直接返回为空：
        if (userLoginRequest == null){
            throw new BusinessException(Error_Code.PARAM_ERROR,"登录参数不能未空！");
        }

        String userAccount=userLoginRequest.getUserAccount();
        String userPassword=userLoginRequest.getUserPassword();

        //如果有一个为空的话，就直接返回null; Controller层进行校验不涉及逻辑层；
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
           throw new BusinessException(Error_Code.NULL_ERROR,"请求参数为空");
        }

        //如果都符合进行逻辑层校验：
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }


    /**
     * 用户注销：移除session
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(Error_Code.PARAM_ERROR,"请求参数不能为空！");
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
    /**
     * 获取当前用户的信息：
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){

        User currentUser = userService.getCurrentUser(request);
        return ResultUtils.success(currentUser);
    }

    /**
     * 查找和删除用户是只有管理员才可以调用；
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        /**
         * 管理员查询
         * @param username 用户昵称
         * @param request springboot内置请求对象，用于存储用户session
         */
        if (!isAdmin(request)){
//            return new ArrayList<>();
           throw new BusinessException(Error_Code.NO_AUTH,"不是管理员！无权限");
        }

        /**
         * 如果是管理员的话 ，直接查询：用户
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            //数据库中查找相同名字的数据
            queryWrapper.like("userName",username);
        }

        List<User> userList = userService.list(queryWrapper);//Mybatis-plus的语法接口
        //进行用户脱敏：然后再转为List集合:
        List<User> list = userList.stream().map(user -> userService.getSaftyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(Error_Code.NO_AUTH,"不是管理员！");
        }
        if (id <=0){
            throw new BusinessException(Error_Code.PARAM_ERROR,"没有该用户！");
        }

        System.out.println(id);
        boolean b = userService.removeById(id);//删除用户
        return ResultUtils.success(b);
    }

    //发送post请求:
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest,HttpServletRequest request){

        if (userAddRequest == null){
            throw new BusinessException(Error_Code.PARAM_ERROR);
        }
        Long id = userService.addUser(userAddRequest, request);
        if (id == null || id <=0){
            throw new BusinessException(Error_Code.SYSTEM_ERROR,"添加用户失败");
        }
        return ResultUtils.success(id);
    }

    /**
     * 管理员或者当前用户修改自己的信息:
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,HttpServletRequest request){
        if (userUpdateRequest == null){
            throw new BusinessException(Error_Code.PARAM_ERROR);
        }
        //返回校验用户的信息:
        boolean result = userService.updateUser(userUpdateRequest, request);
        return ResultUtils.success(result);
    }
    /**
     * 根据请求判断是否为管理员：
     */
    public Boolean isAdmin(HttpServletRequest request){
        /**
         * 鉴权： 仅管理员可以查看
         */
        Boolean result = userService.isAdmin(request);
        if (!result){
            throw new BusinessException(Error_Code.NO_AUTH,"不是管理员没有权限");
        }
        return result;
    }

}
