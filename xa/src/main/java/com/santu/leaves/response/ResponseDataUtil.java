package com.santu.leaves.response;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @Author: LEAVES
 * @Date: 2020年10月27日 16时44分28秒
 * @Version 1.0
 * @Description:
 */
@Slf4j
public class ResponseDataUtil {

    /**
     * 返回成功的描述      状态码、说明
     * @param msg
     * @return
     */
    public static ResponseData success(String msg){
        ResponseData responseData = new ResponseData();
        responseData.setCode(200);
        responseData.setMsg(msg);
        return responseData;
    }

    /**
     * 返回成功的描述      状态码、说明、数据
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseData success(String msg, T data){
        ResponseData responseData = new ResponseData();
        responseData.setCode(200);
        responseData.setMsg(msg);
        responseData.setData(data);
        return responseData;
    }

    /**
     * 返回成功的描述      状态码、说明、数据、令牌
     * @param msg
     * @param data
     * @param token
     * @param <T>
     * @return
     */
    public static <T> ResponseData success(String msg, T data, String token){
        ResponseData responseData = new ResponseData();
        responseData.setCode(200);
        responseData.setMsg(msg);
        responseData.setData(data);
        responseData.setToken(token);
        return responseData;
    }

    /**
     * 返回失败的描述  状态码
     * @param msg
     * @return
     */
    public static ResponseData fail(String msg){
        ResponseData responseData=new ResponseData();
        responseData.setCode(405);
        responseData.setMsg(msg);
        return responseData;
    }

    /**
     * 返回失败的描述  状态码、说明
     * @param code
     * @param msg
     * @return
     */
    public static ResponseData fail(Integer code, String msg){
        ResponseData responseData=new ResponseData();
        responseData.setCode(code);
        responseData.setMsg(msg);
        return responseData;
    }


}
