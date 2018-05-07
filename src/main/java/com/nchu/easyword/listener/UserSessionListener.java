package com.nchu.easyword.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 用户Session监听器
 */
public class UserSessionListener implements HttpSessionListener {
    private static int activeSessions = 0;

    /**
     * Receives notification that a session has been created.
     *
     * @param se the HttpSessionEvent containing the session
     */
    @Override
    public synchronized void sessionCreated(HttpSessionEvent se) {
        activeSessions++;
        //System.out.println("欢迎登录" +activeSessions);
    }

    /**
     * Receives notification that a session is about to be invalidated.
     *
     * @param se the HttpSessionEvent containing the session
     */
    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent se) {
        activeSessions--;
    }
}