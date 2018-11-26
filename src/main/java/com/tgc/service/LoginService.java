
package com.tgc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.tgc.controller.result.CodeMsg;
import com.tgc.entity.User;
import com.tgc.exception.GlobalException;
import com.tgc.mapper.UserMapper;
import com.tgc.redis.RedisService;
import com.tgc.redis.UserKey;

/**
 * 前缀+用户名作为key存入redis（作为用户表缓存）， 前缀+sessionId作为session的缓存。
 * 登录时使用getUserFromRedis查出对象进行检查，    拦截器检查sessionId使用getBySessionId 
 * @author Administrator
 *
 */
@Service
public class LoginService {
	
    @Autowired
    UserMapper userMapper;
	
    @Autowired
    RedisService redisService;
	
	
    public boolean login(HttpServletResponse response, User loginUser,String sessionId) {
        if (loginUser == null) {
        	throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        //判断用户是否存在
        User user = getUserFromDbAndRedis(username);
        if (user == null) {
        	throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }
        //验证密码
        //String dbPass = user.getPassword();
        //String saltDB = user.getSalt();
        //String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!user.getPassword().equals(password)) {
        	throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成唯一id作为token
        //String token = UUIDUtil.uuid();
        addSessionIdToRedis(sessionId, user);
        return true;
    }
    
    /**
     * 登出删除session和redis缓存
     * @param request
     */
    public void logout(HttpServletRequest request) {
    	String sessionId=request.getSession().getId();
    	redisService.delete(UserKey.userSessionId, sessionId);	//只删除key为sessionId的。  username作为key的不删除。
    	request.getSession().invalidate();
    }
    
    /**
     * 根据sessionId获取用户信息
     */
    public User getUserBySessionIdFromRedis(String sessionId) {
        if (StringUtils.isEmpty(sessionId)||sessionId==null) {
            return null;
        }
        User user = redisService.get(UserKey.userSessionId, sessionId, User.class);
        //延长redis有效期，有效期等于最后一次操作+有效期
        if (user != null) {
        	addSessionIdToRedis(sessionId, user);	//web容器中的session自动延期？ （spring-session自动延期？）
        }
        return user;
    }
    
	/**
	 * 从redis中取用户。先去缓存取,没有再查数据库并存到缓存里。
	 * @param username
	 * @return
	 */
	public User getUserFromDbAndRedis(String username) {
        //对象缓存
        User user = redisService.get(UserKey.getByUsername, "" + username, User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userMapper.findByUsername(username);
        //再存入缓存
        if (user != null) {
        	addUserToRedis(user);
        }
        return user;
    }
	
    /**
     * 典型缓存同步场景：更新密码
     */
//    public boolean updatePassword(String token, long id, String formPass) {
//        //取user
//        User user = getById(id);
//        if(user == null) {
//            throw new Exception("没查到用户");
//        }
//        //更新数据库
//        User toBeUpdate = new User();
//        toBeUpdate.setId(id);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//        userMapper.update(toBeUpdate);
//        //更新缓存：先删除再插入
//        redisService.delete(UserKey.getById, ""+id);
//        user.setPassword(toBeUpdate.getPassword());
//        redisService.set(UserKey.token, token, user);
//        return true;
//    }
	
    /**
     * 将sessionId做为key，用户信息做为value 存入redis。
     */
    public void addSessionIdToRedis(String sessionId, User user) {
        redisService.set(UserKey.userSessionId, sessionId, user);
    }
    
    /**
     * 将前缀+username做为key，用户信息做为value 存入redis，作用户信息表使用
     */
    public void addUserToRedis(User user) {
    	 redisService.set(UserKey.getByUsername, "" + user.getUsername(), user);
    }
    
    /**
     * 将token做为key，用户信息做为value 存入redis模拟session
     * 同时将token存入cookie，保存登录状态
     */
//    public void addRedisAndCookie(HttpServletResponse response, String token, User user) {
//        redisService.set(UserKey.token, token, user);
//        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
//        cookie.setMaxAge(UserKey.token.expireSeconds());
//        cookie.setPath("/");//设置为网站根目录
//        response.addCookie(cookie);
//    }
}
