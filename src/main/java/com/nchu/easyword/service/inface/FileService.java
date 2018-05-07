package com.nchu.easyword.service.inface;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件相关业务
 */
public interface FileService {
    /**
     * TODO 单文件上传
     *
     * @param serverPath 文件服务器路径
     * @param file       要上传的文件
     * @return 返回上传后的文件url
     */
    String fileUpload(String serverPath, MultipartFile file) throws IOException;

    /**
     * TODO 多文件上传
     *
     * @param folder 要上传到的文件夹路径
     * @param files  要上传的文件列表
     * @return 返回所有上传文件的url地址
     */
    List<String> fileMultiUpload(String folder, List<MultipartFile> files);

    /**
     * TODO 通过url删除指定文件
     *
     * @param url 要删除文件的url地址
     * @return 返回操作结果
     */
    boolean fileDeleteByUrl(String url);

    /**
     * TODO 多文件删除
     *
     * @param fileIdList 要删除的文件的文件url列表
     * @return 返回删除成功的文件数
     */
    int fileMultiDelete(List<Long> fileIdList);

}
