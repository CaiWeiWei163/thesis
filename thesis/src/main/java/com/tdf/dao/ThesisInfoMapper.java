package com.tdf.dao;

import com.tdf.entity.ThesisInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ThesisInfoMapper {

    /**
     * 统计总记录数
     *
     * @param map
     * @return
     */
    int getCountThesis(@Param(value = "map") Map<String, Object> map);

    /**
     * 论文列表
     *
     * @param topIndex
     * @param pageSize
     * @param whereMap
     * @return
     */
    List<ThesisInfo> listThesis(@Param("topIndex") int topIndex, @Param("pageSize") int pageSize,
                                @Param("map") Map<String, Object> whereMap);

    int deleteByPrimaryKey(String id);

    int insert(ThesisInfo record);

    int insertSelective(ThesisInfo record);

    ThesisInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ThesisInfo record);

    int updateByPrimaryKeyWithBLOBs(ThesisInfo record);

    int updateByPrimaryKey(ThesisInfo record);
}