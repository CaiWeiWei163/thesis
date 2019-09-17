package com.tdf.controller;

import com.tdf.common.ContextHolder;
import com.tdf.controller.base.BaseController;
import com.tdf.entity.ThesisInfo;
import com.tdf.entity.criteria.ThesisInfoCriteria;
import com.tdf.entity.model.FileModel;
import com.tdf.entity.model.SimpleModel;
import com.tdf.entity.sys.SysDict;
import com.tdf.models.LayuiDataTableModel;
import com.tdf.service.thesis.ThesisManageService;
import com.tdf.service.fileUploadService.FileUploadService;
import com.tdf.util.exceptions.KnowException;
import com.tdf.util.page.PagedList;
import com.tdf.util.web.ActionResult;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 论文管理
 *
 * @author cww
 * @create 2019-09-12 14:29
 */
@RestController
@RequestMapping("/thesisManage")
public class ThesisManageController extends BaseController {

    @Autowired
    ThesisManageService thesisManageService;

    @Autowired
    FileUploadService fileUploadService;

    // 论文首页
    @RequestMapping({"", "/index"})
    public ModelAndView listHospitalInfoUserIndex() {
        ModelAndView mv = new ModelAndView("thesis/index");
        return mv;
    }

    // 论文列表
    @RequestMapping("/ajax/listThesis")
    public ActionResult listThesis(ThesisInfoCriteria criteria) {
        try {
            PagedList<ThesisInfo> pagedList = thesisManageService.listThesis(criteria);
            return LayuiDataTableModel.layuiDataTable(pagedList);
        } catch (KnowException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 跳转到添加页面
     */
    @RequestMapping("/ajax/toAddPage")
    public ModelAndView toAddPage() {
        return new ModelAndView("thesis/_add");
    }

    /**
     * 添加论文
     *
     * @param thesisInfo
     * @return
     */
    @RequestMapping(value = "/ajax/addThesis", method = RequestMethod.POST)
    public ActionResult addThesis(ThesisInfo thesisInfo) {
        try {
            thesisManageService.addThesis(thesisInfo);
            return success();
        } catch (KnowException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 跳转到编辑页面
     */
    @RequestMapping("/ajax/toEditPage")
    public ModelAndView toEditPage(String id) {
        ModelAndView mv = new ModelAndView();
        ThesisInfo thesis = thesisManageService.getThesisInfoById(id);
        mv.addObject("thesis", thesis);
        mv.setViewName("thesis/_edit");
        return mv;
    }

    /**
     * 编辑论文
     *
     * @param thesisInfo
     * @return
     */
    @RequestMapping(value = "/ajax/editThesis", method = RequestMethod.POST)
    public ActionResult editThesis(ThesisInfo thesisInfo) {
        try {
            thesisManageService.editThesis(thesisInfo);
            return success();
        } catch (KnowException e) {
            return fail(e.getMessage());
        }
    }

    /**
     * 根据父级key查询子级列表
     *
     * @return
     */
    @RequestMapping(value = "ajax/getSysdictData", method = RequestMethod.GET)
    public List<SimpleModel> getSysdictData(String parentKey, String getAll) {
        List<SysDict> list = thesisManageService.selectSysDictByParentKey(parentKey);
        List<SimpleModel> list1 = new ArrayList<>();
        if (null != getAll && getAll.equals("yes")) {
            SimpleModel simpleModel = new SimpleModel();
            simpleModel.setValue("全部");
            simpleModel.setKey("");
            list1.add(simpleModel);
        }
        for (SysDict s : list) {
            SimpleModel simpleModel = new SimpleModel();
            simpleModel.setKey(s.getDictkey());
            simpleModel.setValue(s.getDictvalue());
            list1.add(simpleModel);
        }
        return list1;
    }

    // 根据名字模糊查询
    @RequestMapping("/ajax/fuzzySearch")
    public List<SysDict> fuzzySearch(String search, String type) {
        List<SysDict> list = thesisManageService.fuzzySearch(search, type);
        return list;
    }

    /**
     * 上传论文原文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileUploadService.fileUpload(file, null);
            return success(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return fail("上传失败");
        }
    }

}
