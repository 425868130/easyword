package com.nchu.easyword.controller;

import com.nchu.easyword.dao.model.ResourceFile;
import com.nchu.easyword.dao.model.User;
import com.nchu.easyword.dto.PageViewDTO;
import com.nchu.easyword.dto.ResponseDTO;
import com.nchu.easyword.exception.ServiceException;
import com.nchu.easyword.service.inface.ResFileService;
import com.nchu.easyword.service.inface.UserService;
import com.nchu.easyword.service.inface.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 2018-3-20 20:44:28
 *
 * @author xujw
 * 资料文件相关控制器
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/resFiles")
public class ResFilesController {
    @Autowired
    ResFileService resFileService;
    @Autowired
    UserSessionService sessionService;
    @Autowired
    UserService userService;

    /**
     * 以分页的方式获取资源文件的列表
     *
     * @param page     页码
     * @param pageSize 每页记录条数
     * @param request  HTTPRequest
     * @return 返回资源文件的列表（不带资源 文件url）
     */
    @RequestMapping(value = "/getFileListByPage", method = RequestMethod.GET)
    public ResponseDTO<PageViewDTO<ResourceFile>> getFileListByPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) {
        responseDTO.setMessage("资源文件列表");
        responseDTO.setData(resFileService.getFileListByPage(page, pageSize));
        return responseDTO;
    }

    /**
     * 通过文件id获取文件下载地址,服务器端要对用户积分进行计算
     *
     * @return
     */
    @RequestMapping(value = "getResUrl/{fileId}", method = RequestMethod.GET)
    public ResponseDTO getResUrl(@PathVariable("fileId") Long fileId, HttpServletRequest request, @RequestAttribute("responseDTO") ResponseDTO responseDTO) throws ServiceException {
        User user = sessionService.getUser(request);
        return responseDTO.setUserUpdate(true).setData(resFileService.downloadFile(fileId, user));
    }
}
