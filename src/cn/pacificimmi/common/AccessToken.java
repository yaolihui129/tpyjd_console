package cn.pacificimmi.common;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

import cn.pacificimmi.common.utils.MD5Util;

public class AccessToken {
	public static String getToken(String username, String secret_key, 
	      long expired) {
	        if (empty(username) || empty(secret_key)){
	            return null;
	        }
		
	        expired = System.currentTimeMillis() + expired*60*1000;  
	        String plain_text = "";
	        
	        plain_text = String.format("user=%s&expired=%d",username,expired);

	        byte[] bin = HmacUtils.hmacSha1(secret_key, plain_text);
	        byte[] all = new byte[bin.length + plain_text.getBytes().length +1];
	       
	        System.arraycopy(bin, 0, all, 0, bin.length);
	        System.arraycopy(plain_text.getBytes(), 0, all, bin.length, plain_text.getBytes().length);
	        
	        all = Base64.encodeBase64(all);
	        return new String(all);
	}
	
	public static String[] getParams(String queryStr){
		String[] params = new String[2];
		Pattern p = Pattern.compile("(user=)([a-zA-Z_0-9]*)(&expired=)([0-9]*)");
		Matcher m = p.matcher(queryStr);

		if(m.find() && m.group(2)!=null && m.group(4)!=null){
			params[0]=m.group(2);
			params[1]=m.group(4);
		}
		
		return params;
	}
	public static boolean empty(String s){
		return s == null || s.trim().equals("") || s.trim().equals("null");
	}
	
	public static String getLoginUserName(String token,String secret_key){
		String username="";
		byte[] all = Base64.decodeBase64(token);
		byte[] mb = new byte[20];
		byte[] qb = new byte[all.length-20-1];
		
		for(int i=0;i<all.length-1;i++){
			if(i>19){
				qb[i-20]=all[i];
			}
			else{
				mb[i]=all[i];
			}
		}
		
		String[] params = getParams(new String(qb));
		if(params.length==2)
			username=params[0];
		return username;
	}
	public static boolean checkToken(String token,String secret_key){
		byte[] all = Base64.decodeBase64(token);
		byte[] mb = new byte[20];
		byte[] qb = new byte[all.length-20-1];
		
		for(int i=0;i<all.length-1;i++){
			if(i>19){
				qb[i-20]=all[i];
			}
			else{
				mb[i]=all[i];
			}
		}
		
		//验证
		byte[] hmacsha = HmacUtils.hmacSha1(secret_key, new String(qb));
		
		if(Arrays.equals(mb, hmacsha)){
			if(System.currentTimeMillis()<Long.valueOf(getParams(new String(qb))[1]))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	/***
	 * 签名验证
	 * @param paramStr
	 * @param key
	 * @param sign
	 * @return
	 */
	public static boolean checkSign(String paramStr,String key,String sign){
		if(MD5Util.MD5(paramStr+key).toUpperCase().equals(sign)){
			return true;
		}
		else
			return false;
	}

	public static void main(String[] args){
		//String token=AccessToken.getToken("zhaw34523452345e145", "tpyjd", 1);
		//System.out.println(token);
		System.out.println(AccessToken.checkToken("LWPj+St3xuTYs/GNcvsOhhn4Nid1c2VyPXpoYXczNDUyMzQ1MjM0NWUxNDUmZXhwaXJlZD0xNDc5NzA5MDE1MTI2AA==", "tpyjd"));
	}
}
