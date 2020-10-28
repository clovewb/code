package com.santu.leaves.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 13时18分25秒
 * @Version 1.0
 * @Description:
 */
@Slf4j
public class JwtUtil {

    //token过期时间
    private static final long EXPIRE_TIME = 1000 * 60 * 50 * 6;
    //redis中token过期时间
    public static final Integer REFRESH_EXPIRE_TIME = 1000 * 60 * 10 * 60 * 2;

    //token密钥(自定义)
    private static final String TOKEN_SECRET = "^/zxc*123!@#$%^&*/";

    /**
     * 校验token是否正确
     * @param token token
     * @param username 用户名
     * @return 是否正确
     */
    public static boolean verify(String token, String username){
        log.info("JwtUtil==verify--->");
        try {
            log.info("JwtUtil==verify--->校验token是否正确");
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    //.withClaim("secret",secret)
                    .build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            log.info("JwtUtil==verify--->jwt = "+jwt.toString());
            log.info("JwtUtil==verify--->JwtUtil验证token成功!");
            return true;
        }catch (Exception e){
            log.info("JwtUtil==verify--->JwtUtil验证token失败!");
            return false;
        }
    }

    /**
     * 获取token中的信息（包含用户名）
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        log.info("JwtUtil==getUsername--->");
        try {
            DecodedJWT jwt = JWT.decode(token);
            log.info("JwtUtil==getUsername--->username = "+jwt.getClaim("username").asString().toString());
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            log.error("JwtUtil==getUsername--->JWTDecodeException: " + e.getMessage());
            return null;
        }
    }

    /**
     * 生成token签名
     * EXPIRE_TIME 分钟后过期
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username) {
        log.info("JwtUtil==sign--->");
        Map<String,Object> header = new HashMap<>();
        header.put("type","Jwt");
        header.put("alg","HS256");
        long currentTimeMillis = System.currentTimeMillis();
        //设置token过期时间
        Date date = new Date(currentTimeMillis + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //生成签名
        String sign = JWT.create()
                .withHeader(header)
                .withClaim("username", username)
                .withClaim("currentTime", currentTimeMillis)
                .withExpiresAt(date)
                .sign(algorithm);
        log.info("JwtUtil==sign--->sign = " + sign);
        return sign;
    }


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        String username = "张三";
        //测试生成token
        String token = sign(username);
        System.out.println("测试-->生成的token：" + token);

        //验证token是否正确
        boolean verify = verify(token, username);
        System.out.println("测试-->校验token结果：" + verify);

        //获取信息
        String username1 = getUsername(token);
        System.out.println("测试-->根据token获取用户名：" + username1);
    }


}
