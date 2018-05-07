package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.Notification;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKey(Notification record);

    /**
     * 分页查询
     *
     * @param start
     * @param limit
     * @param userId
     * @return
     */
    List<Notification> getByPage(@Param("start") int start, @Param("limit") int limit, @Param("userId") long userId);

    /**
     * 获取指定用户消息总数
     *
     * @param userId 用户id
     * @return 消息记录总数
     */
    Long totalNum(long userId);
}