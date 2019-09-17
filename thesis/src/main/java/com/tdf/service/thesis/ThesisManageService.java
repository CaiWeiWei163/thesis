package com.tdf.service.thesis;

import com.tdf.common.ContextHolder;
import com.tdf.dao.ThesisInfoMapper;
import com.tdf.entity.ThesisInfo;
import com.tdf.entity.criteria.ThesisInfoCriteria;
import com.tdf.entity.sys.SysDict;
import com.tdf.util.StringUuid;
import com.tdf.util.page.PagedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cww
 * @create 2019-09-12 15:13
 */
@Service
@Transactional(rollbackForClassName = "Exception.class")
public class ThesisManageService {

    @Autowired
    ThesisInfoMapper thesisInfoMapper;

    // 论文列表
    public PagedList<ThesisInfo> listThesis(ThesisInfoCriteria criteria) {
        // 处理时间
        String datesearch = criteria.getDatesearch();
        if ("".equals(datesearch) || datesearch == null) {
            criteria.setDatesearch(null);
        } else {
            String datesearchstart = datesearch.substring(0, 10);
            String datesearchend = datesearch.substring(13, 23);
            criteria.setDatesearchstart(datesearchstart);
            criteria.setDatesearchend(datesearchend + " 23:59:59");
        }

        Map<String, Object> map = criteria.toWhereMap();
        Integer total = 0;
        if (criteria.getPageSize() > 0) {
            total = thesisInfoMapper.getCountThesis(map);
            if (total == 0) {
                return new PagedList<>(null, criteria.getPageNo(), total);
            }
        }
        List<ThesisInfo> list = thesisInfoMapper.listThesis(criteria.getTopIndex(),
                criteria.getPageSize(), map);
        return new PagedList<>(list, criteria.getPageNo(), total);
    }

    /**
     * 添加论文
     *
     * @param thesisInfo
     */
    public void addThesis(ThesisInfo thesisInfo) {
        thesisInfo.setId(StringUuid.getUuid());
        thesisInfo.setCreateTime(new Date());
        thesisInfo.setCreateUser(ContextHolder.getLoginSysUserInfo().getRealname());
        thesisInfoMapper.insertSelective(thesisInfo);
    }

    /**
     * 编辑论文
     *
     * @param thesisInfo
     */
    public void editThesis(ThesisInfo thesisInfo) {
        thesisInfo.setUpdateTime(new Date());
        thesisInfo.setUpdateUser(ContextHolder.getLoginSysUserInfo().getRealname());
        thesisInfoMapper.updateByPrimaryKeySelective(thesisInfo);
    }

    // 根据父级key查询子级列表
    public List<SysDict> selectSysDictByParentKey(String dictParentKey) {
        return thesisInfoMapper.selectSysDictByParentKey(dictParentKey);
    }

    /**
     * 模糊搜索
     *
     * @param search
     * @return
     */
    public List<SysDict> fuzzySearch(String search, String type) {
        List<SysDict> list = thesisInfoMapper.fuzzySearch(search, type);
        return list;
    }

    /**
     * 根据id查询信息
     *
     * @param id
     */
    public ThesisInfo getThesisInfoById(String id) {
        return thesisInfoMapper.selectByPrimaryKey(id);
    }
}
