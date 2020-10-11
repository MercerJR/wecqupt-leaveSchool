package com.project.wecqupt.service;

import com.github.pagehelper.PageHelper;
import com.project.wecqupt.dao.RentalsListMapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 14:27
 */
@Service
public class ApprovalService {

    @Autowired
    private RentalsListMapper rentalsListMapper;

    @Autowired
    private WeXsfxLxspMapper mapper;

    @Autowired
    private ScanCodeLogMapper logMapper;

    public IndexStates getState(String studentNumber) {
        //TODO 从lxsp数据库里查找是否存在以下条件的记录：审批过了但未出校（待出校），审批过了出校了还未归校的（待入校），都没有（未申请）
        //TODO 查询we_xsfx_lxsp数据库中该学生最新的2条记录(因为单类互斥，双类不互斥)
        //TODO ，如果lczt字段存在“待入校”，则返回“待入校”，若无“待入校”但有“待出校”，那么就返回”待出校“，若都没有或查询为空，则返回“未申请”
        boolean rentalFlag = rentalsListMapper.selectByStudentNumber(studentNumber) != null;
        List<WeXsfxLxsp> records = mapper.selectByStudentNumber(studentNumber);
        IndexStates states = new IndexStates();
        if (records == null) {
            states.setProcessState(Message.NOT_APPLIED);
            return states;
        }
        //如果不是租房同学
        if (!rentalFlag) {
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
                    } else if (record.getQjlx().equals(LeaveType.LONG_TIME_LEAVE.getDescribe())) {
                        states.setLongApplyState(0);
                    }
                }
            }
        } else {
            long earlierMiles = 0;
            for (WeXsfxLxsp record : records) {
                if (record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode())
                        || record.getLcztdm().equals(ProcessStatusType.WAITING_TO_BACK.getCode())){
                    if (states.getProcessState() == null){
                        states.setProcessState(record.getLczt());
                        earlierMiles = record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe()) ?
                                DateFormatUtil.getMilesByDate(record.getWcrq()) + Message.GAP_MILES :
                                DateFormatUtil.getMilesByDetailedDate(record.getWcrq());
                    }else {
                        if (!record.getLczt().equals(states.getProcessState())){
                            long startMiles = record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe()) ?
                                    DateFormatUtil.getMilesByDate(record.getWcrq()) + Message.GAP_MILES :
                                    DateFormatUtil.getMilesByDetailedDate(record.getWcrq());
                            if (startMiles < earlierMiles){
                                states.setProcessState(record.getLczt());
                            }
                        }
                    }
                }

                if (!record.getLcztdm().equals(ProcessStatusType.PROCESS_END.getCode()) &&
                        !record.getLcztdm().equals(ProcessStatusType.PROCESS_ABNORMAL.getCode())) {
                    if (record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe())) {
                        states.setShortApplyState(0);
                    } else if (record.getQjlx().equals(LeaveType.LONG_TIME_LEAVE.getDescribe())) {
                        states.setLongApplyState(0);
                    }
                }
            }
        }

        if (states.getProcessState() == null) {
            states.setProcessState(Message.NOT_APPLIED);
        }
        return states;
    }

    /**
     * 申请离校审批
     */
    public void insertApplyRecord(ApplyRequest applyRequest, WeUserInfo userInfo, int type) {
        boolean rentalFlag = rentalsListMapper.selectByStudentNumber(userInfo.getXh()) != null;
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
                //如果是租房铜须，申请之后就是待入校状态，否则就是正常流程
                if (rentalFlag) {
                    record.setLcztdm(ProcessStatusType.WAITING_TO_BACK.getCode());
                    record.setLczt(ProcessStatusType.WAITING_TO_BACK.getDescribe());
                } else {
                    record.setLcztdm(ProcessStatusType.WAITING_TO_LEAVE.getCode());
                    record.setLczt(ProcessStatusType.WAITING_TO_LEAVE.getDescribe());
                }
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

    /**
     * 展示长期离校申请列表
     */
    public List<WeXsfxLxsp> getRecords(int currPage, String studentNumber) {
        PageHelper.startPage(currPage, 4);
        return mapper.selectByStudentNumberInPage(studentNumber);
    }

    /**
     * 获取离校申请总条数
     */
    public int getTotalNumber(String studentNumber) {
        return mapper.selectTotalNumber(studentNumber);
    }

    /**
     * 获取可扫码列表
     */
    public List<WeXsfxLxsp> getScanRecords(String studentNumber) {
        List<WeXsfxLxsp> records = mapper.selectScanCodeByStudentNumber(studentNumber);
        records.removeIf(record -> !record.getLcztdm().equals(ProcessStatusType.WAITING_TO_BACK.getCode()) &&
                !record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode()));
        return records;
    }

    /**
     * 扫码后更新申请记录信息
     */
    public void updateScanCodeRecord(ScanCodeRequest scanCodeRequest, WeUserInfo weUserInfo) {
        boolean rentalFlag = rentalsListMapper.selectByStudentNumber(weUserInfo.getXh()) != null;
        WeXsfxLxsp record = mapper.selectByPrimaryKey(scanCodeRequest.getLogId());
        //如果不是待出校和待入校的状态，就不允许该记录进行扫码
        if (!record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode()) &&
                !record.getLcztdm().equals(ProcessStatusType.WAITING_TO_BACK.getCode())) {
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.RECORD_CAN_NOT_SCAN_CODE);
        }
        //如果状态是待出校，状态就更新为待入校，并且更新操作改为出校
        //如果状态是待入校，状态就更新为结束，检测入校时间有无延迟，如果没问题，就更新操作改为入校，否则更新为入校-未按时返校
        //如果是租房同学，就反之，若租房同学没有按时出校，也让他正常扫码出校
        String logAction;
        String statusCode;
        String statusDescribe;
        String updateType;
        long currMiles = System.currentTimeMillis();
        String currDate = DateFormatUtil.getDetailedDateByMiles(currMiles);
        //待出校
        if (record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode())) {
            //如果是租房同学，那么出校就代表流程结束（租房同学超时了也可以出去）
            updateType = OperationType.OUT_OF_SCHOOL;
            logAction = ScanCodeAction.LEAVE_SCHOOL;
            if (rentalFlag) {
                statusCode = ProcessStatusType.PROCESS_END.getCode();
                statusDescribe = ProcessStatusType.PROCESS_END.getDescribe();
            } else {
                long startMiles = record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe()) ?
                        DateFormatUtil.getMilesByDate(record.getWcrq()) :
                        DateFormatUtil.getMilesByDetailedDate(record.getWcrq());
                //如果没到扫码时间，那就禁止通行并在数据库扫码日志里放一条扫码失败的记录
                if (currMiles < startMiles){
                    String result = ScanCodeAction.FAILED;
                    ScanCodeLog scanCodeLog = new ScanCodeLog(weUserInfo.getXh(),
                            weUserInfo.getName(), currDate, logAction, result);
                    logMapper.insert(scanCodeLog);
                    throw new CustomException(CustomExceptionType.VALIDATE_ERROR,Message.BEFORE_LEAVE_TIME);
                }
                statusCode = ProcessStatusType.WAITING_TO_BACK.getCode();
                statusDescribe = ProcessStatusType.WAITING_TO_BACK.getDescribe();
            }
        } else {
            //如果是租房同学，进校之后就变成待出校，并且不存在未按时进校的情况，因为未按时进校会直接-9
            if (rentalFlag) {
                statusCode = ProcessStatusType.WAITING_TO_LEAVE.getCode();
                statusDescribe = ProcessStatusType.WAITING_TO_LEAVE.getDescribe();
                updateType = OperationType.ENTER_SCHOOL;
            } else {
                long endMiles = record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe()) ?
                        DateFormatUtil.getMilesByDate(record.getYjfxsj()) + Message.GAP_MILES :
                        DateFormatUtil.getMilesByDetailedDate(record.getYjfxsj());
                statusCode = ProcessStatusType.PROCESS_END.getCode();
                statusDescribe = ProcessStatusType.PROCESS_END.getDescribe();
                updateType = currMiles > endMiles ?
                        OperationType.ENTER_SCHOOL_DELAY : OperationType.ENTER_SCHOOL;
            }
            logAction = ScanCodeAction.ENTER_SCHOOL;
        }
        boolean flag = mapper.updateStatusById(statusCode, statusDescribe, updateType, currDate, record.getLogId());
        String result = flag ? ScanCodeAction.SUCCESS : ScanCodeAction.FAILED;
        ScanCodeLog scanCodeLog = new ScanCodeLog(weUserInfo.getXh(), weUserInfo.getName(), currDate, logAction, result);
        logMapper.insert(scanCodeLog);
    }

    /**
     * 检查用户是否有超时未处理的申请
     */
    public void checkUpRecords(String studentNumber) {
        boolean rentalFlag = rentalsListMapper.selectByStudentNumber(studentNumber) != null;
        //查询该用户未结束和辅导员审批未通过的申请记录
        List<WeXsfxLxsp> records = mapper.selectByStudentNumberNotEnd(studentNumber,
                ProcessStatusType.PROCESS_END.getCode(), ProcessStatusType.INSTRUCTOR_DENIED.getCode(),
                ProcessStatusType.PROCESS_ABNORMAL.getCode());
        long currentMiles = System.currentTimeMillis();
        List<Long> abnormalRecords = new ArrayList<>();
        for (WeXsfxLxsp record : records) {
            //如果不是校外租房学生并且是待入校状态 以及 是校外租房学生并且是待出校状态，都不变为-9
            if (!rentalFlag && record.getLcztdm().equals(ProcessStatusType.WAITING_TO_BACK.getCode())){
                continue;
            }
            if (rentalFlag && record.getLcztdm().equals(ProcessStatusType.WAITING_TO_LEAVE.getCode())){
                continue;
            }
            long startTimeMiles;
            if (record.getQjlx().equals(LeaveType.SAME_DAY_LEAVE.getDescribe())) {
                startTimeMiles = DateFormatUtil.getMilesByDate(record.getWcrq());
            } else {
                startTimeMiles = DateFormatUtil.getMilesByDetailedDate(record.getWcrq());
            }
            if (currentMiles - Message.GAP_MILES > startTimeMiles) {
                abnormalRecords.add(Long.valueOf(record.getLogId()));
            }
        }
        if (abnormalRecords.size() != 0) {
            String currentTime = DateFormatUtil.getDetailedDateByMiles(currentMiles);
            mapper.updateAbnormalRecords(abnormalRecords, ProcessStatusType.PROCESS_ABNORMAL.getDescribe(),
                    ProcessStatusType.PROCESS_ABNORMAL.getCode(), currentTime);
        }
    }

    public int getRentalState(String studentNumber) {
        return rentalsListMapper.selectByStudentNumber(studentNumber) == null ? 0 : 1;
    }
}
