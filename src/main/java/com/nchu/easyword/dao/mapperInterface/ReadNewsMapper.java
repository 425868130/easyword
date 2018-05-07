package com.nchu.easyword.dao.mapperInterface;


import com.nchu.easyword.dao.model.ReadNews;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadNewsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReadNews record);

    int insertSelective(ReadNews record);

    ReadNews selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReadNews record);

    int updateByPrimaryKey(ReadNews record);

    /**
     * 通过新闻id统计新闻阅读量
     *
     * @param newsId 新闻id
     * @return
     */
    long countViewNumById(@Param("newsId") long newsId);

    /**
     * 通过用户及新闻id 获取阅读记录，用于判断用户是否完成阅读
     *
     * @param userId 用户id
     * @param newsId 新闻id
     * @return
     */
    ReadNews selectByPrimaryUser(@Param("userId") long userId, @Param("newsId") long newsId);
}