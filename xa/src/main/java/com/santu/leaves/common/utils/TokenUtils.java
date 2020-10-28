package com.santu.leaves.common.utils;

import com.santu.leaves.config.reids.JedisUtil;
import com.santu.leaves.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证和刷新token
 * @Author LEAVES
 * @Date 2020/8/20
 * @Version 1.0
 */
@Slf4j
public class TokenUtils {
    /**
     * 验证token并刷新token
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public static String verifyrefreshToken(ServletRequest request, ServletResponse response){
        //验证请求中的token是否正确
        if (verifyToken(request)){
            log.info("token验证通过");
            try {
                //返回生成新的token
                return refreshToken(request,response);
            } catch (Exception e) {
                log.info("验证刷新token时出现异常！");
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 验证请求中的token
     * @param request
     * @return
     */
    public static boolean verifyToken(ServletRequest request){
        //获取请求中的token
        String reqToken = ((HttpServletRequest)request).getHeader("Authorization");
        //去掉token前缀
        reqToken = reqToken.substring(7);
        //获取页面请求中的token所带的用户信息
        String accounts = JwtUtil.getUsername(reqToken);
        //验证token
        if (JwtUtil.verify(reqToken,accounts)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成新token
     * @param request
     * @return
     */
    public static String sign(ServletRequest request){
        //获取请求中的token
        String reqToken = ((HttpServletRequest)request).getHeader("Authorization");
        //去掉token前缀
        reqToken = reqToken.substring(7);
        //获取页面请求中的token所带的用户信息
        String accounts = JwtUtil.getUsername(reqToken);
        //生成新的token
        String newToken = JwtUtil.sign(accounts);
        log.info("更新的token：" + newToken);
        return newToken;
    }


    /**
     * refreshToken方法中,只有当redisToken不为空并且验证通过时才返回true,并刷新token,其他情况都返回false;
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public static String refreshToken(ServletRequest request, ServletResponse response) throws Exception {
        try{
            log.info("执行refreshTokens()方法,request="+request+",response="+response);
            //获取页面请求中的token
            String reqToken = ((HttpServletRequest)request).getHeader("Authorization");
            //去掉token前缀
            reqToken = reqToken.substring(7);
            log.info("页面请求中的token = " + reqToken);
            //获取页面请求中的token所带的用户信息
            String accounts = JwtUtil.getUsername(reqToken);
            log.info("页面请求中的account = " + accounts);
            //获取redis中的token
            String redisToken = JedisUtil.getJson(accounts);
            log.info("redis中的token是：" + redisToken);
            //生成新的token
            String newToken = JwtUtil.sign(accounts);
            log.info("更新的token：" + newToken);
            //判断redis中的token是否为空
            if (IsNotEmptyUtil.isNotEmpty(redisToken)) {
                log.info("redisToken不为空！");
                //验证redis中token，redisToken验证成功
                if (JwtUtil.verify(redisToken, accounts)) {
                    log.info("redisToken验证成功");
                    //redis中存入新的token
                    JedisUtil.setJson(accounts,newToken,JwtUtil.REFRESH_EXPIRE_TIME);
                    //new RedisUtil().set(accounts, newToken, JwtUtil.REFRESH_EXPIRE_TIME);
                    //设置失效时间
                    //new RedisUtil().expire(accounts, JwtUtil.REFRESH_EXPIRE_TIME);
                    //给前端返回新的token
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setHeader("token", newToken);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "token");
                    return newToken;
                }
            }
        }catch(Exception ex) {
            throw new Exception(ex.getMessage());
        }
        log.info("RefreshToken刷新token失败，redisToken为空或reqToken不正确！");
        return null;
    }

    /**
     * refreshTokens方法中,当redisToken不为空时返回true,并且如果redisToken验证成功,则将已有的token重新存入一遍,保持redis中的token不过期
     * 如果redisToken验证不通过,重新生成新的token,存入redis并返回给前端;当redisToken为空时返回false,需要重新登录.
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public static boolean refreshTokens(ServletRequest request, ServletResponse response) throws Exception {
        try{
            log.info("执行refreshTokens()方法,request="+request+",response="+response);
            //获取页面请求中的token
            String reqToken = ((HttpServletRequest)request).getHeader("Authorization");
            //去掉token前缀
            reqToken = reqToken.substring(7);
            log.info("页面请求中的token = " + reqToken);
            //获取页面请求中的token所带的用户信息
            String accounts = JwtUtil.getUsername(reqToken);
            log.info("页面请求中的account = " + accounts);
            //获取redis中的token
            String redisToken = JedisUtil.getJson(accounts);
            log.info("redis中的token是：" + redisToken);
            //生成新的token
            String newToken = JwtUtil.sign(accounts);
            log.info("更新的token：" + newToken);
            //判断redis中的token是否为空
            if (IsNotEmptyUtil.isNotEmpty(redisToken)) {
                log.info("redisToken不为空！");
                //验证redis中token，redisToken验证不通过，需要重新存入token
                if (!JwtUtil.verify(redisToken, accounts)) {
                    log.info("redisToken验证不通过");
                    //rides存入新生成的token
                    JedisUtil.setJson(accounts,newToken,JwtUtil.REFRESH_EXPIRE_TIME);
                    //new RedisUtil().set(accounts, newToken, JwtUtil.REFRESH_EXPIRE_TIME);
                    //设置失效时间
                    //new RedisUtil().expire(accounts, JwtUtil.REFRESH_EXPIRE_TIME);
                    //给前端返回新生成的token
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setHeader("token", newToken);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "token");
                } else {
                    log.info("redisToken验证通过，刷新token");
                    //redisToken验证通过，刷新token
                    JedisUtil.setJson(accounts,reqToken,JwtUtil.REFRESH_EXPIRE_TIME);
                    //new RedisUtil().set(accounts, reqToken, JwtUtil.REFRESH_EXPIRE_TIME);
                    //设置失效时间
                    //new RedisUtil().expire(accounts, JwtUtil.REFRESH_EXPIRE_TIME);
                    //给前端返回原来请求的token
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setHeader("token", reqToken);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "token");
                }
                log.info("RefreshTokens刷新token成功！");
                return true;
            }
            //}
        }catch(Exception ex) {
            throw new Exception(ex.getMessage());
        }
        log.info("RefreshTokens刷新token失败，redisToken为空！");
        return false;
    }







//    /**
//     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
//     * @param request
//     * @param response
//     * @return
//     */
//    public boolean refreshToken(ServletRequest request, ServletResponse response) throws Exception {
//        try{
//            log.info("执行MyRealm的refreshToken()方法,request="+request+",response="+response);
//            String token = ((HttpServletRequest)request).getHeader("token");
//            log.info("refreshToken中token = " + token);
//            String account = JwtUtil.getUsername(token);
//            log.info("refreshToken中account = " + account);
//            Long currentTime = JwtUtil.getCurrentTime(token);
//            log.info("refreshToken中currentTime = " + currentTime);
//            //判断Redis中RefreshToken是否存在
//            log.info("refreshToken中判断Redis中RefreshToken是否存在:"+new RedisUtil().hasKey(account));
//            log.info("refreshToken中判断Redis中RefreshToken是否存在:"+redisUtil.hasKey(account));
//            if (redisUtil.hasKey(account)) {
//                // Redis中RefreshToken还存在，获取RefreshToken的时间戳
//                Long currentTimeMillisRedis = (Long) redisUtil.get(account);
//
//                log.info("refreshToken中Redis中RefreshToken的时间戳：" + currentTimeMillisRedis);
//                // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
//                if (currentTimeMillisRedis.equals(currentTime)) {
//                    // 获取当前最新时间戳
//                    Long currentTimeMillis = System.currentTimeMillis();
//                    log.info("refreshToken中重新缓存redis");
//                    redisUtil.set(account, currentTimeMillis,
//                            new JwtUtil().REFRESH_EXPIRE_TIME);
//                    log.info("refreshToken中刷新AccessToken，设置时间戳为当前最新时间戳");
//                    // 刷新AccessToken，设置时间戳为当前最新时间戳
//                    token = new JwtUtil().sign(account);
//                    log.info("刷新后的Token：" + token);
//                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//                    httpServletResponse.setHeader("token", token);
//                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "token");
//                    log.info("JwtFilter的refreshToken()方法执行完成");
//                    return true;
//                }
//            }
//        }catch(Exception ex) {
//            throw new Exception(ex.getMessage());
//        }
//        return false;
//    }
//
//    public static void main(String[] args) {
//        System.out.println(new RedisUtil().getClass());
//        System.out.println(new RedisUtil().hasKey("张三"));
//
//
//    }

}
