package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.Notification;
import com.nchu.easyword.dto.PageViewDTO;

import java.util.List;

/**
 * 2018-4-4 10:45:36
 *
 * @author xujw
 * 用户通知相关业务类接口
 */
public interface NotificationService {
    /**
     * 分页获取指定用户的消息记录
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param userId   要获取消息的用户id
     * @return 消息列表
     */
    PageViewDTO<Notification> getByPage(int page, int pageSize, long userId);

    /**
     * 创建一条新的通知消息
     *
     * @param notification 消息实体
     * @return 操作结果
     */
    boolean newNotification(Notification notification);

    /**
     * 将指定用户的全部信息设置为已读
     *
     * @param notificationIdList 要设置为已读状态的消息id列表
     * @param userId             用户id
     * @return 操作结果
     */
    boolean readAll(List<Long> notificationIdList, long userId);

    /**
     * 将指定用户的全部信息删除
     *
     * @param notificationIdList 要删除的消息id列表
     * @param userId             用户id
     * @return 操作结果
     */
    boolean deleteAll(List<Long> notificationIdList, long userId);
}
