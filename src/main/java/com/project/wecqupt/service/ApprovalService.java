package com.project.wecqupt.service;

import com.github.pagehelper.PageHelper;
import com.project.wecqupt.dao.ScanCodeLogMapper;
import com.project.wecqupt.dao.WeXsfxLxspMapper;
import com.project.wecqupt.data.ApplyRequest;
import com.project.wecqupt.data.IndexStates;
import com.project.wecqupt.data.Message;
import com.project.wecqupt.data.ScanCodeRequest;
import com.project.wecqupt.enumtype.LeaveType;
import com.project.wecqupt.enumtype.OperationType;
import com.project.wecqupt.enumtype.ProcessStatusType;
import com.project.wecqupt.enumtype.ScanCodeAction;
import com.project.wecqupt.exception.CustomException;
import com.project.wecqupt.exception.CustomExceptionType;
import com.project.wecqupt.model.ScanCodeLog;
import com.project.wecqupt.model.WeUserInfo;
import com.project.wecqupt.model.WeXsfxLxsp;
import com.project.wecqupt.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 14:27
 */
@Service
public class ApprovalService {

    @Autowired
    private WeXsfxLxspMapper mapper;

    @Autowired
    private ScanCodeLogMapper logMapper;

    public IndexStates getState(String studentNumber) {
        //TODO 从lxsp数据库里查找是否存在以下条件的记录：审批过了但未出校（待出校），审批过了出校了还未归校的（待入校），都没有（未申请）
        //TODO 查询we_xsfx_lxsp数据库中该学生最新的2条记录(因为单类互斥，双类不互斥)
        //TODO ，如果lczt字段存在“待入校”，则返回“待入校”，若无“待入校”但有“待出校”，那么就返回”待出校“，若都没有或查询为空，则返回“未申请”
        List<WeXsfxLxsp> records = mapper.selectByStudentNumber(studentNumber);
        IndexStates states = new IndexStates();
        if (records == null) {
            states.setProcessState(Message.NOT_APPLIED);
            return states;
        }
        for (WeXsfxLxsp record : records) {
            if (states.getProcessState() == null ||
                    !states.getProcessState().equals(ProcessStatusType.WAITING_TO_BACK.getDescribe())) {
                if (record.getLcztdm().equals(ProcessStatusType.WAITING_TO_BACK.getCode())) {
                    states.setProcessState(ProcessStatusType.WAITING_TO_BACK.getDescribe());
                } else if (record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode())) {
                    states.setProcessState(ProcessStatusType.WAITING_TO_LEAVE.getDescribe());
                }
            }
            if (!record.getLcztdm().equals(ProcessStatusType.PROCESS_END.getCode()) &&
                    !record.getLcztdm().equals(ProcessStatusType.PROCESS_ABNORMAL.getCode())) {
                if (record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe())) {
                    states.setShortApplyState(0);
                } else if (record.getQjlx().equals(LeaveType.LONG_TIME_LEAVE.getDescribe())){
                    states.setLongApplyState(0);
                }
            }
        }
        if (states.getProcessState() == null) {
            states.setProcessState(Message.NOT_APPLIED);
        }
        return states;
    }

    public void insertApplyRecord(ApplyRequest applyRequest, WeUserInfo userInfo, int type) {
        WeXsfxLxsp record = new WeXsfxLxsp();
        String detailedTime = DateFormatUtil.getDetailedDateByMiles(System.currentTimeMillis());
        record.setXh(userInfo.getXh());
        record.setCreatedAt(detailedTime);
        record.setName(userInfo.getName());
        record.setXy(userInfo.getYxm());
        record.setNj(userInfo.getXh().substring(0, 4));
        record.setWcmdd(applyRequest.getArea());
        record.setWcxxdd(applyRequest.getDetailedArea());
        record.setQjsy(applyRequest.getReason());
        record.setGxsj(detailedTime);
        switch (type) {
            //市内当日离返校
            case 1:
                String today = DateFormatUtil.getDateByMiles(System.currentTimeMillis());
                record.setWcrq(today);
                record.setYjfxsj(today);
                record.setLcztdm(ProcessStatusType.WAITING_TO_LEAVE.getCode());
                record.setLczt(ProcessStatusType.WAITING_TO_LEAVE.getDescribe());
                record.setGxcz(OperationType.SUBMIT_APPLY);
                record.setQjlx(LeaveType.SAME_DAY_LEAVE.getDescribe());
                mapper.insertSelective(record);
                break;
            //长期离返校
            case 2:
                record.setWcrq(applyRequest.getStartTime());
                record.setYjfxsj(applyRequest.getEndTime());
                record.setLcztdm(ProcessStatusType.WAIT_INSTRUCTOR_APPROVAL.getCode());
                record.setLczt(ProcessStatusType.WAIT_INSTRUCTOR_APPROVAL.getDescribe());
                record.setGxcz(OperationType.SUBMIT_APPLY);
                record.setQjlx(LeaveType.LONG_TIME_LEAVE.getDescribe());
                mapper.insertSelective(record);
                break;
            default:
                throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.VALIDATE_ERROR);
        }
    }

    public List<WeXsfxLxsp> getRecords(int currPage, String studentNumber) {
        PageHelper.startPage(currPage, 4);
        return mapper.selectByStudentNumberInPage(studentNumber, LeaveType.LONG_TIME_LEAVE.getDescribe());
    }

    public int getTotalNumber(String studentNumber) {
        return mapper.selectTotalNumber(studentNumber, LeaveType.LONG_TIME_LEAVE.getDescribe());
    }

    public List<WeXsfxLxsp> getScanRecords(String studentNumber) {
        List<WeXsfxLxsp> records = mapper.selectScanCodeByStudentNumber(studentNumber);
        records.removeIf(record -> !record.getLcztdm().equals(ProcessStatusType.WAITING_TO_BACK.getCode()) &&
                !record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode()));
        return records;
    }

    public void updateScanCodeRecord(ScanCodeRequest scanCodeRequest, WeUserInfo weUserInfo) {
        WeXsfxLxsp record = mapper.selectByPrimaryKey(scanCodeRequest.getLogId());
        if (!record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode()) &&
                !record.getLcztdm().equals(ProcessStatusType.WAITING_TO_BACK.getCode())) {
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.RECORD_CAN_NOT_SCAN_CODE);
        }
        //如果状态是待出校，状态就更新为待入校，并且更新操作改为出校
        //如果状态是待入校，状态就更新为结束，检测入校时间有无延迟，如果没问题，就更新操作改为入校，否则更新未入校-未按时返校
        String logAction;
        String statusCode;
        String statusDescribe;
        String updateType;
        long currMiles = System.currentTimeMillis();
        String currDate = DateFormatUtil.getDetailedDateByMiles(currMiles);
        //待出校
        if (record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode())) {
            statusCode = ProcessStatusType.WAITING_TO_BACK.getCode();
            statusDescribe = ProcessStatusType.WAITING_TO_BACK.getDescribe();
            updateType = OperationType.OUT_OF_SCHOOL;
            logAction = ScanCodeAction.LEAVE_SCHOOL;
        } else {
            long endMiles = record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe()) ?
                    DateFormatUtil.getMilesByDate(record.getYjfxsj()) + Message.GAP_MILES :
                    DateFormatUtil.getMilesByDetailedDate(record.getYjfxsj());
            statusCode = ProcessStatusType.PROCESS_END.getCode();
            statusDescribe = ProcessStatusType.PROCESS_END.getDescribe();
            updateType = currMiles > endMiles ?
                    OperationType.ENTER_SCHOOL_DELAY : OperationType.ENTER_SCHOOL;
            logAction = ScanCodeAction.ENTER_SCHOOL;
        }
        boolean flag = mapper.updateStatusById(statusCode, statusDescribe, updateType, currDate, record.getLogId());
        String result = flag ? ScanCodeAction.SUCCESS : ScanCodeAction.FAILED;
        ScanCodeLog scanCodeLog = new ScanCodeLog(weUserInfo.getXh(), weUserInfo.getName(), currDate, logAction, result);
        logMapper.insert(scanCodeLog);
    }
}
