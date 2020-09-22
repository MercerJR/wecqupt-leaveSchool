package com.project.wecqupt.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 15:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeXsfxLxsp implements Serializable {
    /**
    * 数据库自动生成id
    */
    private Integer logId;

    /**
    * 学号
    */
    private String xh;

    /**
    * 申请提交时间
    */
    private String createdAt;

    /**
    * 姓名
    */
    private String name;

    /**
    * 学院
    */
    private String xy;

    /**
    * 年级
    */
    private String nj;

    /**
    * 外出地点（省市区）
    */
    private String wcmdd;

    /**
    * 外出详细地点
    */
    private String wcxxdd;

    /**
    * 出发时间
    */
    private String wcrq;

    /**
    * 请假事由
    */
    private String qjsy;

    /**
    * 预计返校时间
    */
    private String yjfxsj;

    /**
    * 审核辅导员
    */
    private String spfdy;

    /**
    * 辅导员审批时间
    */
    private String fdyspsj;

    /**
    * 审批辅导员工号
    */
    private String spfdygh;

    /**
    * 辅导员审批结果
    */
    private String fdyspjg;

    /**
    * 辅导员意见
    */
    private String fdyyj;

    /**
    * 审批副书记工号
    */
    private String spfsjgh;

    /**
    * 审批副书记
    */
    private String spfsj;

    /**
    * 副书记审批时间
    */
    private String fsjspsj;

    /**
    * 副书记审批结果
    */
    private String fsjspjg;

    /**
    * 副书记意见
    */
    private String fsjyj;

    /**
    * 流程状态
    */
    private String lczt;

    /**
    * 流程状态代码
     * 1：待辅导员审批
    */
    private String lcztdm;

    /**
    * 离校扫码时间
    */
    private String lxsmsj;

    /**
    * 离校扫码地点
    */
    private String lxsmdd;

    /**
    * 入校扫码时间
    */
    private String rxsmsj;

    /**
    * 入校扫码地点
    */
    private String rxsmdd;

    /**
    * 是否按时返校
    */
    private String sfasfx;

    /**
    * 更新时间
    */
    private String gxsj;

    /**
    * 更新操作
    */
    private String gxcz;

    /**
    * 备注
    */
    private String bz;

    /**
    * 备注情况
    */
    private String qk;

    /**
    * 请假类型
    */
    private String qjlx;

    private static final long serialVersionUID = 1L;
}