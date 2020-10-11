package com.project.wecqupt.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/24 19:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalsList implements Serializable {
    private Integer id;

    private String studentNumber;

    private String name;

    private static final long serialVersionUID = 1L;
}