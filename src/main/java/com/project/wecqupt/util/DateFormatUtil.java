package com.project.wecqupt.util;

import com.project.wecqupt.data.Message;
import com.project.wecqupt.exception.CustomException;
import com.project.wecqupt.exception.CustomExceptionType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/17 19:29
 */
public class DateFormatUtil {

    public static String getDetailedDateByMiles(Long miles){
        Long time = miles ;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    public static String getDateByMiles(Long miles){
        Long time = miles ;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    public static Long getMilesByDetailedDate(String date){
//        "2001-03-15 15-37-05";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            time = simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
        System.out.println(time);
        return time;
    }

    public static Long getMilesByDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long time = 0;
        try {
            time = simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
        System.out.println(time);
        return time;
    }
}
