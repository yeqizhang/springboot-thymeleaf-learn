
package com.tgc.exception;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
////全局捕获异常类
//@ControllerAdvice
//public class GlobalExceptionHandler {
//	
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
//	
//	@ExceptionHandler(RuntimeException.class)
//	//如果返回json格式 @ResponseBody 返回页面 返回string类型 类型结果指定404页面
//	@ResponseBody
//	public Map<String, Object> resultError(Exception e) {
//		//后台打印具体错误
//		System.err.println("程序出现错误，错误内容：");
//		//System.err.println(e.getMessage());
//		e.printStackTrace();
//		logger.error(e.getMessage());
//		
//		//下面消息返回给前台页面
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("errorCode", "500");
//		result.put("errorMsg", e.getMessage());
//		return result;
//	}
//
//}
