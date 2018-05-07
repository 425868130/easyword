package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.mapperInterface.DownloadRecordMapper;
import com.nchu.easyword.dao.mapperInterface.ResourceFileMapper;
import com.nchu.easyword.dao.mapperInterface.UserMapper;
import com.nchu.easyword.dao.model.DownloadRecord;
import com.nchu.easyword.dao.model.ResourceFile;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.exception.StatusCode;
import com.nchu.easyword.service.inface.NotificationService;
import com.nchu.easyword.service.inface.ResFileService;
import com.nchu.easyword.service.inface.UserService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018-3-20 21:52:59
 *
 * @author xujw
 * 资源文件相关业务类
 */
@Service
public class ResFileServiceImpl implements ResFileService {
    @Autowired
    ResourceFileMapper resourceFileMapper;
    @Autowired
    DownloadRecordMapper downloadRecordMapper;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;

    /**
     * 获取资料文件的分页列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @return 返回当前页的文件列表
     */
    @Override
    public PageViewDTO<ResourceFile> getFileListByPage(int page, int pageSize) {
        if (page > 0 && pageSize > 0) {
            return new PageViewDTO(page, pageSize, resourceFileMapper.totalFileNum(),
                    resourceFileMapper.getFileListByPage((page - 1) * pageSize, pageSize), "资料文件列表");
        }
        return null;
    }

    /**
     * 通过文件id查找文件记录
     *
     * @param id 要查找的文件id
     * @return 文件实体记录
     */
    @Override
    public ResourceFile getFileById(Long id) {
        return resourceFileMapper.selectByPrimaryKey(id);
    }

    /**
     * 下载资源文件，需要当前用户已登录且账号所剩积分足够资源积分
     *
     * @param fileId 要下载的文件id
     * @return 返回文件url地址
     */
    @Override
    public String downloadFile(long fileId, User user) throws ServiceException {
        ResourceFile resourceFile = getFileById(fileId);
        if (resourceFile == null) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "请求的文件不存在!");
        }
        /**
         *  在进行积分判断前还应查询下载记录，如果用户之前下载过该文件则直接给出下载地址无需再次支付积分
         *  */
        if (downloadRecordMapper.selectByUserAndFile(user.getId(), fileId) != null) {
            return resourceFile.getUrl();
        }
        /*判断用户积分是否足够支付下载资料所需积分，小于0表示积分不足*/
        if ((user.getPoints().compareTo(resourceFile.getPointsrequired())) < 0) {
            throw new ServiceException(StatusCode.REQUEST_FAILED, "抱歉,您的积分不足以下载该资源!");
        }
        /*扣除用户对应积分并更新到数据库*/
        user.setPoints(user.getPoints() - resourceFile.getPointsrequired());
        userService.updateUserInfo(user);
        /*文件下载次数加一*/
        resourceFile.setDownCount(resourceFile.getDownCount() + 1);
        resourceFile.setGmtModified(DateUtil.getCurrentTimestamp());
        resourceFileMapper.updateByPrimaryKeySelective(resourceFile);
        /*添加一条下载记录*/
        downloadRecordMapper.insertSelective(new DownloadRecord(user.getId(), fileId, DateUtil.getCurrentTimestamp()));
        /*还要生成一条用户的系统消息记录*/
        notificationService.newNotification(NotificationServiceImpl.genSimpleNotification(user.getId(), "下载资料[" + resourceFile.getFilename() + "]扣除积分:" + resourceFile.getPointsrequired() + "点."));
        return resourceFile.getUrl();
    }
}
