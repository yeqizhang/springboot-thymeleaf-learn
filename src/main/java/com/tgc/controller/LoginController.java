package com.tgc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgc.controller.result.Result;
import com.tgc.entity.User;
import com.tgc.service.UserService;


@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;


    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletRequest request,HttpServletResponse response, @Valid User user) {//加入JSR303参数校验
    	//获取sessionId
        String sessionId=request.getSession().getId();
        boolean status = false;
        status = userService.login(response, user,sessionId);
        if(status){
        	System.out.println("登陆成功~~");
        }else{
        	System.out.println("登陆失败~~");
        }
        return Result.success(Boolean.toString(status));
    }

}
