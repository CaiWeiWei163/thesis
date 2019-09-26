package com.tdf.service.thesis;

import com.tdf.common.ContextHolder;
import com.tdf.dao.ThesisBackupsMapper;
import com.tdf.entity.ThesisBackups;
import com.tdf.util.StringUuid;
import com.tdf.util.page.PagedCriteria;
import com.tdf.util.page.PagedList;
import com.tdf.utils.ExportSQLFile;
import com.tdf.utils.FileToZip;
import com.tdf.utils.GetFileNameUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cww
 * @create 2019-09-12 15:13
 */
@Service
@Transactional(rollbackForClassName = "Exception.class")
public class ThesisDataBackupService {

    @Autowired
    ThesisBackupsMapper thesisBackupsMapper;

    // 数据备份列表
    public PagedList<ThesisBackups> listBackups(PagedCriteria criteria) {
        Map<String, Object> map = criteria.toWhereMap();
        Integer total = 0;
        if (criteria.getPageSize() > 0) {
            total = thesisBackupsMapper.getCountBackups(map);
            if (total == 0) {
                return new PagedList<>(null, criteria.getPageNo(), total);
            }
        }
        List<ThesisBackups> list = thesisBackupsMapper.listBackups(criteria.getTopIndex(),
                criteria.getPageSize(), map);
        // 替换文件列表显示
        if (CollectionUtils.isNotEmpty(list)) {
            for (ThesisBackups thesisBackups : list) {
                String fileList = thesisBackups.getBackupsList();
                if (StringUtils.isNotEmpty(fileList)) {
                    // 去掉[]
                    String strip = StringUtils.strip(fileList, "[]");
                    String replace = strip.replace(",", "、 ");
                    thesisBackups.setBackupsListStr(replace);
                }
            }
        }
        return new PagedList<>(list, criteria.getPageNo(), total);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    public ThesisBackups getThesisBackupsById(String id) {
        return thesisBackupsMapper.selectByPrimaryKey(id);
    }

    /**
     * 创建数据备份
     */
    public void createBackups() throws IOException {
        // https://blog.csdn.net/r1037/article/details/78356698
        // 1.创建文件夹
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
        String format = dateTimeFormatter.format(LocalDateTime.now());
        String basePath = "D:\\databackup\\";
        String filePath = basePath + format;// 文件夹路径
        File file = new File(basePath);
        if (!file.exists()) {//若此目录不存在，则创建之
            file.mkdir();
        }
        File file1 = new File(filePath);
        if (!file1.exists()) {//若此目录不存在，则创建之
//            file1.mkdir();
        }
        File file2 = new File(basePath + "filelist");
        if (!file2.exists()) {//若此目录不存在，则创建之
            file2.mkdir();
        }
        // 2.导出MySql的sql文件
        ExportSQLFile.exportSql();
        // 3.将文件复制到刚创建的文件夹下
//        copyDir(basePath + "filelist", filePath);
        // 4.打成zip包
        FileToZip.fileToZip(basePath + "filelist", basePath, format);
        // 5.表增加一条记录
        ThesisBackups tb = new ThesisBackups();
        tb.setId(StringUuid.getUuid());
        tb.setFileinfoId(basePath + format + ".zip");
        String fileNames = GetFileNameUtils.getFileNames(basePath + "filelist");
        tb.setBackupsList(fileNames);
        tb.setCreateTime(new Date());
        tb.setCreateUser(ContextHolder.getLoginSysUserInfo().getRealname());
        thesisBackupsMapper.insertSelective(tb);
    }

    /**
     * 拷贝文件夹
     *
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        //文件名称列表
        String[] filePath = file.list();
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + file.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }
            if (new File(oldPath + file.separator + filePath[i]).isFile()) {
                copyFile(oldPath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }
        }
    }

    /**
     * 拷贝文件
     *
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[2097152];
        while ((in.read(buffer)) != -1) {
            out.write(buffer);
        }
    }
}
