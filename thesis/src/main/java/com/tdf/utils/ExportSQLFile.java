package com.tdf.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

/**
 * @author cww
 * @create 2019-09-19 11:35
 */
public class ExportSQLFile {

    public static void exportSql() {
        // https://blog.csdn.net/chaoyue1861/article/details/91038304
        StringBuffer command = new StringBuffer();
        String username = "root";
        String password = "Aa123456";
        String host = "192.168.10.244";// 主机地址
        String port = "3306";// 端口号
        String exportDatabaseName = "thesis";// 数据库名称
        String exportPath = "D:\\databackup\\filelist\\thesis.sql";// 目标文件所在位置

        // 密码是用的小p，而端口是用的大P。
        command.append("mysqldump -u").append(username).append(" -p").append(password)
                .append(" -h").append(host).append(" -P").append(port)
                .append(" ").append(exportDatabaseName).append(" -r ").append(exportPath);
        Runtime runtime = Runtime.getRuntime();
        // 这里其实是在命令窗口中执行的 command 命令行
        try {
            Process exec = runtime.exec("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\" + command.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
