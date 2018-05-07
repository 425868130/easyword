package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.UserMapper;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.EmailService;
import com.nchu.easyword.service.inface.UserService;
import com.nchu.easyword.utils.DateUtil;
import com.nchu.easyword.utils.MD5Util;
import com.nchu.easyword.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 2018-3-18 16:32:41
 *
 * @author xujw
 * 用户相关业务接口实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    EmailService emailService;

    /**
     * 用户登录
     *
     * @param userInfo 用户实体，包含用户登录所需的账号信息
     * @return 如果登录成功则返回用户详细信息，否则返回null
     * @throws ServiceException 操作中的异常信息
     */
    @Override
    public User userLogin(User userInfo) throws ServiceException {
        User loginUser = userMapper.loginCheck(userInfo.getAccount(), userInfo.getPassword());
        if (loginUser == null) {
            throw new ServiceException(StatusCode.REQUEST_FAILED,"登录失败,用户名或密码错误!");
        }
        if (!loginUser.getIsverification()) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "账号未校验,请先前往邮箱完成校验再登录!");
        }
        return loginUser;
    }

    /**
     * 用户注销
     *
     * @param userInfo 要注销的用户
     * @return 注销操作结果
     * @throws ServiceException 操作中的异常信息
     */
    @Deprecated
    @Override
    public boolean userLogout(User userInfo) {
        return false;
    }

    /**
     * 用户注册,对新注册的用户详细信息进行校验，包括账号合法性，密码合法性等的校验
     * 注册成功后生成校验码，存入数据库然后向用户邮箱发送一条验证邮件
     *
     * @param userInfo 新注册的用户信息
     * @return 操作结果
     * @throws ServiceException 操作中的异常信息
     */
    @Transactional
    @Override
    public boolean userRegister(User userInfo) throws ServiceException {
        if (userMapper.selectByAccount(userInfo.getAccount()) != null) {
            throw new ServiceException(StatusCode.REPEAT, "注册失败,当前账号已注册!");
        }
        /*密码加密*/
        userInfo.setPassword(MD5Util.encode2hex(userInfo.getPassword()));
        userInfo.setNickname("新用户");
        /*生成校验码*/
        userInfo.setCheckcode(MD5Util.encode2hex(UUIDUtils.getUUID() + userInfo.getEmail() + DateUtil.getCurrentTimestamp().toString()));
        userInfo.setGmtCreate(DateUtil.getCurrentTimestamp());
        userInfo.setGmtModified(DateUtil.getCurrentTimestamp());
        try {
            userMapper.insertSelective(userInfo);
            /*添加完用户后向用户邮箱发送验证邮件*/
            emailService.sendVerifyMail(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更新用户个人信息，对新的用户信息进行校验，通过则修改数据库中对应数据
     *
     * @param newUserInfo 新的用户信息
     * @return 操作结果
     * @throws ServiceException 操作中的异常信息
     */
    @Transactional
    @Override
    public boolean updateUserInfo(User newUserInfo) throws ServiceException {
        if (userMapper.selectByAccount(newUserInfo.getAccount()) == null) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "当前账号不存在,无法修改!");
        }
        /*设置记录修改时间为当前时间*/
        newUserInfo.setGmtModified(DateUtil.getCurrentTimestamp());
        if (userMapper.updateByPrimaryKeySelective(newUserInfo) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 通过用户账号获取用户个人信息
     *
     * @param account 要查找的用户账号
     * @return 返回用户的详细信息
     * @throws ServiceException 操作中的异常信息
     */
    @Override
    public User getUserInfoByAccount(String account) {
        return userMapper.selectByAccount(account);
    }

    /**
     * TODO 用户账户认证
     * 完成用户账户校验,同时比对注册邮箱和校验码,校验通过则修改用户账户为已验证状态
     *
     * @param userAccount 要认证的用户
     * @param email       认证邮箱
     * @param checkCode   校验码
     * @return 返回校验结果
     * @throws ServiceException 操作中的异常信息
     */
    @Transactional
    @Override
    public boolean Authentication(String userAccount, String email, String checkCode) throws ServiceException {
        /*获取用户信息*/
        User user = userMapper.selectByAccount(userAccount);
        if (user == null) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "该账号不存在,请先注册账号!");
        }
        if (user.getIsverification()) {
            throw new ServiceException(StatusCode.REPEAT, "该账号已通过验证,请勿重复操作!");
        }
        /*判断校验码的有效性*/
        if (DateUtil.TheHourUpToNow(user.getGmtCreate()) > 24) {
            /*删除失效账号*/
            userMapper.deleteByPrimaryKey(user.getId());
            throw new ServiceException(StatusCode.REQUEST_FAILED, "验证码已失效,请重新注册!");
        }
        if (email.equals(user.getEmail()) && checkCode.equals(user.getCheckcode())) {
            user.setIsverification(true);
            user.setGmtModified(DateUtil.getCurrentTimestamp());
            userMapper.updateByPrimaryKeySelective(user);
            return true;
        } else {
            throw new ServiceException(StatusCode.SYSTEM_ERROR, "邮箱或验证码错误,校验失败");
        }
    }
}
