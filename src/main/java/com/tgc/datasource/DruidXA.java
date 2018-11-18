package com.tgc.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.xa.DruidXADataSource;

/**
 * 使用 DruidXADataSource 集成 Atomikos, 配置了三个数据源。
 * 
 * MysqlXA和 DruidXA 只能同时开启其中一种，想用哪种把另外那种的@Configuration注释
 * @author Administrator
 *
 */
@Configuration
public class DruidXA {
	

    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.druid.filters}")
    private String filters;

    @Value("{spring.datasource.druid.connectionProperties}")
    private String connectionProperties;
    
    @Value("{spring.datasource.druid.useGlobalDataSourceStat}")
    private String useGlobalDataSourceStat;
    
	
    @Primary
    @Bean(name = "dataSourceOne")
    public DataSource dataSourceOne(DatabaseOneProperties databaseOneProperties) throws Exception {
    	
    	DruidDataSource dataSource = new DruidDataSource();	
        dataSource.setUrl(databaseOneProperties.getUrl());
        dataSource.setUsername(databaseOneProperties.getUsername());
        dataSource.setPassword(databaseOneProperties.getPassword());
        dataSource.setDriverClassName(databaseOneProperties.getDriverClassName());
        
        setDruidXADataSourceProperties(dataSource);
        
        return dataSource;
    	
    }
    
    /**
     * 设置druid 除url username password 以外的属性
     * @param xaDataSource
     */
    private void setDruidXADataSourceProperties(DruidDataSource xaDataSource){
    	xaDataSource.setInitialSize(initialSize);
        xaDataSource.setMinIdle(minIdle);
        xaDataSource.setMaxActive(maxActive);
        xaDataSource.setMaxWait(maxWait);
        xaDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        xaDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        xaDataSource.setValidationQuery(validationQuery);
        xaDataSource.setTestWhileIdle(testWhileIdle);
        xaDataSource.setTestOnBorrow(testOnBorrow);
        xaDataSource.setTestOnReturn(testOnReturn);
        xaDataSource.setPoolPreparedStatements(poolPreparedStatements);
        xaDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            xaDataSource.setFilters(filters);
        } catch (SQLException e) {
            //logger.info("druid configuration initialization filter", e);
        }
        xaDataSource.setConnectionProperties(connectionProperties);
        xaDataSource.setUseGlobalDataSourceStat(Boolean.valueOf(useGlobalDataSourceStat));
    }
}
