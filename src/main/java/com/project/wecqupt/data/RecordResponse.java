package com.project.wecqupt.data;

import com.project.wecqupt.model.WeXsfxLxsp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/19 9:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordResponse {

    private int totalNumber;

    private List<WeXsfxLxsp> records;

}
