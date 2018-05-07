package com.nchu.easyword.service.inface;


import com.nchu.easyword.dao.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户登录session信息管理
 */
public interface UserSessionService {
    String SESSION_FLAG = "USER";

    /**
     * TODO 从Session中获取已登录用户信息
     *
     * @param request Http请求
     * @return 用户实体
     */
    User getUser(HttpServletRequest request);

    /**
     * TODO 向Session中添加登录的用户
     *
     * @param user    已登录的用户
     * @param request http请求
     */
    void addUser(User user, HttpServletRequest request);

    /**
     * TODO 从Session中移除一个用户
     *
     * @param request http请求
     */
    void removeUser(HttpServletRequest request);

    /**
     * TODO 获取当前Session
     *
     * @param request http请求
     * @return 当前请求对应的Session
     */
    HttpSession currentSession(HttpServletRequest request);

    void addAttr(HttpServletRequest request, String name, Object object);

    void removeAttr(HttpServletRequest request, String name);

    Object getAttr(HttpServletRequest request, String name);
}
