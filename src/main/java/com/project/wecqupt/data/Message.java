package com.project.wecqupt.data;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/16 20:53
 */
public interface Message {
    String END = "结束";
    String NOT_APPLIED = "未申请";

    String UNAUTHORIZED = "学号和openid不对应，拒绝访问";
    String CONTACT_ADMIN = "系统内部错误，请联系管理员";
    String VALIDATE_ERROR = "传入参数错误";
    String RECORD_CAN_NOT_SCAN_CODE = "该申请记录不能扫码";
    String BEFORE_LEAVE_TIME = "没到扫码时间，不能扫码";

    String OPENID_EMPTY = "openid不能为空";
    String STUDENT_NUMBER_EMPTY = "学号不能为空";
    String DETAILED_AREA_BEYOND = "详细地址不能超过15个字符";
    String REASON_BEYOND = "原因不能超过30个字符";

    long GAP_MILES = 86400000;
}
