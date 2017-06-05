package cn.pacificimmi.common.interceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import cn.pacificimmi.common.AccessToken;
import cn.pacificimmi.common.models.Websys;

public class APISignInterCeptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller ctr = inv.getController();
		Map<String,Object> rst = new HashMap<String,Object>();
	
		Map<String,String[]> params = ctr.getParaMap();
		String[] sign = params.get("sign");
		String[] sysid = params.get("sysid");
		if(sign!=null && sign.length>0 && sign!=null && sysid.length>0){
			//params.remove("sign");
			String paramStr = getParamStr(params);
			
			Websys ws = Websys.dao.findById(sysid[0]);
			if(ws!=null && AccessToken.checkSign(paramStr, ws.getPublicKey(), sign[0])){
				inv.invoke();
			}
			else{
				rst.put("status", 6);
				rst.put("msg", "数字签名认证失败");
				ctr.renderJson(rst);
			}
		}
		else{
			rst.put("status", 1);
			rst.put("msg", "参数错误");
			ctr.renderJson(rst);
		}
	}
	
	private static String getParamStr(Map<String,String[]> params){
		StringBuffer ParamStr=new StringBuffer();
		boolean flag = false;
		Iterator<String> iter = params.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			if(key.equals("sign"))
				continue;
			String[] value = params.get(key);
			if(flag){
				ParamStr.append("&");
			}
			else{
				flag=true;
			}
			
			ParamStr.append(key).append("=");
			if(value!=null && value.length>0)
				ParamStr.append(value[0]);
		}
		
		return ParamStr.toString();
	}
}
