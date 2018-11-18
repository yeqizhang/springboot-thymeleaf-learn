
package com.tgc.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgc.entity.User;
import com.tgc.mapper.UserMapper;
import com.tgc.service.UserService;

/**
 * 此类的分布式事务，适用于单机多数据源的情况。
 * @author Administrator
 *
 */
@Controller
public class IndexController {
	private static Logger log = Logger.getLogger(IndexController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Value("${my.name}")	//取配置文件中的配置
	private String myname;

	/**
	 * 测试跳转到jsp页面
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "index";
		//http://localhost:8888/tgc/index
	}
	
	//==========使用jdbctemplate===============
	/**
	 * 使用jdbcTemplate插入数据库  test
	 * @param name
	 * @param age
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/createUser")
	public String createUser(String name, Integer age) {
		userService.createUser(name, age);
		//http://localhost:8888/tgc/createUser?name=tgc33&age=26
		return "success";
	}
	
	//==============================================
	
	
	
	//============使用mybatis=========================
	@ResponseBody
	@RequestMapping("/findByName")
	public User findByName(String name) {
		log.info("####findByName()####name:" + name);
		return userMapper.findByName(name);
		//http://localhost:8888/tgc/findByName?name=tgc
	}
	
	
	/**
	 * 测试redis缓存
	 * 
	 * 调用后第一次会进入userService.findByName打印里面的语句
	 * 第二次直接查缓存，不进入userService.findByName方法体类。
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/testRedisCache")
	public String testRedisCache(String name) {
		log.info("####findById()####id:" + name);
		return userService.findByName(name);
	}
	
	/**
	 * 测试采用@Value注解获取配置文件的配置
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getValue")
	public String getValue() {
		return myname;
	}

	
	/**
	 * 给前台调用清除缓存的方法 (对ehcache、redis等都可以用)
	 * 
	 * allEntries = true: 清空cache2里的所有缓存
     * 
     *  allEntries = true: 清空缓存book1里的所有值
    	allEntries = false: 默认值，此时只删除key对应的值
	 * 
	 * @param key
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/clearCacheAll")
	@CacheEvict(cacheNames={"cache1","cache2"}, allEntries=true)
	public String clearCacheAll(String key) {
		//userService.clearCache2All();
		//使用@CacheEvict注解的方法必须是controller层直接调用，service里间接调用不生效
		return "success";
		//http://localhost:8888/tgc/clearCacheAll
	}
	    
}
