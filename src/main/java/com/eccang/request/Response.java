package com.eccang.request;
/**
 * 
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：Response.java
 * @Description:用一句话来描述这个类的作用
 * @Author: liaoziyang
 * @Date: 2020年9月9日下午9:52:37
 * @Version:1.0
 */
public class Response {

	private int statusCode;
	private int length;
	private long date;
	private String msg;
	private String contentType;
	
	public Response() {
	}
	
	public void statusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int statusCode() {
		return statusCode;
	}

	public int length() {
		return length;
	}

	public void length(int length) {
		this.length = length;
	}

	public String contentType() {
		return contentType;
	}

	public void contentType(String contentType) {
		this.contentType = contentType;
	}

	public long date() {
		return date;
	}

	public void date(long date) {
		this.date = date;
	}

	public String msg() {
		return msg;
	}

	public void msg(String msg) {
		this.msg = msg;
	}
	
	
}