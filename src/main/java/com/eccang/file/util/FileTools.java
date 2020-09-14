package com.eccang.file.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.eccang.constant.Constants;

/**
 * 
 * @Title：FileTools.java
 * @Description:文件讀寫工具類
 * @Author: liaoziyang
 * @Date: 2020年9月11日下午5:08:28
 * @Version:1.0
 */
public class FileTools {
	
	static Logger LOG =Logger.getLogger(FileTools.class); 
	
	
	@Test
	public static void test() {
		StringBuffer xml = new StringBuffer();
		//String fileName = "‪E:\\docs\\bigdata\\pub-conf.sh";
		String fileName = "e:/docs/bigdata/pub-conf.sh";
		Path path = Paths.get(fileName);
		File file = new File(fileName);
		
//		try {
//			List<String> lines = Files.readAllLines(path);
//			lines.forEach(str -> xml.append(str));
//			lines.forEach(System.out::println);
//			} catch (IOException e) {
//			e.printStackTrace();
//			}
		writeProp(file, null);
		propKey(fileName).forEach(System.out::println);;
	}
	
	/**
	 * 讀取數據庫配置信息文件的key信息
	 * @MethodName: propKey
	 * @Description: TODO
	 * @author liaoziyang
	 * @param filePath
	 * @return Set<String>
	 * @date 2020-09-11 02:25:03
	 */
	public static Set<String> propKey(String filePath){
		
		Set<String> propKeys = new HashSet<String>();
		Path path = Paths.get(filePath);
		try {
			List<String> lines = Files.readAllLines(path, Charset.forName(Constants.CHARSET));
			lines.forEach(line->{
				String key = line.split("=")[0];
				propKeys.add(key);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("讀取數據庫配置信息錯誤", e);
			return null;
		}
		
		return propKeys;
		
	}
	
	/**
	 * 將配置文件信息寫入到指定配置文件中
	 * @MethodName: writeProp
	 * @Description: TODO
	 * @author liaoziyang
	 * @param filePath
	 * @param prop
	 * @return boolean
	 * @date 2020-09-11 02:45:40
	 */
	public static boolean writeProp(File filePath , List<String> prop) {

		//创建BufferedWriter
        try {
        	OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath, true), Constants.CHARSET);
    		
    		for(String line : prop) {
    			out.write("\n" + line);
    		}
    		
    		out.close();
        } catch (IOException e) {
        	LOG.error("輸出數據庫配置信息錯誤", e);
        	return Boolean.FALSE;
        }
		
		return Boolean.TRUE;
	}
	
	

}
