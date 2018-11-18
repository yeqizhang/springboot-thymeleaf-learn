package com.tgc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateConfig {
	
	/**
     * 初始化名为oneJdbcTemplate的JdbcTemplate对象的数据源为dataSourceOne
     * @param dataSource
     * @return
     */
	 @Bean(name = "oneJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("dataSourceOne") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
