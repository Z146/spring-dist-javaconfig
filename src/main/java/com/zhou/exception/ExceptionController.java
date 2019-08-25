package com.zhou.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/*
异常通知类，会拦截异常并进行匹配 @ExceptionHandler 所标注的异常类型
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ArithmeticException.class)
    public String xxException(){
        return "home";
    }
}
