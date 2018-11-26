
package com.tgc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//@ComponentScan(basePackages = { "com.tgc" })	//如果将此类放在com.tgc下则不需要此注解
@SpringBootApplication 
@EnableCaching // 开启缓存注解
@ImportResource(locations = { "classpath:druid-bean.xml" })	//Druid的spring监控 ， 访问http://localhost:8888/tgc/findByName?name=tgc可以看到效果
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 600) 
//设置session过期时间为600秒。   redis在UserKey中也设置的600秒。
//注意：session过期的时间由两个中最小的时间为准。但是如果redis过期而session还没过期，重新登录时依然是使用未过期的session(解决办法：redis中没查到时进行处理，)
//@EnableAsync	//定时记录druid
public class RunApp {
	
	public static void main(String[] args) {
		// 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
		SpringApplication.run(RunApp.class, args);
	}

}
