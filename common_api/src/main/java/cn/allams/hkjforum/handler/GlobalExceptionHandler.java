package cn.allams.hkjforum.handler;

import cn.allams.hkjforum.entity.CommonResult;
import cn.allams.hkjforum.entity.HkjforumException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 *
 * @author Allams
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult error(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return CommonResult.error();
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public CommonResult error(ArithmeticException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return CommonResult.error().message("算术错误");
    }

    @ExceptionHandler(HkjforumException.class)
    @ResponseBody
    public CommonResult error(HkjforumException e){
        e.printStackTrace();
        return CommonResult.error().message(e.getMessage()).code(e.getCode());
    }
}
