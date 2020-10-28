package com.santu.leaves.controller;

import com.santu.leaves.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LEAVES
 * @Date: 2020年10月28日 11时29分02秒
 * @Version 1.0
 * @Description:
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 查询全部角色
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @RequiresPermissions(value={"1","100"},logical = Logical.OR)
    public Object queryAll(HttpServletRequest request, HttpServletResponse response){
        return roleService.queryAll(request, response);
    }

}
