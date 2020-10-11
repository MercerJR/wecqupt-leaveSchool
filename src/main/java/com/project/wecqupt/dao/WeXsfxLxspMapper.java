package com.project.wecqupt.dao;

import com.project.wecqupt.model.WeXsfxLxsp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 15:11
 */
public interface WeXsfxLxspMapper {
    int deleteByPrimaryKey(Integer logId);

    int insert(WeXsfxLxsp record);

    int insertSelective(WeXsfxLxsp record);

    WeXsfxLxsp selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(WeXsfxLxsp record);

    int updateByPrimaryKey(WeXsfxLxsp record);

    List<WeXsfxLxsp> selectByStudentNumber(String studentNumber);

    List<WeXsfxLxsp> selectByStudentNumberInPage(@Param("studentNumber") String studentNumber);

    int selectTotalNumber(@Param("studentNumber") String studentNumber);

    List<WeXsfxLxsp> selectByStudentNumberNotEnd(@Param("studentNumber") String studentNumber,
                                                 @Param("endState") String endState,
                                                 @Param("deniedState")String deniedState,
                                                 @Param("abnormalState") String abnormalState);

    void updateAbnormalRecords(@Param("abnormalRecords") List<Long> abnormalRecords,
                               @Param("state") String state,
                               @Param("stateCode") String stateCode,
                               @Param("updateTime") String updateTime);

    boolean updateStatusById(@Param("statusCode") String statusCode, @Param("describe") String statusDescribe,
                          @Param("operationType") String operationType, @Param("updateTime") String updateTime,
                          @Param("logId") long logId);

    List<WeXsfxLxsp> selectScanCodeByStudentNumber(String studentNumber);
}