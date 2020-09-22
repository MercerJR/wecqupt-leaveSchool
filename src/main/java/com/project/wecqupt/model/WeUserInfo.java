package com.project.wecqupt.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/16 20:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeUserInfo implements Serializable {
    /**
    * openid
    */
    private String openid;

    /**
    * 学号
    */
    private String xh;

    /**
    * 姓名
    */
    private String name;

    private String sex;

    /**
    * 身份证号
    */
    private String sfzh;

    /**
    * 一卡通号
    */
    private String ykth;

    /**
    * 楼栋
    */
    private String build;

    /**
    * 宿舍号
    */
    private String room;

    /**
    * 学院名
    */
    private String yxm;

    /**
    * 用户类型 学生或者教师
    */
    private String type;

    private Date createTime;

    private String volunteerUid;

    private static final long serialVersionUID = 1L;
}