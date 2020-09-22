package com.project.wecqupt.controller;

import com.project.wecqupt.data.*;
import com.project.wecqupt.enumtype.LeaveType;
import com.project.wecqupt.model.WeUserInfo;
import com.project.wecqupt.service.ApprovalService;
import com.project.wecqupt.service.WeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 18:15
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    private WeUserService weUserService;

    @Autowired
    private ApprovalService approvalService;

    @PostMapping(value = "/shortTimeApply")
    public Response shortTimeApply(@RequestBody ApplyRequest applyRequest) {
        WeUserInfo weUserInfo = weUserService.validateOpenid(
                applyRequest.getOpenid(),applyRequest.getStudentNumber());
        approvalService.insertApplyRecord(applyRequest, weUserInfo, LeaveType.SAME_DAY_LEAVE.getCode());
        return new Response().success();
    }

    @PostMapping(value = "/longTimeApply")
    public Response longTimeApply(@RequestBody ApplyRequest applyRequest) {
        WeUserInfo weUserInfo = weUserService.validateOpenid(
                applyRequest.getOpenid(), applyRequest.getStudentNumber());
        approvalService.insertApplyRecord(applyRequest, weUserInfo, LeaveType.LONG_TIME_LEAVE.getCode());
        return new Response().success();
    }

    @GetMapping(value = "/selectRecords")
    public Response applyRecord(@RequestParam("openid") String openid,
                                @RequestParam("studentNumber") String studentNumber,
                                @RequestParam("currPage") int currPage) {
        weUserService.validateOpenid(openid, studentNumber);
        RecordResponse recordResponse = new RecordResponse(approvalService.getTotalNumber(
                studentNumber), approvalService.getRecords(currPage, studentNumber));
        return new Response().success(recordResponse);
    }

    @GetMapping(value = "/scanList")
    public Response scanList(@RequestParam("openid") String openid,
                             @RequestParam("studentNumber") String studentNumber){
        weUserService.validateOpenid(openid, studentNumber);
        return new Response().success(approvalService.getScanRecords(studentNumber));
    }

    @PostMapping(value = "/scanCode")
    public Response scanCodeLeave(@RequestBody ScanCodeRequest scanCodeRequest) {
        WeUserInfo weUserInfo = weUserService.validateOpenid(scanCodeRequest.getOpenid(),
                scanCodeRequest.getStudentNumber());
        approvalService.updateScanCodeRecord(scanCodeRequest,weUserInfo);
        return new Response().success();
    }
}
