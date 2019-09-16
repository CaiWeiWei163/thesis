//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tdf.controller.sys;

import com.tdf.controller.base.BaseController;
import com.tdf.entity.sys.SysDict;
import com.tdf.models.LayuiDataTableModel;
import com.tdf.service.sys.SysDictService;
import com.tdf.service.sys.criterias.SysDictPagedCriteria;
import com.tdf.util.StringUtil;
import com.tdf.util.exceptions.KnowException;
import com.tdf.util.page.PagedList;
import com.tdf.util.web.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping({"/sysDict"})
public class SysDictController extends BaseController {
    @Autowired
    private SysDictService dictService;

    public SysDictController() {
    }

    @RequestMapping({"", "/index"})
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("sys/sysDict/index");
        return mv;
    }

    @RequestMapping({"/dictItem"})
    public ModelAndView dictItem(String pid) {
        if (StringUtil.isEmpty(pid)) {
            throw new KnowException("必须传递参数 pid");
        } else {
            SysDict dictTypeModel = this.dictService.findById(pid);
            if (dictTypeModel == null) {
                throw new KnowException("未找到该字典类型");
            } else {
                ModelAndView mv = new ModelAndView("sys/sysDict/dictItem");
                mv.addObject("dictTypeModel", dictTypeModel);
                return mv;
            }
        }
    }

    @RequestMapping({"/ajax/pagedListByDictType"})
    public ActionResult pagedListByDictType(SysDictPagedCriteria criteria) {
        criteria.setpKey("");
        PagedList<SysDict> pagedList = this.dictService.findByPagedList(criteria);
        return LayuiDataTableModel.layuiDataTable(pagedList);
    }

    @RequestMapping({"/ajax/pagedListByDictItem"})
    public ActionResult pagedListByDictItem(SysDictPagedCriteria criteria) {
        PagedList<SysDict> pagedList = this.dictService.findByPagedList(criteria);
        return LayuiDataTableModel.layuiDataTable(pagedList);
    }

    @RequestMapping({"/ajax/loadDictItem"})
    public ActionResult loadDictItem(String pKey) {
        SysDictPagedCriteria criteria = new SysDictPagedCriteria();
        criteria.setPageSize(-1);
        criteria.setpKey(pKey);
        PagedList<SysDict> pagedList = this.dictService.findByPagedList(criteria);
        return this.success(pagedList.getData());
    }

    @RequestMapping(
            value = {"/ajax/saveSysDictType"},
            method = {RequestMethod.POST}
    )
    public ActionResult saveSysDictType(SysDict dict) {
        try {
            this.dictService.addDictType(dict);
            return this.success();
        } catch (KnowException var3) {
            return this.fail(var3.getMessage());
        }
    }

    @RequestMapping(
            value = {"/ajax/saveSysDictItem"},
            method = {RequestMethod.POST}
    )
    public ActionResult saveSysDict(SysDict dict) {
        try {
            this.dictService.addDictItem(dict);
            return this.success();
        } catch (KnowException var3) {
            return this.fail(var3.getMessage());
        }
    }

    @RequestMapping(
            value = {"/ajax/updateSysDict"},
            method = {RequestMethod.POST}
    )
    public ActionResult updateSysDict(SysDict dict) {
        try {
            this.dictService.update(dict);
            return this.success();
        } catch (KnowException var3) {
            return this.fail(var3.getMessage());
        }
    }

    @RequestMapping(
            value = {"/ajax/delSysDict"},
            method = {RequestMethod.POST}
    )
    public ActionResult delSysDict(String id) {
        try {
            this.dictService.delDict(id);
            return this.success();
        } catch (KnowException var3) {
            return this.fail(var3.getMessage());
        }
    }
}
