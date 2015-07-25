package com.newDemo.modules.user.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newDemo.common.BaseEntity;

public class AuthenticationToken extends BaseEntity{
	
	@JsonProperty
	String token;
	
	@DBRef
	User user;
	
	public AuthenticationToken(String token,User user){
		this.token = token;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
