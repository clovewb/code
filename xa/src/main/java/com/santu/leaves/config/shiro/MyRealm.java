package com.santu.leaves.config.shiro;

import cn.hutool.core.util.StrUtil;
import com.santu.leaves.entity.TUser;
import com.santu.leaves.jwt.JwtToken;
import com.santu.leaves.jwt.JwtUtil;
import com.santu.leaves.service.IRoleService;
import com.santu.leaves.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.Version;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 14时44分13秒
 * @Version 1.0
 * @Description:
 */
@Slf4j
@Component
public class MyRealm extends AuthorizingRealm {

    @Resource
    private IRoleService roleService;

    @Resource
    private IUserService userService;


    /**
     * 必须重写此方法，否则Shiro会报错
     * @param token
     * @return
     */
    @Version
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthorizationException {
        log.info("MyRealm==doGetAuthorizationInfo--->");
        //通过调用JwtUtil.getUsername()方法得到token中的username
        String username = JwtUtil.getUsername(principals.toString());
        if (StrUtil.isBlank(username)){
            throw new AuthorizationException("无效token,请重新登录！");
        }
        //调用业务方法获取用户的角色
        Set<String> permissions = roleService.getPermissionByUserName(username);
        Set<String> permissionSet = new HashSet<>(permissions);
        //String role = userService.getRole(username);
        //调用业务方法获取用户权限
        //List<Permissions> list = roleService.getPermissionsByUsername(username);
        //每个用户可以设置新的权限
        //String permission = userService.getPermission(username);
        //将List换成set去掉重复权限
        //Set<String> stringPermissions = new HashSet<>();
        //Set<String> roleSet = new HashSet<>();
//        if (list !=null){
//            for (Permissions permissions : list){
//                log.info(username + "拥有的权限有：" + permissions);
//                stringPermissions.add(permissions.getPername());
//            }
//        }
        //授权
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //设置该用户拥有的角色和权限
//        authorizationInfo.setRoles(roleSet);
        authorizationInfo.setStringPermissions(permissionSet);
        //authorizationInfo.setStringPermissions(stringPermissions);
        return authorizationInfo;
    }

    /**
     * 认证
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        log.info("MyRealm==doGetAuthenticationInfo--->");
        //获取用户token信息
        String token = (String) auth.getPrincipal();
        String accessToken = (String) auth.getCredentials();
        // 帐号为空
        if (StrUtil.isBlank(token)) {
            throw new AuthenticationException("暂无token!");
        }
       // token = token.substring(7);
        log.info("MyRealm==doGetAuthenticationInfo--->token = " + token);
        //判断token中是否包含用户信息
        String username = null;
        try {
            //这里工具类没有处理空指针等异常这里处理一下(这里处理科学一些)
            //解密获得username，用于和数据库进行对比
            username = JwtUtil.getUsername(token);

            log.info("MyRealm==doGetAuthenticationInfo--->从token中解析出的username = " + username);
        } catch (Exception e) {
            log.info("MyRealm==doGetAuthenticationInfo--->AuthenticationException：token拼写错误或者值为空！");
            throw new AuthenticationException("token拼写错误或者值为空");
        }
        if (username == null) {
            log.error("MyRealm==doGetAuthenticationInfo--->token无效(空''或者null都不行!)");
            throw new AuthenticationException("认证失败，token无效或token中未包含用户信息！");
        }
        //根据用户信息查询数据库获取后端的用户身份，转交给securityManager判定
        //调用业务方法从数据库中获取用户信息
        TUser tUser = userService.getTUserByusername(username);
        //判断从数据库中获取用户信息是否为空
        if (tUser == null) {
            log.error("MyRealm==doGetAuthenticationInfo--->用户不存在!)");
            throw new AuthenticationException("用户" + username + "不存在!");
        }
        //校验用户的token、username、password
        if (!JwtUtil.verify(token, tUser.getUsername())) {
            log.error("MyRealm==doGetAuthenticationInfo--->用户名或密码错误(token无效或者与登录者不匹配)!)");
            throw new AuthenticationException("用户名错误(token无效或者与登录者不匹配)!");
        }
        //认证
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
