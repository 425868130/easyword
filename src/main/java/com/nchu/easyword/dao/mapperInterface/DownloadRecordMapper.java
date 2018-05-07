package com.nchu.easyword.dao.mapperInterface;


import com.nchu.easyword.dao.model.DownloadRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DownloadRecord record);

    int insertSelective(DownloadRecord record);

    DownloadRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DownloadRecord record);

    int updateByPrimaryKey(DownloadRecord record);

    /**
     * 通过文件id和用户id获取下载记录
     *
     * @param userId
     * @param fileId
     * @return
     */
    DownloadRecord selectByUserAndFile(@Param("userId") long userId, @Param("fileId") long fileId);
}