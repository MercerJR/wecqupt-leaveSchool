package com.project.wecqupt.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 14:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateResponse {

    private String state;

    private String name;

    private String studentNumber;

    private String college;

    private int shortTimeState;

    private int longTimeState;

    private int rentalState;

}
