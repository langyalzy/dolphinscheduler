package com.eccang.file.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：PropertiesTools.java
 * @Description:配置文件工具类
 * @Author: liaoziyang
 * @Date: 2020年9月10日上午10:35:49
 * @Version:1.0
 */
public class PropertiesTools {
	
	
	
	static Logger LOG =Logger.getLogger(PropertiesTools.class); 
	/**
	 * 
	 * @MethodName: loadProperties
	 * @Description: TODO
	 * @author liaoziyang
	 * @param filePath
	 * @return
	 * @throws Exception Properties
	 * @date 2020-09-10 10:38:24
	 */
	public static Properties loadProp(String filePath) throws Exception{
		
		Properties pro = new Properties();
		FileInputStream in = new FileInputStream(filePath);
		BufferedReader bf = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		pro.load(bf);
		in.close();
		
		return pro;
	}
	
	@Test
	public static void test() {
		String filePath = "E:\\project\\bigdata\\azkaban-3.32.x\\azkaban-3.32.x\\azkaban-solo-server\\src\\test\\resources\\log4j.properties";
		try {
			Properties defaultProps = new Properties();
			
			Properties ps = loadProp(filePath);
			
			if(!ps.containsKey("test")) {
				System.out.println("ssssssssssssssssssdddddddddddddddddddddddd");
				defaultProps.setProperty("test", "妮可-广告库 数据源");
				
				outPutProp(filePath, defaultProps,  "妮可-广告库 数据源");
				
			}else {
				System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeetttttttttttttttttt");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e);
		}
	}
	
	/**
	 * 
	 * @MethodName: outPutProp
	 * @Description: 将配置写入到指定配置文件中
	 * @author liaoziyang
	 * @param filePath 目标文件路劲
	 * @param prop 内容临时配置文件
	 * @param comment 配置备注
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException boolean
	 * @date 2020-09-10 11:28:06
	 */
	public static boolean outPutProp(String filePath , Properties prop  , String comment) throws FileNotFoundException, UnsupportedEncodingException {
		
		OutputStream oFile = new FileOutputStream(filePath,true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(oFile,"UTF-8"));
        
        try {
			prop.store(bw, comment);
			oFile.close();
		} catch (IOException e) {
			LOG.error("輸出配置文件錯誤",e);
			return false;
		}
        return true;
		
	}
	
	

}
