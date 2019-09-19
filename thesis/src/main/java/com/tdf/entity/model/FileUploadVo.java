/**
 *
 */

package com.tdf.entity.model;

import java.io.Serializable;

/**
 * @ClassName: FileUploadVo
 * @author: ljw
 * @date: 2019年7月9日 上午10:23:23
 */
public class FileUploadVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String fileName;

    // 完整路径
    private String filePath;

    // 后半部分路径
    private String filePathPart;

    private long fileSize;


    public FileUploadVo(String fileName, String filePath, long fileSize) {
        super();
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePathPart() {
        return filePathPart;
    }

    public void setFilePathPart(String filePathPart) {
        this.filePathPart = filePathPart;
    }
}
