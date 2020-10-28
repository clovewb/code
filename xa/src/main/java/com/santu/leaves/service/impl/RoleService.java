package com.santu.leaves.service.impl;

import cn.hutool.core.util.StrUtil;
import com.santu.leaves.common.utils.TokenUtils;
import com.santu.leaves.entity.TRole;
import com.santu.leaves.mapper.TRoleMapper;
import com.santu.leaves.response.ResponseData;
import com.santu.leaves.response.ResponseDataUtil;
import com.santu.leaves.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 14时50分45秒
 * @Version 1.0
 * @Description: 角色业务实现
 */
@Service
@Slf4j
public class RoleService implements IRoleService {

    @Resource
    private TRoleMapper roleMapper;

    /**
     * 权限鉴别
     * 根据用户名查询用户权限
     * @param username
     * @return
     */
    @Override
    public Set<String> getPermissionByUserName(String username) {
        List<String> permissions = roleMapper.getPermissionByUserName(username);
        log.info("权限鉴别===>通过用户名获取用户角色信息：" + permissions.toString());
        return new HashSet<String>(permissions);
    }

    /**
     * 查询全部角色
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseData queryAll(HttpServletRequest request, HttpServletResponse response) {
        String token = TokenUtils.verifyrefreshToken(request, response);
        if (StrUtil.isNotBlank(token)){
            List<TRole> tRoles = roleMapper.selectAll();
            if (!tRoles.isEmpty()){
                return ResponseDataUtil.success("查询成功！", tRoles, token);
            }
            return ResponseDataUtil.fail("查询失败，暂无角色数据！");
        }
        return ResponseDataUtil.fail("token已失效，请重新登录！");
    }
}
