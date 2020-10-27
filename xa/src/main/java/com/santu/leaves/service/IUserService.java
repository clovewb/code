package com.santu.leaves.service;

import com.santu.leaves.entity.TUser;
import com.santu.leaves.response.ResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 15时00分25秒
 * @Version 1.0
 * @Description:
 */
public interface IUserService {

    TUser getTUserByusername(String username);

    ResponseData login(String username, String password, HttpServletResponse response);

    ResponseData logout(HttpServletRequest request);
}
