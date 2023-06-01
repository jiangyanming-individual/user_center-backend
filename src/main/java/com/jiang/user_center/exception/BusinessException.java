package com.jiang.user_center.exception;

import com.jiang.user_center.common.Error_Code;

/**
 *
 * 定义业务异常
 */
public class BusinessException extends RuntimeException{

    private final int code;
    private final String desc;

    public BusinessException(String message, int code, String desc) {
        super(message);
        this.code = code;
        this.desc = desc;
    }

    /**
     * 传入error_code错误码 自定义错误code和desc;
     * @param error_code
     */
    public BusinessException(Error_Code error_code) {
        super(error_code.getMessage());
        this.code = error_code.getCode();
        this.desc = error_code.getDesc();
    }

    public BusinessException(Error_Code error_code,String desc) {
        /**
         * desc ：自己自定义传入desc
         */
        super(error_code.getMessage()); //父类的异常；
        this.code = error_code.getCode();
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}


