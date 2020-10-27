package com.santu.leaves.service.impl;

import com.santu.leaves.common.utils.MD5Util;
import com.santu.leaves.config.reids.JedisUtil;
import com.santu.leaves.entity.TRole;
import com.santu.leaves.entity.TUser;
import com.santu.leaves.jwt.JwtUtil;
import com.santu.leaves.mapper.TRoleMapper;
import com.santu.leaves.mapper.TUserMapper;
import com.santu.leaves.response.ResponseData;
import com.santu.leaves.response.ResponseDataUtil;
import com.santu.leaves.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 15时00分32秒
 * @Version 1.0
 * @Description:
 */
@Slf4j
@Service
public class UserService implements IUserService {

    @Resource
    private TUserMapper userMapper;

    @Resource
    private TRoleMapper roleMapper;

    /**
     * 权限认证
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public TUser getTUserByusername(String username) {
        TUser tUser = userMapper.selectByusername(username);
        log.info("权限认证===>根据用户名查询用户信息：" + tUser.toString());
        return tUser;
    }

    @Override
    public ResponseData login(String username, String password, HttpServletResponse response) {
        TUser tUser = userMapper.selectByusername(username);
//        Set<String> roleByUserName = tRoleService.getRoleByUserName(username);
        //由于前端+controller两处对username和password进行了判断，所以此处不再做判断
        if (tUser != null) {
            //解密数据库中用户密码
            String dbpwd = MD5Util.convertMD5(tUser.getPassword());
            TRole tRole = roleMapper.selectByPrimaryKey(tUser.getRoleid());
            tUser.setRole(tRole);
            if(tUser.getUsername().equals(username) && dbpwd.equals(password)){
                String token = JwtUtil.sign(tUser.getUsername());
                log.info("登录时生成的token = " + token);
                long currentTimeMillis = System.currentTimeMillis();
                //向redis中存入token
                JedisUtil.setJson(tUser.getUsername(), token, JwtUtil.REFRESH_EXPIRE_TIME);
                //redisUtil.set(users.getUsername(),token,JwtUtil.REFRESH_EXPIRE_TIME);
                //redisUtil.set(users.getUsername(),token,JwtUtil.REFRESH_EXPIRE_TIME);
                //redisUtil.set(users.getUsername(),users);
                //向前端返回token
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("token", token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "token");
                log.info("登录成功：" + tUser.toString());
                tUser.setPassword(dbpwd);
                return ResponseDataUtil.success("登录成功", tUser, token);
            }else {
                return ResponseDataUtil.fail("登录失败，密码错误！");
            }

        }
        return ResponseDataUtil.fail("登录失败，用户不存在！");
    }

    @Override
    public ResponseData logout(HttpServletRequest request) {
        return null;
    }
}
