package com.project.wecqupt.exception;

/**
 * @author Mercer JR
 */

public enum CustomExceptionType {
    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(500,"系统内部错误"),
    /**
     * 参数校验错误
     */
    VALIDATE_ERROR(400,"参数校验错误"),
    /**
     * 未知错误
     */
    UNAUTHORIZED_ERROR(403,"用户未通过身份验证，拒绝访问");

    private Integer code;
    private String typeDesc;


    CustomExceptionType(Integer code, String typeDesc){
        this.code = code;
        this.typeDesc = typeDesc;
    }

    public String getTypeDesc(){
        return this.typeDesc;
    }

    public Integer getCode(){
        return this.code;
    }
}