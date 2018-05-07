package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.exception.ServiceException;

/**
 * 2018-3-18 15:29:08
 * @author xujw
 * 用户相关业务层接口
 */
public interface UserService {
    /**
     * 用户登录
     * @param userInfo 用户实体，包含用户登录所需的账号信息
     * @return 如果登录成功则返回用户详细信息，否则返回null
     * @throws ServiceException 操作中的异常信息
     */
    User userLogin(User userInfo) throws ServiceException;

    /**
     * 用户注销
     * @param userInfo 要注销的用户
     * @return 注销操作结果
     *  @throws ServiceException 操作中的异常信息
     */
    boolean userLogout(User userInfo) throws ServiceException;

    /**
     * 用户注册,对新注册的用户详细信息进行校验，包括账号合法性，密码合法性等的校验
     * 注册成功后生成校验码，存入数据库然后向用户邮箱发送一条验证邮件
     * @param userInfo 新注册的用户信息
     * @return 操作结果
     * @throws ServiceException 操作中的异常信息
     */
    boolean userRegister(User userInfo) throws ServiceException;

    /**
     * 更新用户个人信息，对新的用户信息进行校验，通过则修改数据库中对应数据
     * @param newUserInfo 新的用户信息
     * @return 操作结果
     * @throws ServiceException 操作中的异常信息
     */
    boolean updateUserInfo(User newUserInfo) throws  ServiceException;

    /**
     * 通过用户账号获取用户个人信息
     * @param account 要查找的用户账号
     * @return 返回用户的详细信息
     * @throws ServiceException 操作中的异常信息
     */
    User getUserInfoByAccount(String account)throws  ServiceException;

    /**
     * TODO 用户账户认证
     * 完成用户账户校验,同时比对注册邮箱和校验码,校验通过则修改用户账户为已验证状态
     *
     * @param userAccount      要认证的用户
     * @param email     认证邮箱
     * @param checkCode 校验码
     * @return 返回校验结果
     * @throws ServiceException 操作中的异常信息
     */
    boolean Authentication(String userAccount, String email, String checkCode) throws ServiceException;

}
