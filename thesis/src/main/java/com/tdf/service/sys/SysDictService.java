//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tdf.service.sys;

import com.tdf.common.ContextHolder;
import com.tdf.dao.sys.SysDictMapper;
import com.tdf.entity.sys.SysDict;
import com.tdf.entity.sys.SysUser;
import com.tdf.service.sys.criterias.SysDictPagedCriteria;
import com.tdf.util.SimpleKeyValue;
import com.tdf.util.StringUtil;
import com.tdf.util.exceptions.KnowException;
import com.tdf.util.page.PagedList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(
        rollbackForClassName = {"Exception.class"}
)
public class SysDictService {
    @Autowired
    private SysDictMapper sysDictMapper;

    public SysDictService() {
    }

    public int getMaxSort(String pKey) {
        pKey = pKey == null ? "" : pKey.trim();
        Integer maxSort = this.sysDictMapper.getMaxSort(pKey);
        return maxSort == null ? 0 : maxSort;
    }

    public void addDictType(SysDict dictType) {
        dictType.setDictparentkey("");
        this.addSysDict(dictType);
    }

    public void addDictItem(SysDict dictItem) {
        if (StringUtil.isEmpty(dictItem.getDictparentkey())) {
            this.addDictType(dictItem);
        } else {
            String itemKey = dictItem.getDictparentkey() + "_" + dictItem.getDictkey();
            dictItem.setDictkey(itemKey);
            this.addSysDict(dictItem);
        }
    }

    private void addSysDict(SysDict dict) {
        if (!StringUtil.isEmpty(dict.getDictkey())) {
            SysUser currentLoinUser = ContextHolder.getLoginSysUserInfo();
            int maxSort = this.getMaxSort(dict.getDictparentkey());
            dict.setSort(maxSort + 1);
            dict.created(ContextHolder.getLoginSysUserInfo().getRealname());
            if (!this.sysDictMapper.isExist(dict.getDictkey(), (String) null)) {
                this.sysDictMapper.insert(dict);
            } else {
                throw new KnowException(dict.getDictvalue() + " 已存在,请重新输入！");
            }
        }
    }

    public void delDict(String id) {
        SysDict dict = this.sysDictMapper.selectByPrimaryKey(id);
        if (dict != null) {
            if ("".equals(dict.getDictparentkey())) {
                this.sysDictMapper.deleteDictTypeByKey(dict.getDictkey());
            } else {
                this.sysDictMapper.deleteByPrimaryKey(dict.getId());
            }
        }

    }

    public void update(SysDict dict) {
        SysUser currentLoinUser = ContextHolder.getLoginSysUserInfo();
        SysDict entity = this.sysDictMapper.selectByPrimaryKey(dict.getId());
        if (entity != null) {
            entity.setUpdatetime(new Date());
            entity.setUpdateuser(currentLoinUser.getId());
            entity.setDictkey(dict.getDictparentkey() + "_" + dict.getDictvalue());
            entity.setDictvalue(dict.getDictvalue());
            entity.setNote(dict.getNote());
            if (this.sysDictMapper.isExist(entity.getDictkey(), entity.getId())) {
                throw new KnowException(dict.getDictvalue() + "已存在,请重新输入！");
            }
            this.sysDictMapper.updateByPrimaryKey(entity);
        }

    }

    public PagedList<SysDict> findByPagedList(SysDictPagedCriteria criteria) {
        Map<String, Object> map = criteria.toWhereMap();
        Integer total = 0;
        if (criteria.getPageSize() > 0) {
            total = this.sysDictMapper.countByPaged(criteria.getpKey(), map);
            if (total == 0) {
                return new PagedList((List) null, criteria.getPageNo(), total);
            }
        }

        List<SysDict> list = this.sysDictMapper.findByPaged(criteria.getpKey(), criteria.getTopIndex(), criteria.getPageSize(), map);
        return new PagedList(list, criteria.getPageNo(), total);
    }

    public SysDict findByKey(String key) {
        return this.sysDictMapper.findDictByKey(key);
    }

    public SysDict findById(String id) {
        return this.sysDictMapper.selectByPrimaryKey(id);
    }

    public List<SimpleKeyValue> getKeyValuesByKeys(List<String> keys) {
        return this.sysDictMapper.getKeyValuesByKeys(keys);
    }

    public SimpleKeyValue getKeyValueByKey(String key) {
        List<String> keys = new ArrayList();
        keys.add(key);
        List<SimpleKeyValue> list = this.getKeyValuesByKeys(keys);
        return list != null && list.size() != 0 ? (SimpleKeyValue) list.get(0) : null;
    }
}
