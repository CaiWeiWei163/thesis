package com.tdf.controller;

import com.tdf.controller.base.BaseController;
import com.tdf.entity.ThesisBackups;
import com.tdf.entity.ThesisInfo;
import com.tdf.entity.criteria.ThesisInfoCriteria;
import com.tdf.entity.model.FileUploadVo;
import com.tdf.entity.model.SimpleModel;
import com.tdf.entity.sys.SysDict;
import com.tdf.models.LayuiDataTableModel;
import com.tdf.service.fileUploadService.FileUploadService;
import com.tdf.service.thesis.ThesisDataBackupService;
import com.tdf.service.thesis.ThesisManageService;
import com.tdf.util.exceptions.KnowException;
import com.tdf.util.page.PagedCriteria;
import com.tdf.util.page.PagedList;
import com.tdf.util.web.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据备份
 *
 * @author cww
 * @create 2019-09-12 14:29
 */
@RestController
@RequestMapping("/dataBackup")
public class ThesisDataBackupController extends BaseController {

    @Autowired
    ThesisDataBackupService thesisDataBackupService;

    @Autowired
    FileUploadService fileUploadService;

    // 数据备份首页
    @RequestMapping({"", "/index"})
    public ModelAndView listHospitalInfoUserIndex() {
        ModelAndView mv = new ModelAndView("backup/index");
        return mv;
    }

    // 数据备份列表
    @RequestMapping("/ajax/listBackups")
    public ActionResult listBackups(PagedCriteria criteria) {
        try {
            PagedList<ThesisBackups> pagedList = thesisDataBackupService.listBackups(criteria);
            return LayuiDataTableModel.layuiDataTable(pagedList);
        } catch (KnowException e) {
            return fail(e.getMessage());
        }
    }

    // 创建数据备份
    @RequestMapping(value = "/ajax/createBackups", method = RequestMethod.POST)
    public ActionResult createBackups() {
        try {
            thesisDataBackupService.createBackups();
            return success();
        } catch (KnowException e) {
            return fail(e.getMessage());
        } catch (IOException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 打包并下载
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response, String id) {
        // 根据id查询文件路径
        String filePath = thesisDataBackupService.getThesisBackupsById(id).getFileinfoId();
        File file = new File(filePath);
        String fileName = file.getName();
        InputStream fis = null;
        response.setCharacterEncoding("UTF-8");
        try {
            fis = new FileInputStream(file);
            request.setCharacterEncoding("UTF-8");
            String agent = request.getHeader("User-Agent").toUpperCase();
            if (getBrowser(request).equals("FF")) {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            } else {
                response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            }
//            response.reset();
            response.setContentType("application/force-download");// 设置强制下载不打开
            byte[] b = new byte[1024];
            int len;
            while ((len = fis.read(b)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            response.flushBuffer();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断浏览器种类
     *
     * @param request
     * @return
     */
    private String getBrowser(HttpServletRequest request) {
        String UserAgent = request.getHeader("USER-AGENT").toLowerCase();
        if (UserAgent != null) {
            if (UserAgent.indexOf("msie") >= 0)
                return "IE";
            if (UserAgent.indexOf("firefox") >= 0)
                return "FF";
            if (UserAgent.indexOf("safari") >= 0)
                return "SF";
        }
        return null;
    }

}
