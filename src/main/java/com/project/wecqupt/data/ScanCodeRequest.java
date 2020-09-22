package com.project.wecqupt.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/21 10:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanCodeRequest {

    private String openid;

    private String studentNumber;

    private String type;

    private Integer logId;

    private String location;

    private String latitude;

    private String longitude;
}
