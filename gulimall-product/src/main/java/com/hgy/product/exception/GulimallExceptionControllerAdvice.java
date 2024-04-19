package com.hgy.product.exception;

import com.hgy.common.exception.BizCodeEnume;
import com.hgy.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理,所有(basePackages = "com.hgy.product.controller")这个位置下的异常都是它处理
 *
 * @author hgy
 * @Description
 * @created 2024/4/11 22:06
 */
@Slf4j
//@ResponseBody  //所有异常都是一JSON方式写出去
//@ControllerAdvice(basePackages = "com.hgy.product.controller")
@RestControllerAdvice(basePackages = "com.hgy.product.controller")
public class GulimallExceptionControllerAdvice {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)  //感知异常，可以处理特定的异常(精确匹配)
    public R handValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{},异常类型{}", e.getMessage(), e.getClass());

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", errorMap);
    }

    @ExceptionHandler(value = Throwable.class) //感知异常，可以处理任意所有异常(模糊匹配，异常先进入精确匹配，若没有，才进行这个异常处理)
    public R handleException(Throwable e) {

        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
