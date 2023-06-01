package com.jiang.user_center.service.impl;
import java.util.Date;

import com.jiang.user_center.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Lenovo
 * @date 2023/4/16
 * @time 18:08
 * @project user_center
 **/
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;


    @Test
    public void testAddUser(){
        User user = new User();
        user.setId(0L);
        user.setUserName("jack");
        user.setUserAccount("123456");
        user.setAvatarUrl("https://ts1.cn.mm.bing.net/th/id/R-C.59a1c38d8b32cd555ef315c094b5f808?rik=4S%2bdRG0x9XZZxw&riu=http%3a%2f%2f222.186.12.239%3a10010%2flufei_20180903%2f004.jpg&ehk=v3kmsvRsXe%2briEwd1lFVGOLT%2fZIiZV2TUdNb%2b4H7kkw%3d&risl=&pid=ImgRaw&r=0");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123456789");
        user.setEmail("1635156042@qq.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);
        user.setPlanetCode("001");

        userService.save(user);
    }

    @Test
    public void testUserRegister(){
        String userAccount="jack";
        String userPassword="";
        String checkPassword="123456789";
        String planetCode="1";

        Long reslut = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("result:"+reslut);
        Assertions.assertEquals(-1,reslut);

        userAccount="yu";
        userPassword="123456789";
        Long accountResult = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("accountResult:"+accountResult);

        userAccount="yupi";
        userPassword="123";
        Long passwordResult = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("passwordResult:"+passwordResult);

        //校验特殊的字符：
        userAccount="yi@@@@pi";
        userPassword="123456789";
        Long validResult = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("validResult:"+validResult);


        userAccount="yupi";
        userPassword="1234567810";
        Long checkPasswordResult = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("checkPasswordResult:"+checkPasswordResult);

        //账户不能重复：

        userAccount="123456";//原始的账户名是123456
        userPassword="123456789";
        Long checkUserAccount = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("checkUserAccount:"+checkUserAccount);

        //真正插入成功：
        userAccount="yipi";
        userPassword="123456789";
        checkPassword="123456789";
        Long insertResult01 = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("插入的结果是；"+insertResult01);

        //真正插入成功2：
        userAccount="jack";
        userPassword="123456789";
        checkPassword="123456789";
        Long insertResult02= userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        System.out.println("插入的结果是；"+insertResult02);
    }

}