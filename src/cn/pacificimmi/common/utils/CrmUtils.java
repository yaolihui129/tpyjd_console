package cn.pacificimmi.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.jfinal.kit.PropKit;

public class CrmUtils {
	public static String getBinding(){
		String userName=PropKit.get("CRM_USER");
		String password=PropKit.get("CRM_PASSWORD");
		Map<String,String> params = new HashMap<String,String>();
		params.put("serviceName", "clogin");
		params.put("userName", userName);
		params.put("password", password);
		String url=PropKit.get("CRM_API_URL");
		String binding="";
		try {
			String json = Client.doPost(url,params);
			if(!json.isEmpty()){
				JSONObject jo = new JSONObject(json);
				boolean result = jo.getBoolean("result");
				if(result){
					binding = jo.getString("binding");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return binding;
	}
	
	public static void main(String[] args){
		System.out.println(CrmUtils.getBinding());
	}
}
