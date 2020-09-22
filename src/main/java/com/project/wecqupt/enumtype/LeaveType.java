package com.project.wecqupt.enumtype;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 20:15
 */
public enum LeaveType {

    SAME_DAY_LEAVE(1,"市内当日离返校"),
    LONG_TIME_LEAVE(2,"长期离校申请");

    private final int code;
    private final String describe;

    LeaveType(int code,String describe){
        this.code = code;
        this.describe = describe;
    }

    public int getCode(){
        return this.code;
    }

    public String getDescribe(){
        return this.describe;
    }

}
