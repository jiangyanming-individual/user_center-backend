package com.jiang.user_center.exception;

/**
 * @author Lenovo
 * @date 2023/5/6
 * @time 11:45
 * @project user_center
 **/

import com.jiang.user_center.common.BaseResponse;
import com.jiang.user_center.common.Error_Code;
import com.jiang.user_center.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 使用spring的APO切面；
 * 捕获全局的异常；内部消化，集中捕获异常。
 */
@RestControllerAdvice //APO切面；
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
         //获取异常code ,message,desc
         log.error("BusinessException:"+e.getMessage(),e);
         return ResultUtils.error(e.getCode(),e.getMessage(),e.getDesc());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException:",e);
        return ResultUtils.error(Error_Code.SYSTEM_ERROR,e.getMessage(),"");
    }

}
