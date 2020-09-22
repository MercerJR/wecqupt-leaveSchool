package com.project.wecqupt.config.filter;

import com.alibaba.fastjson.JSONObject;
import com.project.wecqupt.data.HttpInfo;
import com.project.wecqupt.model.WeUserInfo;
import com.project.wecqupt.service.WeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/21 10:26
 */
@Slf4j
@Component
public class ValidatedFilter extends HandlerInterceptorAdapter {

    @Autowired
    private WeUserService weUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (HttpInfo.GET.equals(request.getMethod())){
            String openid = request.getParameter(HttpInfo.OPENID);
            String studentNumber = request.getParameter(HttpInfo.STUDENT_NUMBER);
            WeUserInfo weUserInfo = weUserService.validateOpenid(openid,studentNumber);
            request.setAttribute(HttpInfo.WE_USER_INFO,weUserInfo);
        }else {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            String openid = jsonObject.getString(HttpInfo.OPENID);
            String studentNumber = jsonObject.getString(HttpInfo.STUDENT_NUMBER);
            WeUserInfo weUserInfo = weUserService.validateOpenid(openid,studentNumber);
            request.setAttribute(HttpInfo.WE_USER_INFO,weUserInfo);
        }
        return true;
    }

}
