package com.nchu.easyword.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GenericRequest;
import com.nchu.easyword.service.inface.FileService;
import com.nchu.easyword.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 2018-3-20 21:51:21
 *
 * @author xujw
 * 阿里云OSS文件相关业务类
 */
@Service("ossFileService")
public class OssFileServiceImpl implements FileService {
    @Value("${ossClient.endpoint}")
    String endpoint;
    @Value("${ossClient.accessKeyId}")
    String accessKeyId;
    @Value("${ossClient.accessKeySecret}")
    String accessKeySecret;
    @Value("${ossClient.bucketName}")
    String bucketName;
    /*系统默认资源文件设置*/
    final String[] SysFileResUr = {
            "https://easywords.oss-cn-hangzhou.aliyuncs.com/headPort/default/userDefault.png"
    };

    public OSSClient getOssClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * TODO 单文件上传
     *
     * @param folderName 文件夹
     * @param file       要上传的文件
     * @return 返回上传后的文件url
     */
    @Override
    public String fileUpload(String folderName, MultipartFile file) throws IOException {
        OSSClient ossClient = getOssClient();
        String fileName;
        if (folderName == null) {
            fileName = getRandomName(file.getOriginalFilename());
        } else {
            fileName = folderName + "/" + getRandomName(file.getOriginalFilename());
        }
        try {
            ossClient.putObject(bucketName, fileName, file.getInputStream());
        } catch (IOException e) {
            throw e;
        } finally {
            ossClient.shutdown();
        }
        return getFileUrl(fileName);
    }

    /**
     * TODO 多文件上传
     *
     * @param folder 要上传到的文件夹路径
     * @param files  要上传的文件列表
     * @return 返回所有上传文件的url地址
     */
    @Override
    public List<String> fileMultiUpload(String folder, List<MultipartFile> files) {
        return null;
    }

    /**
     * TODO 通过url删除指定文件
     *
     * @param url 要删除文件的url地址
     * @return 返回操作结果
     */
    @Override
    public boolean fileDeleteByUrl(String url) {
        /*如果是系统默认资源文件则不执行删除操作*/
        if (isSysFileResUrl(url)) {
            return false;
        }
        OSSClient ossClient = getOssClient();
        String fileName = getFileNameByUrl(url);
        if (fileName == null) return false;
        try {
            GenericRequest request = new DeleteObjectsRequest(bucketName).withKey(fileName);
            ossClient.deleteObject(request);
        } catch (Exception e) {
            throw new ServiceException("文件删除失败");
        } finally {
            ossClient.shutdown();
        }
        return true;
    }

    /**
     * TODO 多文件删除
     *
     * @param fileIdList 要删除的文件的文件url列表
     * @return 返回删除成功的文件数
     */
    @Override
    public int fileMultiDelete(List<Long> fileIdList) {
        return 0;
    }

    /**
     * @param fileUrl 文件url
     * @return String fileName
     * @MethodName: getFileName
     * @Description: 根据url获取fileName
     */
    private static String getFileNameByUrl(String fileUrl) {
        String str = "aliyuncs.com/";
        int beginIndex = fileUrl.indexOf(str);
        if (beginIndex == -1) return null;
        return fileUrl.substring(beginIndex + str.length());
    }

    /**
     * 构造文件完整的url地址
     * 阿里云OSS文件路径规则,在原Endpoint地址的http://协议后接Bucket的名字,然后是Endpoint地址,最后加文件名
     *
     * @param fileName 初始文件名
     * @return 完整可访问的文件url地址
     */
    private String getFileUrl(String fileName) {
        return endpoint.replaceFirst("https://", "https://" + bucketName + ".") + "/" + fileName;
    }


    /**
     * 获取随机文件名
     *
     * @param originalFilename 原始文件名
     * @return 返回文件的后缀名
     */
    private String getRandomName(String originalFilename) {
        StringBuilder stringBuilder = new StringBuilder();
        /*将系统时间毫秒加上随机UUID值生成唯一的随机文件名*/
        stringBuilder.append(System.currentTimeMillis() + UUIDUtils.getUUID());
        /*获取文件名最后一个"."字符所在的下标直到字符串结尾即是文件后缀名*/
        stringBuilder.append(originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length()));
        return stringBuilder.toString();
    }

    /**
     * 判断给定url是否为系统资源文件地址
     *
     * @param url
     * @return
     */
    private boolean isSysFileResUrl(String url) {
        for (int i = 0; i < SysFileResUr.length; i++) {
            if (url.equals(SysFileResUr[i])) {
                return true;
            }
        }
        return false;
    }
}
