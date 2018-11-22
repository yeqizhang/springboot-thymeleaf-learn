
package com.tgc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//@ComponentScan(basePackages = { "com.tgc" })	//如果将此类放在com.tgc下则不需要此注解
@SpringBootApplication 
//@EnableConfigurationProperties(value = { DatabaseOneProperties.class })	////让配置对象读取属性初始化.   可以在类上加个@Configuration
@EnableCaching // 开启缓存注解
@ImportResource(locations = { "classpath:druid-bean.xml" })	//Druid的spring监控 ， 访问http://localhost:8888/tgc/findByName?name=tgc可以看到效果
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600) //设置session过期时间为3600秒。   redis在UserKey中也设置的1小时
public class RunApp {
	
	public static void main(String[] args) {
		// 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
		SpringApplication.run(RunApp.class, args);
	}

}
