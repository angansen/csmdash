package com.csm.dashboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "CSM_USER")
//@JsonFilter("UserBeanFilter")
public class CSMUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Integer id;

	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "KEY")
	private String key;

	@Column(name = "ADMIN")
	private String admin;

	@Column(name = "FNAME")
	private String fName="";

	@Column(name = "LNAME")
	private String lName="";

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

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
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
