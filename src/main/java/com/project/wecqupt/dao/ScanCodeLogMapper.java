package com.project.wecqupt.dao;

import com.project.wecqupt.model.ScanCodeLog;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/19 16:21
 */
public interface ScanCodeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScanCodeLog record);

    int insertSelective(ScanCodeLog record);

    ScanCodeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScanCodeLog record);

    int updateByPrimaryKey(ScanCodeLog record);
}