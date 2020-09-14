package com.eccang.shell.util;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * 
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：ShellUtil.java
 * @Description:linux 执行shell工具类
 * @Author: liaoziyang
 * @Date: 2020年9月9日下午3:36:15
 * @Version:1.0
 */
public class ShellUtil {

	static Logger LOG =Logger.getLogger(ShellUtil.class); 
	
	/**
	 * 
	 * @MethodName: execCommand
	 * @Description: 根據不同的操作系統，調用不同的命令
	 * @author liaoziyang
	 * @param bashCommand 執行腳本
	 * @throws Exception void
	 * @date 2020-09-09 04:40:16
	 */
	public static void execCommand(String bashCommand) throws Exception{
		
		//校驗是否包含刪除腳本命令
		if(bashCommand.contains("rm ") || bashCommand.contains(" -rf ")){
			LOG.info("禁止執行刪除腳本！！！");
			return;
		}
		
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		ProcessBuilder builder = new ProcessBuilder();
		
		//判斷操作系統類型
		if (isWindows) {
		  builder.command("cmd.exe", "/c", "dir");
		} else {
		  builder.command("sh", "-c", bashCommand);
		}
		builder.directory(new File(System.getProperty("user.home")));
		Process process = builder.start();

		int exitCode = process.waitFor();
		assert exitCode == 0;
		
		process.getErrorStream().close();
		process.getInputStream().close();
		process.getOutputStream().close();
		
	}
	
}