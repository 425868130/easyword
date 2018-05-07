package com.nchu.easyword.dao.mapperInterface;

import com.nchu.easyword.dao.model.News;
import com.nchu.easyword.dao.model.NewsWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewsWithBLOBs record);

    int insertSelective(NewsWithBLOBs record);

    NewsWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(NewsWithBLOBs record);

    int updateByPrimaryKey(News record);

    /**
     * 分页获取新闻列表(不含新闻内容)
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @param order    排序,默认按时间
     * @return
     */
    List<News> getNewsByPage(@Param("page") int page, @Param("pageSize") int pageSize, @Param("order") String order);

    /**
     * 获取新闻总数条数,用于分页查询
     *
     * @return
     */
    long getNewsCount();

    /**
     * 清空全部新闻
     */
    void clearAll();
}