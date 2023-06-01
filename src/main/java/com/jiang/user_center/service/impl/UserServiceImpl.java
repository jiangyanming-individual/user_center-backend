package com.jiang.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiang.user_center.common.Error_Code;
import com.jiang.user_center.exception.BusinessException;
import com.jiang.user_center.model.domain.User;
import com.jiang.user_center.model.request.UserAddRequest;
import com.jiang.user_center.model.request.UserDeleteRequest;
import com.jiang.user_center.model.request.UserUpdateRequest;
import com.jiang.user_center.service.UserService;
import com.jiang.user_center.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jiang.user_center.contant.UserContant.ADMIN_ROLE;
import static com.jiang.user_center.contant.UserContant.USE_LOGIN_STATE;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-04-16 17:57:46
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    @Resource
    private UserMapper userMapper;

    private static final String SALT="jack"; //自己设置的断言；用于混淆加密；

    /**
     * 用户登录状态 ：session;
     */
    public Long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode){
        //(1)权限校验：
        //1.非空

        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(Error_Code.PARAM_ERROR,"参数为空！");
        }

        //2.账户长度不小于4位
        if (userAccount.length()<4){
            throw new BusinessException(Error_Code.PARAM_ERROR,"账户长度过短！");

        }

        //3.密码长度不小于8位
        if (userPassword.length()<8){
            throw new BusinessException(Error_Code.PARAM_ERROR,"密码长度小于8位！");
        }
        if (planetCode.length()>5){
            //不能大于五
            throw new BusinessException(Error_Code.PARAM_ERROR,"星球编号长度过长！");
        }

        //4.账户不包含特殊的符号；特殊字符的正则表达式
        String validRule="[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……\n" +
                "&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(Error_Code.PARAM_ERROR,"不能含有特殊字符！");
        }

        //5. 密码和校验密码不相同；
        if (!userPassword.equals(checkPassword)){
           throw new BusinessException(Error_Code.PARAM_ERROR,"密码和校验密码不相同");
        }

        //6.账户不能重复；在数据库中查询账户

        /**
         * QueryWrapper ：mybatis-plus中使用的语法；
         * **/
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(Error_Code.PARAM_ERROR,"该用户名已经被使用！");
        }

        // 7.星球编号不能重复：
        queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
        count=userMapper.selectCount(queryWrapper);//查询到一条数据；
        if (count>0){
            throw new BusinessException(Error_Code.PARAM_ERROR,"星球编号已经存在！");
        }

        //(2) 对密码进行加密;验证密码；
        String verifyPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //(3) 向数据库中插入数据：
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(verifyPassword);//插入加密后的密码；
        user.setPlanetCode(planetCode); //插入星球编号；
        int result = userMapper.insert(user);

        if (result<0){
            throw new BusinessException(Error_Code.PARAM_ERROR,"插入数据失败！");
        }
        return user.getId();//返回插入用户后的id；
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 1.校验账户和密码是否合法
            //1. 非空
            if (StringUtils.isAnyBlank(userAccount,userPassword)){
                throw new BusinessException(Error_Code.NULL_ERROR,"用户名和密码不能为空！");
            }

            //2. 账户长度 不小于 4 位
            if (userAccount.length()<4){
                throw new BusinessException(Error_Code.PARAM_ERROR,"账户长度不能小于4！");
            }
            //3. 密码就 不小于 8 位
            if (userPassword.length() <8){
                throw new BusinessException(Error_Code.PARAM_ERROR,"密码长度不能小于8！");
            }
            //4. 账户不包含特殊字符
            String validRule="[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……\n" +
                "&*（）——+|{}【】‘；：”“’。，、？]";

            Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
            if (matcher.find()){
                throw new BusinessException(Error_Code.PARAM_ERROR,"账户不能含有特殊字符！");
            }

        //2. 校验密码是否输入正确 与数据库中的密文进行对比 先对密码进行加密
        String encoderPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //查询账户和密码；对比数据信息：
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encoderPassword);

        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("user login failed, userAccount Cannot match userPassword");
            throw new BusinessException(Error_Code.PARAM_ERROR,"该用户不存在！");
        }

        // 3.用户信息脱敏，新建一个User然后将前端需要的数据返回
        User saftyUser = getSaftyUser(user);

        // 4.记录登录的状态session;  USE_LOGIN_STATE 登录态常量；
        request.getSession().setAttribute(USE_LOGIN_STATE,saftyUser);

        // 5. 返回脱敏后的用户信息
        return saftyUser;
    }

    /**
     * 用户信息脱敏：
     * @param OriginUser
     * @return
     */
    @Override
    public User getSaftyUser(User OriginUser){
        if (OriginUser == null){
            return null;
        }
        User saftyUser = new User();
        saftyUser.setId(OriginUser.getId());
        saftyUser.setUserName(OriginUser.getUserName());
        saftyUser.setUserAccount(OriginUser.getUserAccount());
        saftyUser.setAvatarUrl(OriginUser.getAvatarUrl());
        saftyUser.setGender(OriginUser.getGender());
        saftyUser.setPhone(OriginUser.getPhone());
        saftyUser.setEmail(OriginUser.getEmail());
        saftyUser.setUserStatus(OriginUser.getUserStatus());
        saftyUser.setCreateTime(OriginUser.getCreateTime());
        saftyUser.setUserRole(OriginUser.getUserRole());
        saftyUser.setPlanetCode(OriginUser.getPlanetCode()); //返回星球编号；

        return saftyUser;
    }

    /**
     * 用户注销的功能：
     * @param request
     * @return
     */
    @Override
    public Integer userLogout(HttpServletRequest request){

        request.getSession().removeAttribute(USE_LOGIN_STATE);
        return 1;
    }

    /**
     * 获取当前用户
     * @param request
     * @return
     */
    @Override
    public User getCurrentUser(HttpServletRequest request){

        Object userObj = request.getSession().getAttribute(USE_LOGIN_STATE);
        User user=(User) userObj;

        if (user == null){
            throw new BusinessException(Error_Code.NOT_LOGIN,"该用户未登录！");
        }

        // TODO 校验用户是否合法：
        Long userId=user.getId();
        User newUser = userMapper.selectById(userId);
        return getSaftyUser(newUser);//用户数据脱敏；
    }


    /**
     * 管理员新插入用户:
     * @param userAddRequest
     * @param request
     * @return
     */
    @Override
    public Long addUser(UserAddRequest userAddRequest, HttpServletRequest request) {

        if (userAddRequest == null){
            throw new BusinessException(Error_Code.PARAM_ERROR);
        }
        boolean result = isAdmin(request);
        if (!result){
            throw new BusinessException(Error_Code.NO_AUTH,"不是管理员");
        }

        String userAccount = userAddRequest.getUserAccount();
        String planetCode = userAddRequest.getPlanetCode();
        //账号和星球编号不能为空:
        if (StringUtils.isAnyBlank(userAccount,planetCode)){
            throw new BusinessException(Error_Code.PARAM_ERROR,"账号和星球编号不能为空!");
        }

        // 1. 校验用户账户长度:
        if (userAccount.length()<4){
            throw new BusinessException(Error_Code.PARAM_ERROR,"账户长度不符合要求");
        }

        //2 校验用户账户是否有特殊字符:
        String validRule="[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……\n" +
                "&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(Error_Code.PARAM_ERROR,"账号含有特殊字符");
        }
        //3.校验星球编号长度;

        if (planetCode.length()>5){
            throw new BusinessException(Error_Code.PARAM_ERROR,"星球编号长于5位");
        }
        //4 查看用户账户是否重复:
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count>0){
            throw new BusinessException(Error_Code.PARAM_ERROR,"该账户已经存在!");
        }

        //5.查看星球编号是否重复
        queryWrapper.eq("planetCode",planetCode);
        Long planetCount = userMapper.selectCount(queryWrapper);
        if (planetCount>0){
            throw new BusinessException(Error_Code.PARAM_ERROR,"星球编号已经存在!");
        }

        User user = new User();
        BeanUtils.copyProperties(userAddRequest,user);
        long id = userMapper.insert(user);
        //返回id
        return id;
    }

    /**
     * 管理员根据用户id删除用户
     * @param userDeleteRequest
     * @param request
     * @return
     */
    @Override
    public boolean deleteUser(UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        if (userDeleteRequest == null){
            throw new BusinessException(Error_Code.PARAM_ERROR);
        }
        Long id = userDeleteRequest.getId();
        if (id == null || id <=0){
            throw new BusinessException(Error_Code.PARAM_ERROR,"用户id不存在");
        }
        boolean result = isAdmin(request);
        if (!result){
            throw new BusinessException(Error_Code.NO_AUTH,"不是管理员");
        }
        int i = userMapper.deleteById(id);
        //如果删除成功: i>0;
        if (i>0){
            return true;
        }
        return false;
    }

    /**
     * 管理员和用户自己修改自己的信息:
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @Override
    public boolean updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null){
            throw new BusinessException(Error_Code.PARAM_ERROR);
        }
        String userAccount = userUpdateRequest.getUserAccount();
        String userPassword = userUpdateRequest.getUserPassword();
        Long userId = userUpdateRequest.getId();
        //(1)判断是不是管理员:
        boolean result= isAdmin(request);
        User currentUser = getCurrentUser(request);
        //(2). 如果不是管理员而且不是当前用户自己:
        if (!result && !currentUser.getId().equals(userId)){
            throw new BusinessException(Error_Code.NO_AUTH,"不是管理员或者不是当前用户");
        }
        //(3). 校验更新的账户长度:
        if (userAccount.length()<4){
            throw new BusinessException(Error_Code.PARAM_ERROR,"账户长度不符合要求");
        }
        // (4).校验更新账户是否有特殊字符
        String validRule="[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……\n" +
                "&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(Error_Code.PARAM_ERROR,"账户含有特殊字符");
        }
        //(5).校验密码是否小于8位: 而且必要需要加密
        if (userPassword.length()<8){
            throw new BusinessException(Error_Code.PARAM_ERROR,"密码长度不能小于8位");
        }
        //对密码进行加密:
        String encodePassword=DigestUtils.md5DigestAsHex((SALT +userPassword).getBytes(StandardCharsets.UTF_8));
        //(6).校验是否存在相同的账户:
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        User olderUser = userMapper.selectOne(queryWrapper);
        //判断账户是否已经存在:
        if (olderUser!=null && olderUser.getId()!=userId){
            throw new BusinessException(Error_Code.PARAM_ERROR,"该用户已经存在");
        }
        User user = new User();
        //对密码进行加密的操作:
        userUpdateRequest.setUserPassword(encodePassword);
        BeanUtils.copyProperties(userUpdateRequest,user);
        //更新用户:
        int i = userMapper.updateById(user);
        if (i >0){
            return true;
        }
        return false;
    }

    /**
     * 是否是管理员
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {

        if (request == null){
            throw  new BusinessException(Error_Code.PARAM_ERROR,"请求参数异常");
        }
        //拿到session中的信息:
        Object userObject = request.getSession().getAttribute(USE_LOGIN_STATE);
        User user=(User) userObject;
        //如果不是管理员：
        if (user == null || user.getUserRole() !=ADMIN_ROLE){
            return false;
        }
        return true;
    }

    /**
     * 根据当前用户判断是不是管理员
     * @param loginUser
     * @return
     */
    @Override
    public boolean isAdmin(User loginUser) {

        if (loginUser == null){
            throw  new BusinessException(Error_Code.PARAM_ERROR);
        }
        if (loginUser.getUserRole()!=ADMIN_ROLE){
            return false;
        }
        return true;
    }


}




