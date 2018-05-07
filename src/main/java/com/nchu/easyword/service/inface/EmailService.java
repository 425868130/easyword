package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.exception.ServiceException;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 2017-9-28 17:13:12
 * 邮件相关业务接口
 */
public interface EmailService {
    /**
     * TODO 发送邮件给单个用户
     *
     * @param recipient 收件人,邮箱信息不能为空
     * @param subject   标题
     * @param content   邮件内容
     */
    void send(User recipient, String subject, String content) throws ServiceException;

    /**
     * TODO 邮件群发
     *
     * @param recipients 接收人列表
     * @param subject    标题
     * @param content    邮件内容
     */
    void sendAll(List<User> recipients, String subject, String content) throws ServiceException;

    /**
     * TODO 向指定用户发送账号验证邮件
     *
     * @param user 要验证的用户
     * @throws MessagingException 邮件发送异常
     */
    void sendVerifyMail(User user) throws MessagingException;
}
