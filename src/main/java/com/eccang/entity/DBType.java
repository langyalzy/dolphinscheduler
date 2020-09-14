package com.eccang.entity;

public class DBType {
	
	private String app_code;
	private String db_name;
	
	
	public String getApp_code() {
		return app_code;
	}
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	public String getDb_name() {
		return db_name;
	}
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	
	@Override
	public String toString() {
		return "DBType [app_code=" + app_code + ", db_name=" + db_name + ", getApp_code()=" + getApp_code()
				+ ", getDb_name()=" + getDb_name() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
}
