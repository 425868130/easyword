package com.nchu.easyword.service.inface;

import com.nchu.easyword.dao.model.ResourceFile;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.exception.ServiceException;

/**
 * 2018-3-20 21:18:33
 *
 * @author xujw
 * 资料文件相关业务接口
 */
public interface ResFileService {
    /**
     * 获取资料文件的分页列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 返回当前页的文件列表
     */
    PageViewDTO<ResourceFile> getFileListByPage(int page, int pageSize);

    /**
     * 通过文件id查找文件记录
     *
     * @param id 要查找的文件id
     * @return 文件实体记录
     */
    ResourceFile getFileById(Long id);

    /**
     * 下载资源文件，需要当前用户已登录且账号所剩积分足够资源积分
     *
     * @param fileId 要下载的文件id
     * @param user   要下载文件的用户
     * @return 返回文件url地址
     */
    String downloadFile(long fileId, User user) throws ServiceException;


}
