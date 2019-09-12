package com.tdf.dao;

import com.tdf.entity.ThesisInfo;

public interface ThesisInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ThesisInfo record);

    int insertSelective(ThesisInfo record);

    ThesisInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ThesisInfo record);

    int updateByPrimaryKeyWithBLOBs(ThesisInfo record);

    int updateByPrimaryKey(ThesisInfo record);
}