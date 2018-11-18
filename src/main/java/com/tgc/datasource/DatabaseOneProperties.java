package com.tgc.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 将Properties的配置转换成java对象
 * @author tgc
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseOneProperties extends DatabaseProperties {
	
    private String driverClassName;	
    
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

}
