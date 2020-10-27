package com.santu.leaves.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 14时31分15秒
 * @Version 1.0
 * @Description:    Authentication-Token： shiro中负责把username,password生成用于验证的token的封装类，需要自定义一个对象用来包装token。
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
