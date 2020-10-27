package com.santu.leaves.controller;

import cn.hutool.core.util.StrUtil;
import com.santu.leaves.response.ResponseDataUtil;
import com.santu.leaves.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录接口
 * @Author LEAVES
 * @Date 2020/8/31 20:43
 * @Version 1.0
 */

@Slf4j
@RestController
@CrossOrigin
public class LoginController {

    @Resource
    private IUserService userService;

    /**
     * 登录
     * @param username
     * @param password
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password", required = false) String password, HttpServletResponse response){
//        log.info(username + "  *** " + password);
        //用户名和密码非空判断
        if (StrUtil.isNotBlank(username) && StrUtil.isNotBlank(password)){
            return userService.login(username, password, response);
        } else {
            log.info("******");
            return ResponseDataUtil.fail("登录失败，用户名或密码为空！");

        }

    }


}
