package com.tdf.dao;

import com.tdf.entity.ThesisBackups;
import com.tdf.entity.ThesisInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ThesisBackupsMapper {

    /**
     * 统计总记录数
     *
     * @param map
     * @return
     */
    int getCountBackups(@Param(value = "map") Map<String, Object> map);

    /**
     * 论文列表
     *
     * @param topIndex
     * @param pageSize
     * @param whereMap
     * @return
     */
    List<ThesisBackups> listBackups(@Param("topIndex") int topIndex, @Param("pageSize") int pageSize,
                                    @Param("map") Map<String, Object> whereMap);

    int deleteByPrimaryKey(String id);

    int insert(ThesisBackups record);

    int insertSelective(ThesisBackups record);

    ThesisBackups selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ThesisBackups record);

    int updateByPrimaryKey(ThesisBackups record);
}