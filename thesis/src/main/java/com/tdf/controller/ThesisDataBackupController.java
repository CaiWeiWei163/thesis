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
     * 数据备份(打包并下载，根据id下载文件)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/ajax/download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response, String id) {
        // 根据id查询文件路径
        String filePath = thesisDataBackupService.getThesisBackupsById(id).getFileinfoId();
        File file = new File(filePath);
        String fileName = file.getName();
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            request.setCharacterEncoding("UTF-8");
            String agent = request.getHeader("User-Agent").toUpperCase();
            if ((agent.indexOf("MSIE") > 0) || ((agent.indexOf("RV") != -1) && (agent.indexOf("FIREFOX") == -1)))
                fileName = URLEncoder.encode(fileName, "UTF-8");
            else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(file.length()));
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

}
