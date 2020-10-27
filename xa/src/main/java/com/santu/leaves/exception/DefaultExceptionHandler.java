package com.santu.leaves.exception;


import com.santu.leaves.response.ResponseData;
import com.santu.leaves.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DefaultExceptionHandler:自定义异常处理器
 *
 * @Author LEAVES
 * @Date 2020/8/18 13:58
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice  //不指定包默认加了@Controller和@RestController都能控制
//@ControllerAdvice(basePackages ="com.leaves.controller")
public class DefaultExceptionHandler {

    /**
     * @param  ex
     * @Description: 运行时异常
     */
    @ExceptionHandler(CustomException.class)
    public ResponseData CustomExceptionHandler(CustomException ex) {
        log.error(ex.getMessage(), ex);
        ResponseData responseData=new ResponseData<>();
        responseData.setCode(400);
        responseData.setMsg(ex.getMessage());
        return responseData;
    }
//
    /**
     * @param  ex
     * @Description: 权限认证异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseData unauthorizedExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseDataUtil.fail("对不起，您没有相关权限！");
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public ResponseData authorizationException(AuthorizationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseDataUtil.fail("无效token,请重新登录！");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseData authenticationException(AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseDataUtil.fail("请先重新登录！");
    }

    /**
     * 异常统一自定义处理
     */
//    @ExceptionHandler({MyException.class})
//    @ResponseBody
//    public ResponseData MyException(MyException e) {
//        return ResponseDataUtil.failure(500,e.getMessage());
//    }
    /**
     * 异常统一处理(最后的异常处理)
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseData allException(Exception e) {
        log.info(e.getMessage());
        return ResponseDataUtil.fail(500,"系统异常！");
    }


}
