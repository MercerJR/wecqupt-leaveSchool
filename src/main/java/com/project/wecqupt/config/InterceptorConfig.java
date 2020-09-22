package com.project.wecqupt.config;

import com.project.wecqupt.config.filter.ValidatedFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mercer JR
 * @Date: 2020/9/21 10:59
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private ValidatedFilter validatedFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
//        InterceptorRegistration registration = registry.addInterceptor(validatedFilter);
//        registration.addPathPatterns("/**");
    }

}
