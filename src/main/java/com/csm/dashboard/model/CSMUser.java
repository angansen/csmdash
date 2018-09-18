package com.csm.dashboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name="CSM_USER")
@JsonFilter("UserBeanFilter")
public class CSMUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="USER_ID")
	private String userId;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="KEY")
	private String key;
	
	protected CSMUser() {
		
	}	
	/**
	 * @param userId
	 * @param password
	 * @param key
	 */
	public CSMUser(String userId, String password, String key) {
		this.userId = userId;
		this.password = password;
		this.key = key;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
