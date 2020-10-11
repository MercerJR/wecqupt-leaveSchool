package com.project.wecqupt.dao;

import com.project.wecqupt.model.RentalsList;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/24 19:40
 */
public interface RentalsListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RentalsList record);

    int insertSelective(RentalsList record);

    RentalsList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RentalsList record);

    int updateByPrimaryKey(RentalsList record);

    RentalsList selectByStudentNumber(String studentNumber);
}