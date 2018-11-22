package com.tgc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tgc.interceptor.MyInterceptor;

/**
 *
 * 
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    
    @Autowired
    MyInterceptor myInterceptor;

    /**
     * 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	// addPathPatterns 添加拦截规则。  addPathPatterns("/**")对所有请求都拦截，但是排除了/toLogin和/login请求的拦截。
    	// excludePathPatterns 排除拦截的规则
    	// 默认不会拦截静态资源
    	registry.addInterceptor(myInterceptor).addPathPatterns("/**").excludePathPatterns("/**/to_login","/**/do_login");
    	super.addInterceptors(registry);
    }
}
