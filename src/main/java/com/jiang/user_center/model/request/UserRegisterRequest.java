package com.jiang.user_center.model.request;



/**
 * @author Lenovo
 * @date 2023/4/30
 * @time 9:47
 * @project user_center
 **/

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体 json格式
 */
public class UserRegisterRequest implements Serializable {

    private String userAccount;

    private String userPassword;
    private String checkPassword;
    private String planetCode;

    public UserRegisterRequest() {
    }

    public UserRegisterRequest(String userAccount, String userPassword, String checkPassword, String planetCode) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.checkPassword = checkPassword;
        this.planetCode = planetCode;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public String getPlanetCode() {
        return planetCode;
    }

    public void setPlanetCode(String planetCode) {
        this.planetCode = planetCode;
    }
}
