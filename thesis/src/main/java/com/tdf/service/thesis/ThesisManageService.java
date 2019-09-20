package com.tdf.service.thesis;

import com.tdf.common.ContextHolder;
import com.tdf.dao.ThesisInfoMapper;
import com.tdf.entity.ThesisBackups;
import com.tdf.entity.ThesisInfo;
import com.tdf.entity.criteria.ThesisInfoCriteria;
import com.tdf.entity.sys.SysDict;
import com.tdf.util.StringUuid;
import com.tdf.util.page.PagedList;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        // 优化列表显示
        if (CollectionUtils.isNotEmpty(list)) {
            for (ThesisInfo thesisInfo : list) {
                replaceView(thesisInfo, "author");
                replaceView(thesisInfo, "type");
                replaceView(thesisInfo, "keyword");
                replaceView(thesisInfo, "literature");
            }
        }
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
        // TODO 论文原文件的编辑
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
        // 详情页面显示优化
        ThesisInfo thesisInfo = thesisInfoMapper.selectByPrimaryKey(id);
        replaceView(thesisInfo, "author");
        replaceView(thesisInfo, "type");
        replaceView(thesisInfo, "keyword");
        replaceView(thesisInfo, "literature");
        return thesisInfo;
    }

    /**
     * 优化页面显示
     *
     * @param thesisInfo
     * @param str
     */
    public void replaceView(ThesisInfo thesisInfo, String str) {
        if ("author".equals(str)) {
            // 作者
            String author = thesisInfo.getAuthor();
            if (StringUtils.isNotEmpty(author)) {
                String replace = author.replace(",", "、 ");
                thesisInfo.setAuthorStr(replace);
            }
        } else if ("type".equals(str)) {
            // 分类
            String type = thesisInfo.getType();
            if (StringUtils.isNotEmpty(type)) {
                String replace = type.replace(",", "、 ");
                thesisInfo.setTypeStr(replace);
            }
        } else if ("keyword".equals(str)) {
            // 关键字
            String keyword = thesisInfo.getKeyword();
            if (StringUtils.isNotEmpty(keyword)) {
                String replace = keyword.replace(",", "、 ");
                thesisInfo.setKeywordStr(replace);
            }
        } else if ("literature".equals(str)) {
            // 引证文献
            String literature = thesisInfo.getLiterature();
            if (StringUtils.isNotEmpty(literature)) {
                String replace = literature.replace(",", "、 ");
                thesisInfo.setLiteratureStr(replace);
            }
        }
    }
}
