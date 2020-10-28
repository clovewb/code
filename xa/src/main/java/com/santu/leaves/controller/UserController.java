package com.santu.leaves.controller;

import com.santu.leaves.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 17时33分01秒
 * @Version 1.0
 * @Description:
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Object logi(HttpServletRequest request, HttpServletResponse response){
        return userService.queryAll(request, response);
    }
}
