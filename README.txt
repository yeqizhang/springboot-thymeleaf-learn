一、构建分布式部署项目,集成连接池druid、spring-session分布式session, 日志logback
	1。Druid数据源配置、数据源监控、sql监控、spring监控
	2.spring-session配置
	3.logback依赖 、logback作为druid的日志统计filter、logback日志文件保存路径、logback输出样式、分布式的问题、发送邮件

二、使用thymeleaf视图
	依赖、配置、公共页面、表达式
	
三、拦截器、登陆状态
	拦截器开发、登录时sessionId的处理、设置session过期时间、设置session失效。


====================================================================================================

一、构建分布式部署项目,集成连接池druid、spring-session分布式session, 日志logback
	1。Druid数据源
		  注意！！——有时候不一定是配置的不行，可能为还没有查数据库，所以数据源和SQL监控无内容。
		<!-- 去掉默认配置 -->
		<exclusions>  
        	<exclusion>  
                <groupId>org.springframework.boot</groupId>  
                <artifactId>spring-boot-starter-logging</artifactId>  
            </exclusion>  
        </exclusions>
		<!-- 连接池  druid-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		
		数据源配置 DruidDS.java
		配置web监控： DruidConfiguration。java 
		sql监控：   spring.datasource.druid.filters=stat,wall,log4j2
		spring监控： @ImportResource(locations = { "classpath:druid-bean.xml" })
		
		Druid数据源 与log4j2 无法兼容导致SQL监控无内容的问题无法解决。现在使用logback(配置为slf4j)
		/*
		内置Filter别名和对应的Filter类名如下：
		别名				Filter类名
		default			com.alibaba.druid.filter.stat.StatFilter
		stat			com.alibaba.druid.filter.stat.StatFilter
		mergeStat		com.alibaba.druid.filter.stat.MergeStatFilter
		encoding		com.alibaba.druid.filter.encoding.EncodingConvertFilter
		log4j			com.alibaba.druid.filter.logging.Log4jFilter
		log4j2			com.alibaba.druid.filter.logging.Log4j2Filter
		slf4j			com.alibaba.druid.filter.logging.Slf4jLogFilter
		commonlogging	com.alibaba.druid.filter.logging.CommonsLogFilter
		*/
		待解决：  分布式多应用下，druid监控的问题。  druid只能监控嵌入的那个对应的应用。（无解，暂无法解决）
		
		druid的监控信息可通过定时器定时请求druid的接口获取，然后实例化到日志文件中。
		
	2.spring-session
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
		<dependency>
		    <groupId>org.springframework.session</groupId>
		    <artifactId>spring-session</artifactId>
		</dependency>
		
		application.properties加入redis配置。
		
		启动类使用注解 @EnableRedisHttpSession(maxInactiveIntervalInSeconds = 600)  //设置session过期时间为600秒。   redis在UserKey中也设置的600秒。
	
	3.logback
		springboot默认使用logback.
		要去掉则
		<!-- 去掉默认配置 -->
		<!-- <exclusions>  
        	<exclusion>  
                <groupId>org.springframework.boot</groupId>  
                <artifactId>spring-boot-starter-logging</artifactId>  
            </exclusion>  
        </exclusions> -->	
        
        logback作为Druid数据源的日志统计filter则配置为slf4j ：
        spring.datasource.druid.filters=stat,wall,slf4j
        
        logback日志文件保存路径：
		        文件相对保存路径设置可分以下三种： 
		1. value=“logs” – 表示保存到程序运行目录，在tomcat中为bin目录 
		2. value=“/logs” – 表示保存到系统目录 
		3. value=“../logs” – 表示保存到程序运行目录的父目录
        
                           日志输出不了的问题（能生成文件，但是是空的），一定要按照loan_error_%d{yyyy-MM-dd}.%i.log的格式，  注意 %i
        <!-- 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。 -->  
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">  
            <FileNamePattern>${log.base}/archive/${log.moduleName}_all_%d{yyyy-MM-dd}.%i.log.zip  
            </FileNamePattern>  
            <!-- 文件输出日志 (文件大小策略进行文件输出，超过指定大小对文件备份) -->  
            <timeBasedFileNamingAndTriggeringPolicy  
                class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">  
                <maxFileSize>${log.max.size}</maxFileSize>  
            </timeBasedFileNamingAndTriggeringPolicy>  
        </rollingPolicy>  
        
                          此应用多实例部署时，每个应用都写日志文件，但是会写到一个文件夹下，但因为每个实例都是一样的，所以，这里不在日志里加端口标志也没事（如果多个应用不同代码则写到不同文件即可）。
        	有一个东西叫分布式日志——一般本机的日志用来排错，我上面说的是日志的归档管理和日志数据的大规模分析用的。你如果只是要debug排错的话，就别搞这么复杂的东西了，老老实实去本机上找吧。
       
                         配置发邮件提示
       logback.xml中：
        <!--  邮件属性配置  -->
	    <!-- SMTP server的地址，必需指定。如网易的SMTP服务器地址是： smtp.163.com -->
	    <property name="smtpHost" value="smtp.qq.com"/><!--填入要发送邮件的smtp服务器地址-->
	    <!-- SMTP server的端口地址。默认值：25 -->
	    <property name="smtpPort" value="25"/>
	    <!-- 发送邮件账号，默认为null -->
	    <property name="username" value="347792253@qq.com"/><!--发件人账号-->
	    <!-- 发送邮件密码，默认为null。（qq邮箱中开启smtp服务后可获取到） -->
	    <property name="password" value=""/><!--发件人密码-->
	    <!-- 如果设置为true，appender将会使用SSL连接到日志服务器。默认值：false -->
	    <property name="SSL" value="false"/>
	    <!-- 指定发送到那个邮箱，可设置多个<to>属性，指定多个目的邮箱 -->
	    <property name="email_to" value="yqz19920528@163.com"/><!--收件人账号多个可以逗号隔开-->
	    <!-- 指定发件人名称。如果设置成“&lt;ADMIN&gt; ”，则邮件发件人将会是“<ADMIN> ”  最好设置和username一致，比如qq邮箱则不允许指定发件人名称为其它-->
	    <property name="email_from" value="347792253@qq.com" />
	    <!-- 指定emial的标题，它需要满足PatternLayout中的格式要求。如果设置成“Log: %logger - %msg ”，就案例来讲，则发送邮件时，标题为“【Error】: com.foo.Bar - Hello World ”。 默认值："%logger{20} - %m". -->
	    <property name="email_subject" value="【Error】: %logger" />
	  
	  	<appender name="EMAIL" class="ch.qos.logback.classic.net.SMTPAppender">
	        <smtpHost>${smtpHost}</smtpHost>
	        <smtpPort>${smtpPort}</smtpPort>
	        <username>${username}</username>
	        <password>${password}</password>
	        <asynchronousSending>true</asynchronousSending>
	        <SSL>${SSL}</SSL>
	        <to>${email_to}</to>
	        <from>${email_from}</from>
	        <subject>${email_subject}</subject>
	        <!-- 日志输出 html格式-->
	        <!--
	        <layout class="ch.qos.logback.classic.html.HTMLLayout">
	                    <Pattern>%date%level%thread%logger{0}%line%message</Pattern>
	                </layout>-->
	        <layout class="ch.qos.logback.classic.html.HTMLLayout"></layout>
	        
	　　　　	 <!-- 这里采用等级过滤器 指定等级相符才发送 -->
	        <filter class="ch.qos.logback.classic.filter.LevelFilter">
	            <level>ERROR</level>
	            <onMatch>ACCEPT</onMatch>
	            <onMismatch>DENY</onMismatch>
	        </filter>
	        <cyclicBufferTracker class="ch.qos.logback.core.spi.CyclicBufferTracker">
	            <!-- 每个电子邮件只发一个日志条目 -->
	            <bufferSize>2</bufferSize>
	        </cyclicBufferTracker>
	    </appender>
    
	             使用：
	    //log.error("测试error日志发送到邮箱~");	//测试成功
		//log.error("测试error日志发送到邮箱~", new Exception());	//测试成功。同时把异常详细信息发送了过去。
		//log.info(MarkerFactory.getMarker("DONE"),"测试通过标记，发送日志到邮箱");	//测试成功。
		
二、使用thymeleaf视图
	依赖：
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
	配置：
	spring.thymeleaf.prefix=classpath:/templates/
	spring.thymeleaf.suffix=.html
	spring.thymeleaf.cache=false
	spring.thymeleaf.content-type=text/html
	spring.thymeleaf.enabled=true
	spring.thymeleaf.encoding=UTF-8
	spring.thymeleaf.mode=HTML5
	
	引入公共页面css js,以及basePath —— common_header.html
	
	表达式：   th:src="@{}"	th:href="@{}"	th:each="user : ${users}"	th:text="${user}"
		  th:fragment	th:replace	th:include	th:insert 

三、配置拦截器检查登陆状态、sessionId存入redis
	拦截器implements HandlerInterceptor
	登陆拦截也可以使用servlet过滤器，但使用springmvc的拦截器更好。
	拦截器配置继承重写 WebMvcConfigurerAdapter的addInterceptors方法。
	
	整个流程为 :用户访问某个页面（排除掉登陆页面链接和请求登陆的链接）
	拦截器验证(getUserBySessionIdFromRedis，通过前端传递过来的sessionId查询缓存中是否有登陆信息缓存。)
				   -->不成功 -->刷新sessionId,跳转到登陆页面 
				   					-->do_login登陆，查询redis中是否缓存了用户信息，没有则再查mysql
				   							-->存在用户则进行密码验证（不存在则返回错误提示）
				   									-->密码正确（不正确则返回错误提示）。将sessionId存入缓存，供下一次验证查询
				   										-->跳转首页。
				   								
				   -->成功（访问getUserBySessionId时会再加一次缓存，以刷新redis的缓存失效时间。）	
	
	注意：  应用程序session的有效期以及redis缓存的有效期的问题。		如果如果redis过期而session还没过期，重新登录后依然会使用未过期的sessionId。
		解决办法是从redis缓存中没有查到用户登陆信息后（失效了），重定向到登陆界面前，将当前的session设置失效（request.getSession().invalidate()）。

	本应用一个登陆用户的session缓存（5条缓存，spring-session生产三条，自己生成两条UserKey开头的）：
	127.0.0.1:6379> keys *
	1) "UserKey:user#tgc"
	2) "UserKey:sessionId#a626483a-f34f-4c6b-a3fa-275dbeec89a2"
	3) "spring:session:sessions:expires:a626483a-f34f-4c6b-a3fa-275dbeec89a2"
	4) "spring:session:sessions:a626483a-f34f-4c6b-a3fa-275dbeec89a2"
	5) "spring:session:expirations:1542968100000"
	
下一步，集成rabbitMQ 、 dubbo等

