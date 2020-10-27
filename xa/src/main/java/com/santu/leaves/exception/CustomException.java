package com.santu.leaves.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义异常(CustomException)
 * @Author LEAVES
 * @Date 2020/8/31
 * @Version 1.0
 */
public class CustomException extends AuthenticationException {

    public CustomException(String msg){
        super(msg);
    }

    public CustomException() {
        super();
    }
}
