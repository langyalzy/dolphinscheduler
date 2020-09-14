package com.eccang.mysql.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.eccang.decrypt.DesUtil;
import com.eccang.entity.CompanyAmazonConfig;


/**
 * 
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：JDBCutil.java
 * @Description: jdbc 连接工具类<br>
 * 1、获取连接<br>
 * 2、关闭连接<br>
 * 3、查询数据<br>
 * 4、修改数据<br>
 * @Author: liaoziyang
 * @Date: 2020年9月9日下午2:53:39
 * @Version:1.0
 */
public class JDBCutil {
	
	static Logger LOG =Logger.getLogger(JDBCutil.class); 
	
    private static  String driver="com.mysql.jdbc.Driver";
    private static  String url="jdbc:mysql://xxxxxxxx:3306/xxx"; 
    private static  String username="xxxx";
    private static  String password="xxxx";
    
    static {//静态代码块注册驱动
        try {
            Class.forName(driver);//加载驱动
        } catch (ClassNotFoundException e) {
        	LOG.error("加载驱动异常", e);
        }
    }
    
    /**
     * 
     * @MethodName: getConnection
     * @Description: 获取连接对象
     * @author liaoziyang
     * @return Connection
     * @date 2020-09-12 01:51:26
     */
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
        	LOG.error("连接数据库异常", e);
            return null;
        }
    }

    /**
     * 
     * @MethodName: executeUpdate
     * @Description: 执行SQL脚本（增、删、改、查）
     * @author liaoziyang
     * @param sql
     * @param params void
     * @date 2020-09-12 01:52:22
     */
    public static void executeUpdate(String sql,Object...params){//
        Connection conn=JDBCutil.getConnection();
        PreparedStatement ps=null;
            try {
                ps=conn.prepareStatement(sql);
                if(null!=params)for(int i=0;i<params.length;i++){
                    ps.setObject(i+1, params[i]);
                }
                ps.executeUpdate();
            } catch (SQLException e) {
            	LOG.error("查询数据异常", e);
            }finally{
                close(ps);
                close(conn);
            }
    }
    
    /**
     * 
     * @MethodName: executeCount
     * @Description: 获取查询结果集
     * @author liaoziyang
     * @param sql
     * @return Integer
     * @date 2020-09-12 01:53:49
     */
    public static Integer executeCount(String sql){//返回表中有多少条记录
        Integer count=0;
        Connection conn=JDBCutil.getConnection();
        PreparedStatement ps=null;
        try {
            ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            rs.next();
            count=rs.getInt(1);
            
        } catch (SQLException e) {
        	LOG.error("插叙数据异常", e);
        }
        return count;
    }
    
    /**
     * 
     * @MethodName: executeQuery
     * @Description: 自定义查询结果集
     * @author liaoziyang
     * @param sql
     * @return Map<String,CompanyAmazonConfig>
     * @date 2020-09-12 01:54:19
     */
    public static Map<String,CompanyAmazonConfig> executeQuery(String sql){//查询数据库，需要返回
        
    	Map<String,CompanyAmazonConfig> companyAmaonConfigs = new HashMap<String,CompanyAmazonConfig>();
    	
        Connection conn=JDBCutil.getConnection();
        PreparedStatement ps=null;
        
        try {
            ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();//获得结果集
            
            CompanyAmazonConfig amazonConfig = null;
            while(rs.next()){
            	
            	amazonConfig = new CompanyAmazonConfig();
            	
            	Integer id = 0;
            	String app_code="",company_code="",company_alias="",account_type="",db_host="",db_host_out="",db_port="",db_name="",user_account="",user_passwd="",passwd_secret="",type="",passwd = "";
            	
            	id = rs.getInt("id");
            	app_code = rs.getString("app_code");
            	company_code = rs.getString("company_code");
            	company_alias = rs.getString("company_alias");
            	account_type = rs.getString("account_type");
            	db_host = rs.getString("db_host");
            	db_host_out = rs.getString("db_host_out");
            	db_port = rs.getString("db_port");
            	db_name = rs.getString("db_name");
            	user_account = rs.getString("user_account");
            	user_passwd = rs.getString("user_passwd");
            	passwd_secret = rs.getString("passwd_secret");
            	type = rs.getString("type");
            	passwd = DesUtil.decrypt(user_passwd, passwd_secret);
            	
            	amazonConfig.setId(id);
            	amazonConfig.setApp_code(app_code);
            	amazonConfig.setCompany_code(company_code);
            	amazonConfig.setCompany_alias(company_alias);
            	amazonConfig.setAccount_type(account_type);
            	amazonConfig.setDb_host(db_host);
            	amazonConfig.setDb_host_out(db_host_out);
            	amazonConfig.setDb_port(db_port);
            	amazonConfig.setDb_name(db_name);
            	amazonConfig.setUser_account(user_account);
            	amazonConfig.setUser_passwd(user_passwd);
            	amazonConfig.setPasswd_secret(passwd_secret);
            	amazonConfig.setType(type);
            	amazonConfig.setPasswd(passwd);
            	amazonConfig.getDbType().put(amazonConfig.getApp_code(), amazonConfig.getDb_name());
            	
            	if(companyAmaonConfigs.containsKey(amazonConfig.getCompany_code())) {
            		companyAmaonConfigs.get(amazonConfig.getCompany_code()).getDbType().put(amazonConfig.getApp_code(), amazonConfig.getDb_name());
            	}else {
            		companyAmaonConfigs.put(amazonConfig.getCompany_code(), amazonConfig);
            	}
            	
            	
            }
            if(companyAmaonConfigs.size()==0)return null;
            return companyAmaonConfigs;
        } catch (Exception e) {
        	LOG.error("插叙数据异常", e);
        }finally{
            close(ps);
            close(conn);
        }
        return null;
    }
    
    /**
     * 
     * @MethodName: close
     * @Description: 关闭数据库连接对象
     * @author liaoziyang
     * @param conn void
     * @date 2020-09-12 01:54:42
     */
    public static void close(Connection conn){
        if(null!=conn){
            try {
                conn.close();
            } catch (SQLException e) {
            	LOG.error("关闭数据库连接对象异常", e);
            }
        }
    }

    /**
     * 
     * @MethodName: close
     * @Description: 关闭statement操作对象
     * @author liaoziyang
     * @param stat void
     * @date 2020-09-12 01:56:44
     */
    public static void close(Statement stat){
        if(null!=stat){
            try {
                stat.close();
            } catch (SQLException e) {
            	LOG.error("关闭statement异常", e);
            }
        }
    }

    /**
     * 
     * @MethodName: close
     * @Description: 关闭resultset操作对象
     * @author liaoziyang
     * @param rs void
     * @date 2020-09-12 01:57:16
     */
    public static void close(ResultSet rs){
        if(null!=rs){
            try {
                rs.close();
            } catch (SQLException e) {
            	LOG.error("关闭resultset异常", e);
            }
        }
    }
    
    @Test
	public static void test() {
    	Map<String,CompanyAmazonConfig> list = executeQuery("select id,app_code,company_code,company_alias,account_type,db_host,db_host_out,db_port,db_name,user_account,user_passwd,passwd_secret,type from company_amazon_config");
    	LOG.info(list.toString());
    	
	}
}
