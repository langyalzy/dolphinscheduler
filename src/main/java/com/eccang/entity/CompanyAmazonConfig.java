package com.eccang.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：CompanyAmazonConfig.java
 * @Description:用一句话来描述这个类的作用
 * @Author: liaoziyang
 * @Date: 2020年9月9日下午6:16:16
 * @Version:1.0
 */
public class CompanyAmazonConfig {
	
	private Integer id;
	
	private String app_code;
	private String company_code;
	private String company_alias;
	private String account_type;
	private String db_host;
	private String db_host_out;
	private String db_port;
	private String db_name;
	private String user_account;
	private String user_passwd;
	private String passwd_secret;
	private String type;
	private String passwd;
	
	private Map<String,String> dbType = new HashMap<String,String>();
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getApp_code() {
		return app_code;
	}
	
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	
	public String getCompany_code() {
		return company_code;
	}
	
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	
	public String getCompany_alias() {
		return company_alias;
	}
	
	public void setCompany_alias(String company_alias) {
		this.company_alias = company_alias;
	}
	
	public String getAccount_type() {
		return account_type;
	}
	
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	
	public String getDb_host() {
		return db_host;
	}
	
	public void setDb_host(String db_host) {
		this.db_host = db_host;
	}
	
	public String getDb_host_out() {
		return db_host_out;
	}
	
	public void setDb_host_out(String db_host_out) {
		this.db_host_out = db_host_out;
	}
	
	public String getDb_port() {
		return db_port;
	}
	
	public void setDb_port(String db_port) {
		this.db_port = db_port;
	}
	
	public String getDb_name() {
		return db_name;
	}
	
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	
	public String getUser_account() {
		return user_account;
	}
	
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	
	public String getUser_passwd() {
		return user_passwd;
	}
	
	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}
	
	public String getPasswd_secret() {
		return passwd_secret;
	}
	
	public void setPasswd_secret(String passwd_secret) {
		this.passwd_secret = passwd_secret;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Map<String, String> getDbType() {
		return dbType;
	}

	public void setDbType(Map<String, String> dbType) {
		this.dbType = dbType;
	}

	@Override
	public String toString() {
		return "CompanyAmazonConfig [id=" + id + ", app_code=" + app_code + ", company_code=" + company_code
				+ ", company_alias=" + company_alias + ", account_type=" + account_type + ", db_host=" + db_host
				+ ", db_host_out=" + db_host_out + ", db_port=" + db_port + ", db_name=" + db_name + ", user_account="
				+ user_account + ", user_passwd=" + user_passwd + ", passwd_secret=" + passwd_secret + ", type=" + type
				+ ", passwd=" + passwd + ", dbType=" + dbType + "]";
	}
}
