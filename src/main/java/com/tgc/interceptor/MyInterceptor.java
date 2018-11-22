package com.tgc.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.tgc.config.constants.Constants;
import com.tgc.entity.User;
import com.tgc.service.UserService;
import com.tgc.utils.CookieUtil;

@Component
public class MyInterceptor implements HandlerInterceptor{
	
    @Autowired
    UserService userService;
    
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("************* MyInterceptor 校验登陆状态 ***************");
    	//获取sessionId
        String sessionId=request.getSession().getId();
        //System.out.println("session AttributeNames:" + request.getSession().getAttributeNames());
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
    	
		//检验浏览器是否有cookies.或者cookies对应的session是否过期
//		String paramToken = request.getParameter(Constants.COOKIE_NAME_TOKEN);	//url传递
//        String cookieToken = CookieUtil.getCookieValue(request, Constants.COOKIE_NAME_TOKEN);	//cookie传递
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//        	response.sendRedirect("/login/to_login");
//        	System.out.println("*************没有传递token 未登陆状态***********");
//        	System.out.println("");
//        	return false;
//        }
        User user = userService.getBySessionId(response, sessionId);
		if(user == null){
			response.sendRedirect(request.getContextPath()+"/login/to_login");
			System.out.println("*************未登陆状态***************");
			return false;
		}
		
		System.out.println("*************已登陆状态***************");
		System.out.println("");
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
