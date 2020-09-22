package com.project.wecqupt.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/19 16:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanCodeLog implements Serializable {
    private Integer id;

    private String studentNumber;

    private String name;

    private String time;

    private String action;

    private String result;

    private static final long serialVersionUID = 1L;

    public ScanCodeLog(String studentNumber,String name,String time,String action,String result){
        this.studentNumber = studentNumber;
        this.name = name;
        this.time = time;
        this.action = action;
        this.result = result;
    }

}