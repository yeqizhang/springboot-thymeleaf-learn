package com.tgc.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 配置druid  web访问。   http://127.0.0.1:8888/tgc/druid     http:/<host>:<port>/<context>/druid
 * 
 * session监控，不监控某些请求。。
 * spring监控 ，监控的类在 druid-bean.xml中配置。 所有类都可以监控到
 * 
 * @author Administrator
 *
 */
@Configuration
public class DruidConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

    /*@Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid() {
        return new DruidDataSource();
    }*/
    
    
    @Bean
    public ServletRegistrationBean druidServlet() {
        logger.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // IP白名单
        //servletRegistrationBean.addInitParameter("allow", "*");
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
    	 FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    	 WebStatFilter webStatFilter = new WebStatFilter();
    	 webStatFilter.setProfileEnable(true);	//设置profileEnable能够监控单个url调用的sql列表。
    	 webStatFilter.setSessionStatEnable(true);	//session统计功能
    	 
    	 filterRegistrationBean.setFilter(webStatFilter);
    	 filterRegistrationBean.addUrlPatterns("/*");
    	 filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    	 return filterRegistrationBean;
    }


}
