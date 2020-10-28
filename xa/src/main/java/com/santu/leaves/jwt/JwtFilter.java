package com.santu.leaves.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 重写鉴权
 * @Author: LEAVES
 * @Date: 2020年10月27日 15时17分07秒
 * @Version 1.0
 * @Description:
 *
 * 执行流程 preHandle -> isAccessAllowed -> isLoginAttempt -> executeLogin 。
 */

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    //默认需要放行的接口 shiroc处判断过，此处可写可不写
    private String[] defalutExcludeUrl = new String[] {
            "/logout","/login","/authenticate"
            //,"/formLogin",".jpg",".png",".gif",".css",".js",".jpeg"
    };

    /**
     * 检测用户是否想要登录
     * 检测header里面是否包含token字段即可
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        log.info("JwtFilter==isLoginAttempt--->");
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        if (authorization != null){
            //去掉token前缀
            authorization = authorization.substring(7);
            log.info("JwtFilter==isLoginAttempt--->authorization = " + authorization);
            log.info("JwtFilter==isLoginAttempt--->用户已经登录过了");
            return authorization != null;
        }else{
            log.info("JwtFilter==isLoginAttempt--->用户未登录");
            return authorization == null;
        }
    }

    /**
     * JwtToken实现了AuthenticationToken接口封装了token参数
     * 通过getSubject方法获取 subject对象
     * login()发送身份验证
     * 为什么需要在Filter中调用login,不能在controller中调用login?
     * 由于Shiro默认的验证方式是基于session的，在基于token验证的方式中，不能依赖session做为登录的判断依据．
     * 执行登录
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        log.info("JwtFilter==executeLogin--->");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        //去掉token前缀
        authorization = authorization.substring(7);
        JwtToken token = new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            //触发 Shiro Realm 自身的登录控制
            getSubject(request, response).login(token);
            // 如果没有抛出异常则代表登入成功，返回true
            log.info("JwtFilter==executeLogin--->验证登入成功");
            return true;
        } catch (Exception e) {
            log.error("JwtFilter==executeLogin--->没有访问权限，原因是：" + e.getMessage());
            //throw new AuthenticationException("无效token，请先登录！！！！" + e.getMessage());
            return false;
        }
    }

    /**
     * 这里详细说明下为什么最终返回的都是true，即允许访问
     * 例如提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("JwtFilter==isAccessAllowed--->");
        //判断请求的请求头是否带上 "token"
        if (this.isLoginAttempt(request, response)) {
            log.info("JwtFilter==isAccessAllowed--->token存在");
            try {
                //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
                if (this.executeLogin(request, response)){
                    String requestURL = ((HttpServletRequest) request).getRequestURL().toString();
                    log.info("JwtFilter==isAccessAllowed--->requestURL="+requestURL.toString());
                    for(String excludeUrl : defalutExcludeUrl){
                        if (requestURL.endsWith(excludeUrl)){
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("JwtFilter==isAccessAllowed--->Token已失效或为空,JwtFilter过滤验证失败!");
                //response401(request, response);
                throw new AuthenticationException("token为空，请重新登录！");
            }
        }
        //如果请求头不存在 token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    /**
     * 对跨域提供支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("JwtFilter==preHandle--->");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /401
     * @param req
     * @param resp
     */
    private void response401(ServletRequest req, ServletResponse resp) {
        log.info("JwtFilter==response401--->");
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
