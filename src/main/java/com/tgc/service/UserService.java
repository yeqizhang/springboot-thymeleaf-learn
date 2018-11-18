
package com.tgc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	@Qualifier("oneJdbcTemplate")
	private JdbcTemplate jdbcTemplate;	//此jdbcTemplate使用的数据库为test
	
	
	/*@Autowired
	private UserDao userDao;*/
	
	/**
	 * 测试事务是否回滚
	 * @param name
	 * @param age
	 */
	@Cacheable(value = "cache2", key = "#name")	
	public String findByName(String name) {
		System.out.println("第一次不使用缓存");
		if("tgc".equals(name)){
			return "26";
		}else{
			return "xx";
		}
	}
	
	public void createUser(String name, Integer age) {
		System.out.println("createUser");
		jdbcTemplate.update("insert into users values(null,?,?);", name, age);
		System.out.println("创建用户成功...");
	}
	
}
