package com.eccang.process;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.eccang.constant.Constants;
import com.eccang.entity.CompanyAmazonConfig;
import com.eccang.file.util.FileTools;
import com.eccang.mysql.util.JDBCutil;
import com.eccang.request.Request;

/**
 * 
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：ProcessTools.java
 * @Description:遠程創建工作流主控類
 * 
 * 执行主逻辑：
 * 
 * [tidb数据同步到数仓ods层数据抓取自动任务生成步骤]

1、shell脚本自动生成
	
	A、制作两种(增量/全量)模板
		A-1、增量抓取模板
			-> 每个客户的所有脚本内容完全一致
			-> <用户代码根据linux脚本自动获取脚本目录名称（脚本目录名称就是客户代码）>
			
		A-2、全量抓取模板
			-> 每个客户的所有脚本内容完全一致
			-> <用户代码根据linux脚本自动获取脚本目录名称（脚本目录名称就是客户代码）>

	B、自动生成shell脚本
		B-1、根据客户代码创建目录
			-> 根据客户代码创建增量抓取数据脚本目录
			-> 根据客户代码创建全量抓取数据脚本目录
			
		B-2、复制模板脚本到客户代码对应的目录下面
			-> 将增量模板脚本复制到用户增量抓取目录下方
			-> 将全量模板脚本复制到用户全量抓取目录下方
		
	备注：
		
2、任务json自动生成

	A、制作json模板
	B、远程创建任务（远程请求）
	
 * @Author: liaoziyang
 * @Date: 2020年9月11日下午12:07:32
 * @Version:1.0
 */
public class ProcessTools {
	
	static Logger LOG =Logger.getLogger(ProcessTools.class); 
	
	/**
	 * 
	 * @MethodName: main
	 * @Description: 程序入口
	 * @author liaoziyang
	 * @param args void
	 * @date 2020-09-12 04:08:39
	 */
	public static void main(String[] args) {
		execCreateProcess();
	}
	
	/**
	 * 
	 * @MethodName: execCreateProcess
	 * @Description: 執行創建工作流
	 * @author liaoziyang void
	 * @date 2020-09-12 04:08:10
	 */
	public static void execCreateProcess() {
		
		
		Set<String> propKeys = FileTools.propKey(Constants.DB_PROP_PATH);
		File propFile = new File(Constants.DB_PROP_PATH);
		
		//查询出所有的客户信息，
		Map<String,CompanyAmazonConfig> list = JDBCutil.executeQuery(Constants.SEARCH_COMPANY_SQL);
		
		/**
		 * 根据客户代码创建增量、全量脚本、数据库连接写入配置信息
		 */		
		for(CompanyAmazonConfig amazonConfig : list.values()) {
		
			try {
				//1、创建增量脚本
				LOG.info("【***************************增量脚本创建开始********************************】");
				String newIncrementPath = "cp -r " + Constants.INCREMENT_MODEL_PATH + " "+ Constants.NEW_INCREMENT_MODEL_PATH.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER , amazonConfig.getCompany_code() );
				LOG.info(newIncrementPath);
				//执行创建增量脚本
				//ShellUtil.execCommand(newIncrementPath);
				LOG.info("【***************************增量脚本创建完成********************************】");
				
				
				//2、创建全量脚本
				LOG.info("【***************************全量脚本创建开始********************************】");
				String newFullPath = "cp -r " + Constants.FULL_MODEL_PATH + " "+ Constants.NEW_FULL_MODEL_PATH.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER , amazonConfig.getCompany_code() );
				LOG.info(newFullPath);
				//执行创建全量脚本
				//ShellUtil.execCommand(newFullPath);
				LOG.info("【***************************全量脚本创建完成********************************】");

				//3、写入配置信息（数据库连接配置信息）
				LOG.info("【***************************程序写入配置开始********************************】");
				if(!propKeys.contains(Constants.AD_TIDB_CONNECT.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code())) 
					&& !propKeys.contains(Constants.BIZ_TIDB_CONNECT.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code()))) {//判斷是否已經存在客戶相關配置
					
					LOG.info("客户 "+amazonConfig.getCompany_code()+" 的配置写入");
					FileTools.writeProp(propFile, dbParams(amazonConfig));
				 }
				LOG.info("【***************************程序写入配置完成********************************】");
				//4、创建工程（海豚调度平台远程请求）
				
				LOG.info("【***************************远程创建工作流开始********************************】");
//				post("http://prod-bigdata-4:9030/dolphinscheduler/projects/"+Constants.PROJECT_NAME+"/process/save")//生产
				String saveProcess = Request.post(Constants.DOLPHINSCHEDULER_URL)//测试
				.params(processParams(amazonConfig.getCompany_code()))
				.body();
				LOG.info(amazonConfig.getCompany_code()+"  ::  "+saveProcess);
				LOG.info(amazonConfig.toString());
				LOG.info("【***************************远程创建工作流完成********************************】");
			} catch (Exception e) {
				LOG.error("任务创建异常", e);
			}
			
		}
		
	}
	
	/**
	 * 
	 * @MethodName: params
	 * @Description: TODO
	 * @author liaoziyang
	 * @param companyCode
	 * @return Map<String,Object>
	 * @date 2020-09-10 11:33:36
	 */
	public static Map<String,Object> processParams(String companyCode){
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("connects", Constants.CONNECTIONS);
		params.put("locations", Constants.LOCATIONS.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, companyCode));
		params.put("name", Constants.NAME.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, companyCode));
		params.put("processDefinitionJson", Constants.PROCESS_DEFINITION_JSON.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, companyCode));
//		params.put("projectName", Constants.PROJECT_NAME);//生产工程名
		params.put("projectName", "show_test");//测试工程名
		params.put("description", Constants.DESCRIPTION);
		
		return params;
	}
	
	
	/**
	 * 为每个客户生成对应的数据库连接信息配置对象
	 * @MethodName: dbParams
	 * @Description: TODO
	 * @author liaoziyang
	 * @param amazonConfig
	 * @return Map<String,Object>
	 * @date 2020-09-11 11:54:20
	 */
	public static List<String> dbParams(CompanyAmazonConfig amazonConfig){
		
		List<String> params = new ArrayList<String>();
		
		
		params.add(Constants.AD_TIDB_CONNECT.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code()) + "=" + "\"jdbc:mysql://" + amazonConfig.getDb_host() + ":" + amazonConfig.getDb_port() + "/" + amazonConfig.getDbType().get(Constants.AMAZON_AD) + "?tinyInt1isBit=false&zeroDateTimeBehavior=convertToNull\"");
		params.add(Constants.AD_TIDB_USERNAME.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code()) + "=" + amazonConfig.getUser_account());
		params.add(Constants.AD_TIDB_PASSWORD.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code()) + "=\"" + amazonConfig.getPasswd() + "\"");
		
		params.add(Constants.BIZ_TIDB_CONNECT.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code()) + "=" + "\"jdbc:mysql://"+amazonConfig.getDb_host() + ":" + amazonConfig.getDb_port() + "/" + amazonConfig.getDbType().get(Constants.AMAZON_BIZ) + "?tinyInt1isBit=false&zeroDateTimeBehavior=convertToNull\"");
		params.add(Constants.BIZ_TIDB_USERNAME.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code()) + "=" + amazonConfig.getUser_account());
		params.add(Constants.BIZ_TIDB_PASSWORD.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, amazonConfig.getCompany_code()) + "=" + amazonConfig.getPasswd() + "\"");
		
		
		return params;
	}

}
