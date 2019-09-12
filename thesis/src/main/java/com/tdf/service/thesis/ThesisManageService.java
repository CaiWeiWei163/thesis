package com.tdf.service.thesis;

import com.tdf.dao.ThesisInfoMapper;
import com.tdf.entity.ThesisInfo;
import com.tdf.entity.criteria.ThesisInfoCriteria;
import com.tdf.util.page.PagedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
