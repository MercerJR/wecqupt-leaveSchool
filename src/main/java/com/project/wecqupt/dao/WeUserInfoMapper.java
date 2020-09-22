package com.project.wecqupt.dao;

import com.project.wecqupt.model.WeUserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/16 20:18
 */
public interface WeUserInfoMapper {
    boolean deleteByPrimaryKey(@Param("openid") String openid, @Param("xh") String xh);

    boolean insert(WeUserInfo record);

    boolean insertSelective(WeUserInfo record);

    WeUserInfo selectByPrimaryKey(@Param("openid") String openid, @Param("xh") String xh);

    boolean updateByPrimaryKeySelective(WeUserInfo record);

    boolean updateByPrimaryKey(WeUserInfo record);

    String selectXhById(String openid);

}