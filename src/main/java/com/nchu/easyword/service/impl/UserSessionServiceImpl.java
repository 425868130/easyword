package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.service.inface.UserSessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    /**
     * TODO 从Session中获取已登录用户信息
     *
     * @param request Http请求
     * @return 用户实体
     */
    @Override
    public User getUser(HttpServletRequest request) {
        return  (User) currentSession(request).getAttribute(SESSION_FLAG);
    }

    /**
     * TODO 向Session中添加登录的用户
     *
     * @param user    已登录的用户
     * @param request http请求
     */
    @Override
    public void addUser(User user, HttpServletRequest request) {
        currentSession(request).setAttribute(SESSION_FLAG, user);
    }

    /**
     * TODO 从Session中移除一个用户
     *
     * @param request http请求
     */
    @Override
    public void removeUser(HttpServletRequest request){
        request.getSession().removeAttribute(SESSION_FLAG);
        currentSession(request).invalidate();
    }

    /**
     * TODO 获取当前Session
     *
     * @param request http请求
     * @return 当前请求对应的Session
     */
    @Override
    public HttpSession currentSession(HttpServletRequest request) {
        return request.getSession();
    }

    @Override
    public void addAttr(HttpServletRequest request, String name, Object object) {
        request.getSession().setAttribute(name, object);
    }

    @Override
    public void removeAttr(HttpServletRequest request, String name) {
        request.getSession().removeAttribute(name);
    }

    @Override
    public Object getAttr(HttpServletRequest request, String name) {
        return request.getSession().getAttribute(name);
    }
}
