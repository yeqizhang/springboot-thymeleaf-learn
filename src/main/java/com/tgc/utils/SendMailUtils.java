package com.tgc.utils;

/**
 * 
 * 
 */
public class SendMailUtils {
	
	public static void send(String subject, String txt) {
		
		//new Thread(runable).start
		new Thread(new EmailThread(subject, txt)).start();
		
	}

    public static void main(String[] args) {
        SendMailUtils.send("主题","内容");
    }
}