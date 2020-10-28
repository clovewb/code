package com.santu.leaves.response;

import lombok.Data;

/**
 * 统一返回格式
 * @Author: LEAVES
 * @Date: 2020年10月27日 16时43分08秒
 * @Version 1.0
 * @Description:
 */
@Data
public class ResponseData<T> {

    /**
     * 统一返回码
     */
    public Integer code;

    /**
     * 统一消息
     */
    public String msg;

    /**
     * token令牌
     */
    public String token;

    /**
     * 结果对象
     */
    public T data;



}
