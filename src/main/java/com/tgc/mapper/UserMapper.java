package com.tgc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.tgc.entity.User;

@CacheConfig(cacheNames = "cache1")	//
public interface UserMapper {
	
	@Select("SELECT * FROM USERS WHERE NAME = #{name}")
	@Cacheable
	User findByName(@Param("name") String name);
	
	@Select("SELECT * FROM USERS WHERE USERNAME = #{username}")
	//@Cacheable
	User findByUsername(@Param("username") String username);

	@Insert("INSERT INTO USERS(NAME, AGE) VALUES(#{name}, #{age})")
	int insert(@Param("name") String name, @Param("age") Integer age);
	
	@Select("SELECT * FROM USERS")
	List<User> findAllUsers();
}
