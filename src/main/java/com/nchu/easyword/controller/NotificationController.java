package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.service.inface.NotificationService;
import com.nchu.easyword.service.inface.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

/**
 * 2018-4-1 11:48:30
 *
 * @author xujw
 * 系统通知消息相关控制器
 */

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    UserSessionService sessionService;

    /**
     * 分页获取用户个人消息列表
     *
     * @param page     页码
     * @param pageSize 每页记录条数
     * @param request  请求对象
     * @return 返回页面记录表
     */
    @RequestMapping(value = "/getUserNotification", method = RequestMethod.GET)
    public ResponseDTO getUserNotification(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws SerialException {
        User user = sessionService.getUser(request);
        if (user == null) {
            throw new SerialException("用户未登陆!");
        }
        return responseDTO.setData(notificationService.getByPage(page, pageSize, user.getId()));
    }
}
