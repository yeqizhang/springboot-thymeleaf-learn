package com.tgc.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tgc.entity.User;
import com.tgc.service.LoginService;
import com.tgc.utils.SendMailUtils;

@Component
public class MyInterceptor implements HandlerInterceptor{
	
    private static Logger log = LoggerFactory.getLogger(MyInterceptor.class);

	
    @Autowired
    LoginService loginService;
    
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("************* MyInterceptor 校验登陆状态 ***************");
    	//获取sessionId
        String sessionId=request.getSession().getId();	//浏览器cookie中的sessionId被删除会再生成一个新的。(获取客户端传过来的sessionid,如果没有传spring会通过UUID的方式分配一个)
        Date d = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println("当前时间：" + sdf.format(d));
    	System.out.println("sessionId:" + sessionId);
    	String url = "http://" + request.getServerName() //服务器地址  
        + ":"   
        + request.getServerPort()           //端口号  
        + request.getContextPath()      //项目名称  
        + request.getServletPath()      //请求页面或其他地址  
        + "?" + (request.getQueryString()); //参数 
        System.out.println("请求地址：" + url);
    	
        User user = loginService.getUserBySessionIdFromRedis(sessionId);	//根据redis中是否存在sessionid对应的user,判断是否登陆
		if(user == null){
			// 注销用户，使session失效。(主要考虑应用的session没过期而redis过期。)
			request.getSession().invalidate();
			//log.error("测试error日志发送到邮箱~");	//测试成功
			//log.error("测试error日志发送到邮箱~", new Exception());	//测试成功。同时把异常详细信息发送了过去。
			//log.info(MarkerFactory.getMarker("DONE"),"测试通过标记，发送日志到邮箱");	//测试成功。
			//SendMailUtils.send("测试通过java类发送error日志发送到邮箱~", (new Exception("报错信息：xxx")).getMessage());	//测试成功。
			response.sendRedirect(request.getContextPath()+"/login/to_login");
			System.out.println("*************未登陆状态***************");
			return false;
		}
		
		System.out.println("*************已登陆状态***************");
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		
	}
	
}
