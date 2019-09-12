package com.tdf.controller;

import com.tdf.common.ContextHolder;
import com.tdf.controller.base.BaseController;
import com.tdf.entity.sys.SysDict;
import com.tdf.service.sys.SysDictService;
import com.tdf.util.StringUtil;
import com.tdf.util.exceptions.KnowException;
import com.tdf.util.web.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * 类型管理、作者管理、关键字管理、引证文献管理
 *
 * @author cww
 * @create 2019-09-11 16:15
 */
@RestController
@RequestMapping("/dictManage")
public class DictManageController extends BaseController {

    @Autowired
    private SysDictService dictService;

    /**
     * 参数校验
     *
     * @param pid
     */
    public void parameterCalibration(String pid) {
        if (StringUtil.isEmpty(pid)) {
            throw new KnowException("必须传递参数 pid");
        }
        SysDict dictTypeModel = dictService.findById(pid);
        if (dictTypeModel == null) {
            throw new KnowException("未找到该字典类型");
        }
    }

    /**
     * 类型管理列表
     *
     * @param pid
     * @return
     */
    @RequestMapping("/typeManage")
    public ModelAndView typeManage(String pid) {
        SysDict dictTypeModel = dictService.findById(pid);
        // 参数校验
        parameterCalibration(pid);
        ModelAndView mv = new ModelAndView("dict/typemanage/dictItem");
        mv.addObject("dictTypeModel", dictTypeModel);
        return mv;
    }

    /**
     * 作者管理列表
     *
     * @param pid
     * @return
     */
    @RequestMapping("/authorManage")
    public ModelAndView authorManage(String pid) {
        SysDict dictTypeModel = dictService.findById(pid);
        // 参数校验
        parameterCalibration(pid);
        ModelAndView mv = new ModelAndView("dict/authormanage/dictItem");
        mv.addObject("dictTypeModel", dictTypeModel);
        return mv;
    }

    /**
     * 关键字管理列表
     *
     * @param pid
     * @return
     */
    @RequestMapping("/keywordManage")
    public ModelAndView keywordManage(String pid) {
        SysDict dictTypeModel = dictService.findById(pid);
        // 参数校验
        parameterCalibration(pid);
        ModelAndView mv = new ModelAndView("dict/keywordmanage/dictItem");
        mv.addObject("dictTypeModel", dictTypeModel);
        return mv;
    }

    /**
     * 引征文献管理列表
     *
     * @param pid
     * @return
     */
    @RequestMapping("/literatureManage")
    public ModelAndView literatureManage(String pid) {
        SysDict dictTypeModel = dictService.findById(pid);
        // 参数校验
        parameterCalibration(pid);
        ModelAndView mv = new ModelAndView("dict/literaturemanage/dictItem");
        mv.addObject("dictTypeModel", dictTypeModel);
        return mv;
    }

    /**
     * 添加类型
     *
     * @param dict
     * @return
     */
    @RequestMapping(value = "/ajax/saveSysDictItem", method = RequestMethod.POST)
    public ActionResult saveSysDict(SysDict dict) {
        try {
            dict.setDictvalue(dict.getDictkey());
            dict.setCreatetime(new Date());
            dict.setCreateuser(ContextHolder.getLoginSysUserInfo().getRealname());
            dictService.addDictItem(dict);
            return success();
        } catch (KnowException e) {
            return fail(e.getMessage());
        }
    }

}
