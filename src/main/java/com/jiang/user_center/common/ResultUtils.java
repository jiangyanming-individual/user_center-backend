package com.jiang.user_center.common;

/**
 * 返回工具类 :主要是用于返回BaseResponse类；
 * @author Lenovo
 * @date 2023/5/6
 * @time 10:20
 * @project user_center
 **/
public class ResultUtils {
    /**
     * 处理信息成功的工具类
     * @param data
     * @param <T>
     * @return 返回一个BaseResponse的类；
     */
    public static <T>  BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"OK");
    }

    /**
     * 失败： 自定义传入 code message desc
     * @param code
     * @param message
     * @param desc
     * @return
     */
    public static  BaseResponse error(int code,String message,String desc){
        return new BaseResponse(code,null,message,desc);
    }

    /**
     * 失败；自定义传入message ;desc
     * @param error_code
     * @return
     */

    public static  BaseResponse error(Error_Code error_code,String message,String desc){
        return new BaseResponse(error_code.getCode(),null,message,desc);
    }
    /**
     * 自定义desc 传入描述
     * @param error_code
     * @param desc
     * @return
     */
    public static  BaseResponse error(Error_Code error_code,String desc){
        return new BaseResponse(error_code.getCode(),null,error_code.getMessage(),desc);
    }


}
