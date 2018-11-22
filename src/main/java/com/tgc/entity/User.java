package com.tgc.entity;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 必须要实现序列化接口才能使用redis缓存
	 */
	private static final long serialVersionUID = 1542370946090407431L;
	
	private Integer id;
	private String name;
	private Integer age;
	
	private String username;
	private String password;

	// ..get/set方法
	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public Integer getAge() {

		return age;
	}

	public void setAge(Integer age) {

		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
