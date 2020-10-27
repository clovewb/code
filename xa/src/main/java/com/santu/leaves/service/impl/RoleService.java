package com.santu.leaves.service.impl;

import com.santu.leaves.mapper.TRoleMapper;
import com.santu.leaves.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
