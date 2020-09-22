package com.project.wecqupt.service;

import com.project.wecqupt.dao.WeXsfxLxspMapper;
import com.project.wecqupt.data.Message;
import com.project.wecqupt.dao.WeUserInfoMapper;
import com.project.wecqupt.data.WeValidate;
import com.project.wecqupt.enumtype.LeaveType;
import com.project.wecqupt.enumtype.ProcessStatusType;
import com.project.wecqupt.exception.CustomException;
import com.project.wecqupt.exception.CustomExceptionType;
import com.project.wecqupt.model.WeUserInfo;
import com.project.wecqupt.model.WeXsfxLxsp;
import com.project.wecqupt.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/16 20:47
 */
@Service
public class WeUserService {

    @Autowired
    private WeUserInfoMapper mapper;

    @Autowired
    private WeXsfxLxspMapper recordMapper;

    public WeUserInfo validateOpenid(WeValidate weValidate) {
        WeUserInfo weUserInfo = mapper.selectByPrimaryKey(weValidate.getOpenid(), weValidate.getStudentNumber());
        if (weUserInfo == null) {
            throw new CustomException(CustomExceptionType.UNAUTHORIZED_ERROR, Message.UNAUTHORIZED);
        }
        return weUserInfo;
    }

    public WeUserInfo validateOpenid(String openid, String studentNumber) {
        WeUserInfo weUserInfo = mapper.selectByPrimaryKey(openid, studentNumber);
        if (weUserInfo == null) {
            throw new CustomException(CustomExceptionType.UNAUTHORIZED_ERROR, Message.UNAUTHORIZED);
        }
        return weUserInfo;
    }

    public void checkUpRecords(String studentNumber) {
        List<WeXsfxLxsp> records = recordMapper.selectByStudentNumberNotEnd(studentNumber,
                ProcessStatusType.PROCESS_END.getCode(),ProcessStatusType.INSTRUCTOR_DENIED.getCode(),
                ProcessStatusType.PROCESS_ABNORMAL.getCode());
        long currentMiles = System.currentTimeMillis();
        List<Long> abnormalRecords = new ArrayList<>();
        for (WeXsfxLxsp record : records) {
            long startTimeMiles;
            if (record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe())){
                startTimeMiles = DateFormatUtil.getMilesByDate(record.getWcrq());
            }else {
                startTimeMiles = DateFormatUtil.getMilesByDetailedDate(record.getWcrq());
            }
            if (currentMiles - Message.GAP_MILES > startTimeMiles) {
                abnormalRecords.add(Long.valueOf(record.getLogId()));
            }
        }
        if (abnormalRecords.size() != 0){
            String currentTime = DateFormatUtil.getDetailedDateByMiles(currentMiles);
            recordMapper.updateAbnormalRecords(abnormalRecords, ProcessStatusType.PROCESS_ABNORMAL.getDescribe(),
                    ProcessStatusType.PROCESS_ABNORMAL.getCode(),currentTime);
        }
    }
}
