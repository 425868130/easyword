package com.nchu.easyword.interceptor;

import com.nchu.easyword.service.inface.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xujw
 * 2018-4-18 18:03:40
 * 用户登录状态拦截器,用于检测用户登录状态,优先级为 2
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserSessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
        /*如果为OPTIONS方法则略过校验*/
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            return true;
        }
        if (sessionService.getUser(request) == null) {
            System.out.println("用户未登陆");
            response.sendRedirect("/user/unLoginError");
            return false;
        }
        return true;
    }
}
