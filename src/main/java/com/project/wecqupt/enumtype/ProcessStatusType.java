package com.project.wecqupt.enumtype;

import lombok.Data;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 19:04
 */
public enum  ProcessStatusType {

    WAIT_INSTRUCTOR_APPROVAL(1,"待辅导员审批"),
    WAIT_LEADER_APPROVAL(2,"待分管领导审批"),
    WAITING_TO_LEAVE(3,"待出校"),
    WAITING_TO_BACK(4,"待入校"),
    PROCESS_END(5,"结束"),
    INSTRUCTOR_DENIED(-1,"辅导员审批不通过"),
    LEADER_DENIED(-2,"分管领导审批不通过"),
    PROCESS_ABNORMAL(-9,"流程未正常结束，超时48小时自动结束");


    private final String code;
    private final String describe;

    ProcessStatusType(int code,String describe){
        this.code = String.valueOf(code);
        this.describe = describe;
    }

    public String getCode(){
        return this.code;
    }

    public String getDescribe(){
        return this.describe;
    }
}
