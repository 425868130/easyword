package com.nchu.easyword.dao.mapperInterface;


import com.nchu.easyword.dao.model.ResourceFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ResourceFile record);

    int insertSelective(ResourceFile record);

    ResourceFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ResourceFile record);

    int updateByPrimaryKey(ResourceFile record);

    /**
     * 分页查询
     *
     * @param start
     * @param limit
     * @return
     */
    List<ResourceFile> getFileListByPage(@Param("start") int start, @Param("limit") int limit);

    /**
     * 获取文件总数量
     *
     * @return
     */
    Long totalFileNum();
}