package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2017-9-28 17:13:12
 * 邮件相关业务接口实现类
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    SimpleMailMessage simpleMailMessage;
    @Autowired
    SpringTemplateEngine springTemplateEngine;
    /*认证主机前缀*/
    @Value("${spring.mail.validate_host}")
    String VALIDATE_HOST;
    @Value("${spring.mail.username}")
    String emailFrom;
    /*创建可缓存线程池,由于邮件发送执行速度慢,为了不影响系统响应速度,需要用在后台线程发送邮件*/
    @Autowired
    ExecutorService pool;

    /**
     * TODO 发送邮件给单个用户
     *
     * @param recipient 收件人,邮箱信息不能为空
     * @param subject   标题
     * @param content   邮件内容
     */
    @Override
    public void send(User recipient, String subject, String content) throws ServiceException {
        if (recipient.getEmail().isEmpty()) {
            throw new ServiceException(StatusCode.SYSTEM_ERROR,"发送失败,用户邮箱不存在");
        }
        simpleMailMessage.setTo(recipient.getEmail());
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        mailSender.send(simpleMailMessage);
    }

    /**
     * TODO 邮件群发
     *
     * @param recipients 接收人列表
     * @param subject    标题
     * @param content    邮件内容
     */
    @Override
    public void sendAll(List<User> recipients, String subject, String content) throws ServiceException {
        pool.execute(new SendAllRunnable(recipients, subject, content));
    }

    /**
     * TODO 向指定用户发送账号验证邮件
     *
     * @param user 要验证的用户
     * @throws MessagingException 邮件发送异常
     */
    @Override
    public void sendVerifyMail(User user) throws MessagingException {
        /*向线程池中添加发送认证邮件的任务*/
        pool.execute(new SendVerifyMailRunnable(user));
    }

    /*用于执行群发邮件的任务类*/
    class SendAllRunnable implements Runnable {
        List<User> recipients;
        String subject;
        String content;

        public SendAllRunnable(List<User> recipients, String subject, String content) {
            this.recipients = recipients;
            this.subject = subject;
            this.content = content;
        }

        @Override
        public void run() {
            /*获取邮箱信息不为空的用户邮箱列表*/
            List<String> receivers = new ArrayList<>();
            for (User user : recipients) {
                if (!user.getEmail().isEmpty()) {
                    receivers.add(user.getEmail());
                }
            }
            simpleMailMessage.setTo(receivers.toArray(new String[receivers.size()]));
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(content);
            mailSender.send(simpleMailMessage);
        }
    }

    /*用于执行邮件发送的任务类*/
    class SendVerifyMailRunnable implements Runnable {
        private User recipients;

        public SendVerifyMailRunnable(User recipients) {
            this.recipients = recipients;
        }

        @Override
        public void run() {
            Context context = new Context();
            /*填充模板变量*/
            Map<String, Object> map = new HashMap<>();
            String href = VALIDATE_HOST + "/authentication?action=activate&account=" + recipients.getAccount() +
                    "&email=" + recipients.getEmail() + "&checkCode=" + recipients.getCheckcode();
            map.put("userAccount", recipients.getAccount());
            map.put("href", href);
            context.setVariables(map);
            String emailText = springTemplateEngine.process("validateMailTmp", context);
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(recipients.getEmail());
                helper.setSubject("欢迎加入[简词]");
                helper.setFrom(emailFrom);
                helper.setText(emailText, true);
            } catch (MessagingException e) {
                e.printStackTrace();

            }
            mailSender.send(message);
        }
    }
}
