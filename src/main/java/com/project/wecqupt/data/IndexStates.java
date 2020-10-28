package com.project.wecqupt.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/18 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexStates {

    private String processState;

    private int shortApplyState = 1;

    private int longApplyState = 1;

    private int rentalState = 0;

}
