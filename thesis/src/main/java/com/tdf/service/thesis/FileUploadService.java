/**
 *
 */

package com.tdf.service.fileUploadService;

import com.tdf.util.StringUuid;
import com.tdf.util.exceptions.KnowException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @ClassName: FileUploadServer
 * @author: ljw
 * @date: 2019年7月9日 上午10:19:53
 */
@Service
@Transactional(rollbackForClassName = "Exception.class")
public class FileUploadService {

    @Autowired
    private Environment env; //读取配置文件

    /**
     * 保存图片 不需要保存数据库表
     *
     * @param file       文件
     * @param folderName 文件夹名称
     * @return
     */
    public String fileUpload(MultipartFile file, String folderName) {
        String relativePath = getRelativePath(folderName);

        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        // 文件存放物理目录
        String filePath = getPhysicsPath(folderName);
        File file1 = new File(filePath);
        if (!file1.exists() && !file1.isDirectory()) {
            file1.mkdirs();
        }
        //得到后缀
        String suffixType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        //得到新的名称
        String realName = StringUuid.getUuid() + suffixType;
        // 文件存放物理地址
        String completeFilePath = filePath + realName;
        // 文件访问相对地址
        String relativeFilePath = relativePath + realName;
        try {
            //保存文件到本地
            FileUtils.writeByteArrayToFile(new File(completeFilePath), file.getBytes());
            return fileName;
        } catch (Exception e) {
            throw new KnowException(e.getMessage(), -1);
        }
    }


    /**
     * 获取相对地址（访问地址）
     *
     * @param module
     * @return
     */
    public String getRelativePath(String folderName) {
        String path = env.getProperty("uploadCenterPath");
        if (StringUtils.isNotEmpty(folderName)) {
            path += folderName + "/";
        }

        return path;
    }

    /**
     * 文件存放目录
     *
     * @param module
     * @return
     */
    public String getPhysicsPath(String folderName) {
        String path = env.getProperty("filePath");
        if (StringUtils.isNotEmpty(folderName)) {
            path += folderName + "/";
        }
        return path;
    }
}
