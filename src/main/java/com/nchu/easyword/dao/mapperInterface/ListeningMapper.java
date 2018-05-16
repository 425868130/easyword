package com.nchu.easyword.dao.mapperInterface;


import com.nchu.easyword.dao.model.Listening;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListeningMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Listening record);

    int insertSelective(Listening record);

    Listening selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Listening record);

    int updateByPrimaryKeyWithBLOBs(Listening record);

    int updateByPrimaryKey(Listening record);

    /*获取总记录数*/
    long getCount();

    /*分页*/
    List<Listening> getByPage(@Param("page") int page, @Param("pageSize") int pageSize);
}