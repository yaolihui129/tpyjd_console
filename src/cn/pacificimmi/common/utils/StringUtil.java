package cn.pacificimmi.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class StringUtil {
	private static DateFormat Format = new SimpleDateFormat("MM-dd HH:mm:ss");
	private static DateFormat Format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat FormatDay = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat FormatTime = new SimpleDateFormat("HH:mm:ss");
	private static DateFormat FormatString = new SimpleDateFormat("yyyyMMddHHmmssFFF");
	private static DateFormat FormatStringDay = new SimpleDateFormat("yyyyMMdd");
	public static boolean isEnglishChar(String str){
		Pattern pattern = Pattern.compile("[a-zA-Z]*");  
	    return pattern.matcher(str).matches();
	}
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("[0-9.]*");  
	    return pattern.matcher(str).matches();
	}

	public static boolean validateIntegerStringArray(String[] array){
		for(String a:array){
			if(!isNumeric(a))
				return false;
		}
		return true;
	}
	
	public static boolean isNumericList(String str){  
	    Pattern pattern = Pattern.compile("[0-9,]*");  
	    return pattern.matcher(str).matches();
	}
	
	public static String mmddhmsTime(Date date){
		return Format.format(date);
	}
	
	public static String yyyymmddhmsTime(Date date){
		return Format1.format(date);
	}
	public static String yyyymmddhmsfffStringTime(Date date){
		return FormatString.format(date);
	}
	
	public static String yyyymmddString(Date date){
		return FormatStringDay.format(date);
	}
	
	public static String yyyymmddTime(Date date){
		return FormatDay.format(date);
	}
	
	public static String hmsTime(Date date){
		return FormatTime.format(date);
	}
	
	public static String getNextCategoryCode(String code){
		if(code.length()<4||(code.length()%4)!=0){
			return "";
		}
		
		String tip="",front="";
		boolean flag =false;
		for(int i=0;i<code.length();i++){
			if(i<(code.length()-4)){
				front += code.charAt(i);
			}
			else{
				if(code.charAt(i)!='0'){
					flag=true;
				}
				if(flag)
					tip+=code.charAt(i);
			}
		}
		int nTip = Integer.valueOf(tip);
		nTip++;
		tip = String.valueOf(nTip);
		if(tip.length()>code.length())
			return "";
		int step=4-tip.length();
		for(int i=0;i<step;i++)
			tip = "0"+tip;
		tip = front+tip;
		return tip;
	}
	
	public static String getParentCode(String code){
		if(code.length()>4){
			StringBuffer bf = new StringBuffer();
			int step = code.length()-4;
			for(int i=0;i<step;i++){
				bf.append(code.charAt(i));
			}
			return bf.toString();
		}
		else
			return "0000";
	}

	public static String getParentCodes(String code){
		String codes=code;
		String pcode = code;
		while(pcode.length()>4){
			pcode = getParentCode(pcode);
			if(codes.isEmpty()){
				codes=pcode;
			}
			else{
				codes+=",";
				codes+=pcode;
			}
		}
		
		return codes;
	}
	public static boolean isNumString(String code){
		for(int i=0;i<code.length();i++){
			char ch = code.charAt(i);
			if(ch<48 || ch>57)
				return false;
		}
		
		return true;
	}	
	
	public static boolean validateFieldName(String field){
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_.]*");  
	    return pattern.matcher(field).matches();
	}

	public static boolean validateChinese(String key){
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]*");
	    return pattern.matcher(key).matches();
	}
	
	public static boolean validateSearchKey(String key){
		Pattern pattern = Pattern.compile("[_a-zA-Z0-9\u4e00-\u9fa5]*");
	    return pattern.matcher(key).matches();
	}
	
	public static boolean isValidDateTime(String str) {
	      boolean convertSuccess=true;
	       SimpleDateFormat format = new SimpleDateFormat("yyyy－MM－dd HH:mm:ss");
	       try {
	          format.setLenient(false);
	          format.parse(str);
	       } catch (ParseException e) {
	           convertSuccess=false;
	       } 
	       return convertSuccess;
	}
	
	public static boolean validateData(String str,String mode){
		if(mode.equals("numeric")){
			return isNumeric(str);
		}
		else if(mode.equals("datetime")){
			return isValidDateTime(str);
		}
		else
			return false;
	}
	
	public static String doGet(String url) throws Exception {
        URL localURL = new URL(url);
        
        URLConnection connection = localURL.openConnection();
        
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        /*
         *  httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
         httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         * */
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Content-type", "text/html");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("ContentType", "utf-8");
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
	public  static void main(String[] args) throws Exception{
		System.out.println(StringUtil.doGet("http://localhost:8080/APILogin?username=admin&password=888888&sysid=1"));
		//System.out.println(StringUtil.validateSearchKey("bss0sdf中文"));
//		System.out.println(StringUtil.createSubCode("0001", "console_orgnization", "org_code"));
	}
}
