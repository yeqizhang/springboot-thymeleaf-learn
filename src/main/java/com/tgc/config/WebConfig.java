package com.tgc.config;

/**
 * Created by jiangyunxiong on 2018/5/22.
 *
 * 
 */
//@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter{
//
//    
//    @Autowired
//    MyInterceptor myInterceptor;
//
//    /**
//     * 拦截器
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//    	// addPathPatterns 添加拦截规则。  addPathPatterns("/**")对所有请求都拦截，但是排除了/toLogin和/login请求的拦截。
//    	// excludePathPatterns 排除拦截的规则
//    	// 默认不会拦截静态资源
//    	registry.addInterceptor(myInterceptor).addPathPatterns("/**").excludePathPatterns("/**/to_login","/**/do_login");
//    	super.addInterceptors(registry);
//    }
//}
