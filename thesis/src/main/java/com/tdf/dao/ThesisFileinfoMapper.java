package com.tdf.dao;

import com.tdf.entity.ThesisFileinfo;

public interface ThesisFileinfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ThesisFileinfo record);

    int insertSelective(ThesisFileinfo record);

    ThesisFileinfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ThesisFileinfo record);

    int updateByPrimaryKey(ThesisFileinfo record);
}