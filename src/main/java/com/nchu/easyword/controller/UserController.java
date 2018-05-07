package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.dto.UserDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.FileService;
import com.nchu.easyword.service.inface.UserService;
import com.nchu.easyword.service.inface.UserSessionService;
import com.nchu.easyword.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 2018-3-18 11:57:32
 *
 * @author xujw
 * 用户相关控制器
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/user")
public class
UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    @Qualifier("ossFileService")
    FileService fileService;

    /**
     * 用户登录
     *
     * @param userInfo
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseDTO userLogin(@RequestBody User userInfo, HttpServletRequest request
            , @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        /*密码在前端经过一次MD5后端再进行一次MD5以匹配数据库中的密码*/
        userInfo.setPassword(MD5Util.encode2hex(userInfo.getPassword()));
        /*先从session中读取用户信息*/
        User loginUser = sessionService.getUser(request);
        if (loginUser != null) {
            if (userInfo.getAccount().equals(loginUser.getAccount()) && userInfo.getPassword().equals(loginUser.getPassword())) {
                return responseDTO.setData(UserDTO.transFromUser(loginUser));
            } else {
                throw new ServiceException(StatusCode.REQUEST_FAILED, "用户名或密码不正确");
            }
        } else {
            /*如果session中用户信息为空说明用户状态为未登陆,则进行数据库校验*/
            loginUser = userService.userLogin(userInfo);
            sessionService.addUser(loginUser, request);
        }
        responseDTO.setMessage("用户登录");
        return responseDTO.setData(UserDTO.transFromUser(loginUser));
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseDTO userLogout(HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        sessionService.removeUser(request);
        return responseDTO.setStatusCode(StatusCode.UN_LOGIN.getcode());
    }

    /**
     * 用户注册
     *
     * @param userInfo 用户账号信息,其中用户密码必须为经过MD5运算后的字符串
     * @return 注册结果
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseDTO userRegister(@RequestBody User userInfo, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        responseDTO.setMessage("用户注册");
        return responseDTO.setData(userService.userRegister(userInfo));
    }

    /**
     * 用户信息修改更新
     *
     * @param userInfo 新的用户信息
     * @return 操作结果
     * @throws ServiceException 操作中的异常信息
     */
    @RequestMapping(value = "/userInfoUpdate", method = RequestMethod.PUT)
    public ResponseDTO userInfoUpdate(@RequestBody User userInfo, HttpServletRequest request
            , @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        User user = sessionService.getUser(request);
        responseDTO.setMessage("更新用户信息");
        if (!user.getAccount().equals(userInfo.getAccount()) || !user.getPassword().equals(userInfo.getPassword())) {
            throw new ServiceException(StatusCode.PERMISSION_DENIED, "非法操作");
        }
        /*前端传来的用户对象不含id必须设置id才能更新到数据库*/
        userInfo.setId(user.getId());
        if (userService.updateUserInfo(userInfo)) {
            /*更新session中的用户信息*/
            sessionService.addUser(userService.getUserInfoByAccount(user.getAccount()), request);
            /*返回数据并通知前端用户数据已修改*/
            return responseDTO.setData(true).setUserUpdate(true);
        }
        return responseDTO.setData(false);
    }

    /**
     * 用户账号认证,当用户点击账号认证邮件链接时触发
     * 对账号校验码及邮箱进行认证，通过则修改账号状态为已认证
     *
     * @param user 要认证的用户
     * @return 返回查操作结果
     */
    @RequestMapping(value = "/userAuthentication", method = RequestMethod.POST)
    public ResponseDTO userAuthentication(@RequestBody User user
            , @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        return responseDTO.setData(userService.Authentication(user.getAccount(), user.getEmail(), user.getCheckcode()));
    }

    /**
     * 获取当前在线的用户信息
     *
     * @return 用户信息
     */
    @RequestMapping(value = "/getOnlineUser", method = RequestMethod.GET)
    public ResponseDTO getOnlineUser(HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {

        return responseDTO.setData(UserDTO.transFromUser(sessionService.getUser(request))).setMessage("用户信息");
    }

    /*文件上传*/
    @RequestMapping(value = "/updateUserImg", method = RequestMethod.POST)
    public ResponseDTO updateUserImg(@RequestPart("file") MultipartFile uploadPicture, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO
    ) throws IOException, ServiceException {
        responseDTO.setMessage("头像修改");
        User user = sessionService.getUser(request);
        if (uploadPicture == null) {
            return responseDTO.setData("");
        }
        String url = fileService.fileUpload("headPort", uploadPicture);
        /*删除原头像文件*/
        fileService.fileDeleteByUrl(user.getHeadportrait());
        user.setHeadportrait(url);
        userService.updateUserInfo(user);
        return responseDTO.setData(url);
    }

    /**
     * 用于抛出用户未登陆异常，主要用于提供给用户拦截器，以使异常可以被统一处理
     * 直接在拦截器中抛出业务异常无法被ControllerAdvice统一处理，所以在登录拦截器中进行重定向
     *
     * @throws ServiceException
     */
    @RequestMapping(value = "/unLoginError", method = {RequestMethod.GET, RequestMethod.POST})
    public void unLoginError() throws ServiceException {
        throw new ServiceException(StatusCode.UN_LOGIN);
    }
}
