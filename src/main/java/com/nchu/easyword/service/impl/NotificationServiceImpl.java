package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.NotificationMapper;
import com.nchu.easyword.dao.model.Notification;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.service.inface.NotificationService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018-4-4 10:45:36
 *
 * @author xujw
 * 用户通知相关业务类
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationMapper notificationMapper;

    /**
     * 快速生成一条简单的通知消息
     * @param receiveId 接收人id
     * @param content 消息内容
     * @return 通知消息实体
     */
    public static Notification genSimpleNotification(long receiveId,String content) {
        Notification notification = new Notification();
        notification.setGmtCreate(DateUtil.getCurrentTimestamp());
        notification.setGmtModified(DateUtil.getCurrentTimestamp());
        notification.setIsread(false);
        notification.setReceiveid(receiveId);
        notification.setContent(content);
        return notification;
    }

    /**
     * 分页获取指定用户的消息记录
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param userId   要获取消息的用户id
     * @return 消息列表
     */
    @Override
    public PageViewDTO<Notification> getByPage(int page, int pageSize, long userId) {
        if (page > 0 && pageSize > 0) {
            return new PageViewDTO(page, pageSize, notificationMapper.totalNum(userId), notificationMapper.getByPage((page - 1) * pageSize, pageSize, userId), "通知消息");
        }
        return null;
    }

    /**
     * 创建一条新的通知消息
     *
     * @param notification 消息实体
     * @return 操作结果
     */
    @Override
    public boolean newNotification(Notification notification) {
        if (notificationMapper.insertSelective(notification) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 将指定用户的全部信息设置为已读
     *
     * @param notificationIdList 要设置为已读状态的消息id列表
     * @param userId             用户id
     * @return 操作结果
     */
    @Override
    public boolean readAll(List<Long> notificationIdList, long userId) {
        return false;
    }

    /**
     * 将指定用户的全部信息删除
     *
     * @param notificationIdList 要删除的消息id列表
     * @param userId             用户id
     * @return 操作结果
     */
    @Override
    public boolean deleteAll(List<Long> notificationIdList, long userId) {
        return false;
    }
}
