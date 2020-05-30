package com.seizedays.semall.webutils.configs;

import com.seizedays.semall.webutils.interceptor.AuthIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    AuthIntercepter authIntercepter;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authIntercepter)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/css/**", "/**/js/**", "/**/img/**", "/**/image/**", "/**/json/**", "/**/font/**")
                .excludePathPatterns("/error","/index/jsindex.js");

    }
}
