package com.tgc.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 为指定的mapper包注入不同的sqlSessionTemplate sqlSessionFactory来绑定数据源
 * 
 * 总结：首先创建DataSource，然后创建SqlSessionFactory，
 * 
 * @author Administrator
 *
 * SqlSessionFactoryBuilder：build方法创建SqlSessionFactory实例。
 * SqlSessionFactory：创建SqlSession实例的工厂。
 * SqlSession：用于执行持久化操作的对象，类似于jdbc中的Connection。
 * SqlSessionTemplate：MyBatis提供的持久层访问模板化的工具，线程安全，可通过构造参数或依赖注入SqlSessionFactory实例
 * 
 * 使用sqlSessionFactoryBeanName属性，注入不同的sqlSessionFactory的名称，这样的话，就为不同的数据库对应的 mapper 接口注入了对应的 sqlSessionFactory。
 * 库1的数据源模板，应用在从库所对应的Dao层上（扫描对应的mapper），实现数据源1的指定+增删改查
 */
@Configuration
//basePackages 最好分开配置 如果放在同一个文件夹可能会报错
@MapperScan(basePackages = "com.tgc.mapper",sqlSessionTemplateRef = "oneSqlSessionTemplate")
public class MyBatisDbsOneConfig {

	 /**
     * 
     * SqlSession：应用程序和数据库之间交互的一个单线程对象（非线程安全的），数据库的C、R、U、D及事务的处理接口，select | update | delete | insert | commit | rollback | close | flushStatements等
		SqlSessionFactory:工厂设计模式，创建SqlSession的工厂。SqlSessionFactory是MyBatis的关键对象
     * @param dataSource
     * @return
     */
	@Bean(name = "oneSqlSessionFactory")
	@Primary
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("dataSourceOne") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}
	
	//这里不再创建事务管理器
	@Bean(name = "oneTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("dataSourceOne") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

	@Bean(name = "oneSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate testSqlSessionTemplate(
			@Qualifier("oneSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
