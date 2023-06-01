package com.jiang.user_center.common;

import org.omg.CORBA.StringHolder;

import java.io.Serializable;

/**
 *
 * 通用返回类：
 * @author Lenovo
 * @date 2023/5/5
 * @time 22:05
 * @project user_center
 **/

public class BaseResponse<T> implements Serializable {

    private int code;
    private T data;
    private String message;
    private String desc;

    public BaseResponse() {
    }

    public BaseResponse(int code, T data, String message,String desc) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.desc=desc;
    }

    public BaseResponse(int code, T data, String message) {
        this(code,data,message,"");
    }


    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }


    public BaseResponse(Error_Code error_code){
        this(error_code.getCode(),null,error_code.getMessage(),error_code.getDesc());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
