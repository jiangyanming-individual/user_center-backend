package com.jiang.user_center.model.request;

import java.io.Serializable;

/**
 * @author Lenovo
 * @date 2023/4/30
 * @time 10:01
 * @project user_center
 **/
public class UserLoginRequest implements Serializable {

    private String userAccount;
    private String userPassword;

    public UserLoginRequest(){

    }
    public UserLoginRequest(String userAccount, String userPassword) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
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
}
