package com.tgc.redis;

/**
 * Created by jiangyunxiong on 2018/5/21.
 */
public class UserKey extends BasePrefix {

    //public static final int TOKEN_EXPIRE = 3600*1;//默认一小时
	public static final int TOKEN_EXPIRE = 600;

    /**
     * 防止被外面实例化
     */
    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 需要缓存的key
     */
    //public static UserKey token = new UserKey(TOKEN_EXPIRE,"token");
    public static UserKey getByUsername = new UserKey(0, "user#");
    public static UserKey userSessionId = new UserKey(TOKEN_EXPIRE,"sessionId#");	//缓存一个小时

}
