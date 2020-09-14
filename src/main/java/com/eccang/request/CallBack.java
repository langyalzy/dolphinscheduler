package com.eccang.request;

/**
 * 
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：CallBack.java
 * @Description:用一句话来描述这个类的作用
 * @Author: liaoziyang
 * @Date: 2020年9月9日下午9:52:42
 * @Version:1.0
 */
public interface CallBack {

	public void call(Exception e, Response response);
	
}
