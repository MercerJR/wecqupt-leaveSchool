package com.project.wecqupt.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/16 20:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeValidate {
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
}
