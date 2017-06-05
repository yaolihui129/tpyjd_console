package cn.pacificimmi.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.jasper.tagplugins.jstl.core.Param;

import cn.pacificimmi.common.utils.objects.Activity;
import cn.pacificimmi.common.utils.objects.Customer;
import net.sf.json.JSONObject;

public class Client {
	
	public static String doGet(String url) throws Exception {
        URL localURL = new URL(url);
        
        URLConnection connection = localURL.openConnection();
        
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        try {
        		if (httpURLConnection.getResponseCode() == 200) {
            inputStream = httpURLConnection.getInputStream();
           // inputStreamReader = new InputStreamReader(inputStream); 
           // reader = new BufferedReader(inputStreamReader);
            
            
           /* 字符流设置编码格式
            * */
             reader = new BufferedReader(  
            	       new InputStreamReader(inputStream,"utf-8"));  
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        		}
        } finally {
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }
       /* System.out.println("resultBuffer--"+resultBuffer); */
        return resultBuffer.toString();
    }
	
	public static String doPost(String Url,Map<String,String> params) throws IOException{
		//建立连接
		StringBuffer resultBuffer=new StringBuffer();
		  
	    URL url=new URL(Url);
	    HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
	    //设置参数
	    httpConn.setDoOutput(true);   //需要输出
	    httpConn.setDoInput(true);   //需要输入
	    httpConn.setUseCaches(false);  //不允许缓存
	    httpConn.setRequestMethod("POST");   //设置POST方式连接
	    //设置请求属性
	    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	    httpConn.setRequestProperty("Charset", "UTF-8");
	    //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
	    httpConn.connect();
	    //建立输入流，向指向的URL传入参数
	    OutputStreamWriter osw = new OutputStreamWriter(httpConn  
	            .getOutputStream(), "UTF-8");
	    osw.write(getParamStr(params));
	    osw.flush();
	    osw.close();
	    //获得响应状态
	    int resultCode=httpConn.getResponseCode();
	    if(HttpURLConnection.HTTP_OK==resultCode){
	    
	      String readLine=new String();
	      BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
	      while((readLine=responseReader.readLine())!=null){
	    	  	resultBuffer.append(readLine).append("\n");
	      }
	      responseReader.close();
	    } 
	    
	    return resultBuffer.toString();
	}
	
	private static String getParamStr(Map<String,String> params){

			StringBuffer ParamStr=new StringBuffer();
			boolean flag = false;
			Iterator<String> iter = params.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				String value = params.get(key);
				if(flag){
					ParamStr.append("&");
				}
				else{
					flag=true;
				}
				ParamStr.append(key).append("=").append(value);
			}
			System.out.println(ParamStr.toString());
			return ParamStr.toString();
		
	}
	
	public static String getSign(Map<String,String> params,String key){
		String paramStr = getParamStr(params);
		return MD5Util.MD5(paramStr+key).toUpperCase();
	}
	
	public static void main(String[] args) throws Exception{
		/***
		 * 测试单独获取临时密钥 APISign
		 */
//		String Url = "http://localhost:8080/APISign";
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("username", "jeff");
//		params.put("password", "jeff123");
//		params.put("expired","20");
//		System.out.println(Client.doPost(Url, params));
		
		/***
		 * 测试帐密登录 APILogin
		 */
//		String Url = "http://localhost:8080/APILogin?username=jeff&password=jeff123&sysid=2";
//		System.out.println(Client.doGet(Url));
		
		/**
		 * 测试临时密钥登录
		 */
//		String Url = "http://localhost:8080/APITokenLogin?sysid=2&token=D/6jk3vtEbgKvgzZqoAzQohr5bN1c2VyPWplZmYmZXhwaXJlZD0xNDg4ODY2MTIyMDI1AA==";
//		System.out.println(Client.doGet(Url));
		
		/**
		 * 测试获取用户列表接口 APIUsers
		 */
//		String Url = "http://localhost:8080/APIUsers";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 测试获取组织机构信息接口 APIOrgnization
		 */
//		String Url = "http://localhost:8080/APIOrgnization";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 测试数据修改接口
		 */
//		String Url = "http://localhost:8080/APIUpdate";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		params.put("opt_type", "employee");
//		params.put("opt_info", "{\"user_id\":212,\"dep_code\":\"00010027\"}");
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 测试新增数据接口：message
		 */
//		String Url = "http://localhost:8080/APIAdd";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		params.put("opt_type", "message");
//		params.put("opt_info", "{\"target\":\"employee\",\"s_ids\":\"11\",\"title\":\"111\",\"content\":\"中文\"}");
//		
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 测试新增数据接口：assigned_steward
		 */
//		String Url = "http://localhost:8080/APIAdd";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		params.put("opt_type", "assigned_steward");
//		params.put("opt_info", "{\"s_userid\":88,\"s_custid\":66,\"steward_type\":\"顾问\"}");
//		
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 测试新增数据接口：customer
		 */
//		String Url = "http://localhost:8080/APIAdd";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		params.put("opt_type", "customer");
//		//为Customer对象赋值
//		Customer cus = new Customer();
//		cus.setS_custid(123);
//		cus.setMobile("18678778987");
//		cus.setNick_name("新用户");
//		
//		JSONObject json = JSONObject.fromObject(cus);
//		params.put("opt_info",json.toString()); 
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 测试新增数据接口：activity
		 */
//		String Url = "http://localhost:8080/APIAdd";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		params.put("opt_type", "activity");
//		//为Activity对象赋值
//		Activity activity = new Activity();
//		activity.setS_activityid(26);
//		activity.setActivity_name("测试crm同步过来的市场活动2!");
//		activity.setStart_time(System.currentTimeMillis());
//
//		JSONObject json = JSONObject.fromObject(activity);
//		params.put("opt_info",json.toString());
//		
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 修改数据接口：change_steward
		 */
//		String Url = "http://localhost:8080/APIUpdate";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		params.put("opt_type", "change_steward");
//		params.put("opt_info", "{\"s_userid\":138,\"s_custid\":66,\"steward_type\":\"顾问\"}");
//		
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
		
		/**
		 * 修改数据接口：customer
		 */
//		String Url = "http://localhost:8080/APIUpdate";
//		Map<String,String> params = new HashMap<String,String>();
//		String uuid = UUID.randomUUID().toString();
//		params.put("sysid", "2");
//		params.put("uuid", uuid);
//		params.put("opt_type", "customer");
//		//为Customer对象赋值
//		Customer cus = new Customer();
//		cus.setS_custid(123);
//		cus.setMobile("18678778987");
//		cus.setNick_name("再改一次");
//		
//		JSONObject json = JSONObject.fromObject(cus);
//		params.put("opt_info",json.toString()); 
//		//密钥key由权限管理平台提供
//		String key="123456";
//		String sign=Client.getSign(params,key);
//		params.put("sign", sign);
//		System.out.println(Client.doPost(Url, params));
	}
}