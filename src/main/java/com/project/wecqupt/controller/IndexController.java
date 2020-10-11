package com.project.wecqupt.controller;

import com.project.wecqupt.data.*;
import com.project.wecqupt.model.WeUserInfo;
import com.project.wecqupt.service.ApprovalService;
import com.project.wecqupt.service.WeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/16 20:35
 */
@RestController
@Slf4j
@Validated
public class IndexController {

    @Autowired
    private WeUserService weUserService;

    @Autowired
    private ApprovalService approvalService;

    @GetMapping("/index")
    public Response index(@RequestParam(HttpInfo.OPENID) String openid,
                          @RequestParam(HttpInfo.STUDENT_NUMBER) String studentNumber) {
        log.info("openid:" + openid);
        log.info("学号：" + studentNumber);
        WeValidate weValidate = new WeValidate(openid, studentNumber);
        WeUserInfo weUserInfo = weUserService.validateOpenid(weValidate);
        approvalService.checkUpRecords(studentNumber);
        IndexStates states = approvalService.getState(weUserInfo.getXh());
        StateResponse stateResponse = new StateResponse(states.getProcessState(),
                weUserInfo.getName(), weUserInfo.getXh(), weUserInfo.getYxm(),
                states.getShortApplyState(), states.getLongApplyState(),
                approvalService.getRentalState(studentNumber));
        Response response = new Response().success(stateResponse);
        log.info(response.toString());
        return response;
    }
}
