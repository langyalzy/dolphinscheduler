package com.eccang.request;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.eccang.constant.Constants;

/**
 * 
 * @Title：Request.java
 * @Description:请求主类
 * @Author: liaoziyang
 * @Date: 2020年9月9日下午9:49:48
 * @Version:1.0
 */
public class Request {
	
	static Logger LOG =Logger.getLogger(Request.class); 
	
	// Default timeout is 60s
	private int timeout = 60 * 1000;
	
	// Request url
	private String url;
	
	// Request method
	private HttpMethod httpMethod;
	
	// Input data
	private InputStream inputStream;
	
	// Header info
	private Map<String, String> headers = new HashMap<>();
	
	// Form data
	private Map<String, Object> formdatas = new HashMap<>();
	
	// Response
	private Response response;
	
	/**
	 * Send get request
	 * @param url
	 * @return
	 */
	public static Request get(String url){
		return new Request(url, HttpMethod.GET);
	}
	
	/**
	 * Send post request
	 * @param url
	 * @return
	 */
	public static Request post(String url){
		return new Request(url, HttpMethod.POST);
	}
	
	/**
	 * Send put request
	 * @param url
	 * @return
	 */
	public static Request put(String url){
		return new Request(url, HttpMethod.PUT);
	}
	
	/**
	 * Send delete request
	 * @param url
	 * @return
	 */
	public static Request delete(String url){
		return new Request(url, HttpMethod.DELETE);
	}
	
	/**
	 * Send patch request
	 * @param url
	 * @return
	 */
	public static Request patch(String url){
		return new Request(url, HttpMethod.PATCH);
	}
	
	/**
	 * Send head request
	 * @param url
	 * @return
	 */
	public static Request head(String url){
		return new Request(url, HttpMethod.HEAD);
	}
	
	/**
	 * Add form data
	 * @param name
	 * @param value
	 * @return
	 */
	public Request param(String name, Object value){
		this.formdatas.put(name, value);
		return this;
	}
	
	/**
	 * Put form data
	 * @param params
	 * @return
	 */
	public Request params(Map<String, Object> params){
		this.formdatas.putAll(params);
		return this;
	}
	
	/**
	 * Setting header info
	 * @param name
	 * @param value
	 * @return
	 */
	public Request header(String name, String value){
		this.headers.put(name, value);
		return this;
	}
	
	/**
	 * Setting request timeout
	 * @param timeout
	 * @return
	 */
	public Request timeout(int timeout){
		this.timeout = timeout;
		return this;
	}
	
	public Request auth(){
		return this;
	}
	
	private Request(String url, HttpMethod httpMethod) {
		this.url = url;
		this.httpMethod = httpMethod;
	}
	
	/**
	 * Return request body as string
	 * @return
	 * @throws IOException
	 */
	public String body() throws IOException {
		send();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(this.inputStream));
		String line;
		StringBuffer body = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			body.append(line);
			body.append('\r');
		}
		rd.close();
		return body.toString();
	}
	
	/**
	 * @return	Return InputStream
	 */
	public InputStream stream(){
		return this.inputStream;
	}
	
	/**
	 * Save request url to file
	 * @param file
	 * @throws IOException
	 */
	public void saveAsDisk(File file) throws IOException {
		send();
		
		ReadableByteChannel rbc = Channels.newChannel(this.inputStream);
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.flush();
		fos.close();
	}
	
	/**
	 * @return	Return response
	 */
	public Response response(){
		return response;
	}
	
	private void send() {
		try {
			
			this.response = new Response();
			
			URL _url = new URL(this.url);
			final HttpURLConnection urlConn = url.startsWith("https") ?  (HttpsURLConnection) _url.openConnection() : (HttpURLConnection) _url.openConnection();
			
			urlConn.setRequestMethod(this.httpMethod.toString());
			urlConn.setReadTimeout(timeout);
			urlConn.addRequestProperty("token", Constants.TOKEN);
			
			// Setting header
			headers.forEach((key, value) -> urlConn.setRequestProperty(key, value.toString()));
			
			// Send post data
			if(this.httpMethod == HttpMethod.POST){
				
				// sn=C02G8416DRJM&cn=&locale=&caller=&num=12345
				String urlParameters = this.postParams();
				
				urlConn.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
			}
			
			response.contentType(urlConn.getContentType());
			response.length(urlConn.getContentLength());
			response.date(urlConn.getDate());
			response.msg(urlConn.getResponseMessage());
			response.statusCode(urlConn.getResponseCode());
			
			this.inputStream = urlConn.getInputStream();
			
		} catch (MalformedURLException e) {
			LOG.error("send error:", e);
			response.statusCode(500);
		} catch (IOException e) {
			LOG.error("send error:", e);
			response.statusCode(500);
		}
	}

	private String postParams() {
		if(formdatas.size() > 0){
			// url has been a parameter e.g:sn=C02G8416DRJM&cn=&locale=&caller=&num=12345
			StringBuffer sb = new StringBuffer();
			formdatas.forEach((key, value) -> sb.append( "&" + key + "=" + formdatas.get(key) ));
			return sb.substring(1);
		}
		return null;
	}
	
	@Test
	public static void test() {
		try {
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("connects", Constants.CONNECTIONS);
			params.put("locations", Constants.LOCATIONS.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, "xinyi"));
			params.put("name", Constants.NAME.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, "xinyi"));
			params.put("processDefinitionJson", Constants.PROCESS_DEFINITION_JSON.replaceAll(Constants.COMPANY_CODE_PLACEHOLDER, "xinyi"));
//			params.put("projectName", Constants.PROJECT_NAME);//生产
			params.put("projectName", "show_test");//测试
			params.put("description", Constants.DESCRIPTION);
			
//			post("http://prod-bigdata-4:9030/dolphinscheduler/projects/"+Constants.PROJECT_NAME+"/process/save")//生产
			 String saveProcess = post("http://prod-bigdata-4:9030/dolphinscheduler/projects/show_test/process/save")//测试
			.params(params)
			.body();
			 LOG.info(saveProcess);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e);
			
		}
	}
	
}
