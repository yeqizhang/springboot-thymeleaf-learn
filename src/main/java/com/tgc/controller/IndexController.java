
package com.tgc.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 此类的分布式事务，适用于单机多数据源的情况。
 * @author Administrator
 *
 */
@Controller
public class IndexController {
	private static Logger log = Logger.getLogger(IndexController.class);
	
	
	@Value("${my.name}")	//取配置文件中的配置
	private String myname;
	
	/**
	 * 测试跳转到页面
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "index";
		//http://localhost:8888/tgc/index
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
	
}
