package com.jiang.user_center.common;

/**
 * @author Lenovo
 * @date 2023/5/6
 * @time 10:45
 * @project user_center
 **/
public enum Error_Code {

    /**
     * 定义枚举
     */
    SUCCESS(0,"OK",""),
    PARAM_ERROR(40000,"请求参数异常",""),
    NULL_ERROR(40001,"请求参数为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"没有权限",""),
    SYSTEM_ERROR(50000,"系统内部异常","");

    /**
     * 状态码
     */
    private final int code;
    /**
     *信息
     */
    private final String message;
    /**
     * 详细描述：
     */
    private final String desc;

    Error_Code(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }
}
