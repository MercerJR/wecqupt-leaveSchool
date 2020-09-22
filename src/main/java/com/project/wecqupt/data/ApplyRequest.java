package com.project.wecqupt.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyRequest {

    /**
     * openid
     */
    @NotEmpty(message = Message.OPENID_EMPTY)
    private String openid;

    /**
     * 学号
     */
    @NotEmpty(message = Message.STUDENT_NUMBER_EMPTY)
    private String studentNumber;

    @NotEmpty
    private String area;

    @Size(max = 15,message = Message.DETAILED_AREA_BEYOND)
    private String detailedArea;

    @Size(max = 30,message = Message.REASON_BEYOND)
    private String reason;

    private String startTime;

    private String endTime;

}
