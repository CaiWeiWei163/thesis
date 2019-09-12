package com.tdf.controller;

import com.tdf.controller.base.BaseController;
import com.tdf.entity.ThesisInfo;
import com.tdf.entity.criteria.ThesisInfoCriteria;
import com.tdf.models.LayuiDataTableModel;
import com.tdf.service.thesis.ThesisManageService;
import com.tdf.util.exceptions.KnowException;
import com.tdf.util.page.PagedList;
import com.tdf.util.web.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

}
