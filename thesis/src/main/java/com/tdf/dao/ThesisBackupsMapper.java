package com.tdf.dao;

import com.tdf.entity.ThesisBackups;

public interface ThesisBackupsMapper {
    int deleteByPrimaryKey(String id);

    int insert(ThesisBackups record);

    int insertSelective(ThesisBackups record);

    ThesisBackups selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ThesisBackups record);

    int updateByPrimaryKey(ThesisBackups record);
}