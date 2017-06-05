package cn.pacificimmi.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Record;

public abstract class ComplexModel<T> {
	
	public void bindingData(T model,Record r){
		try{ 
			Field[] field = model.getClass().getDeclaredFields();
	        for(int j=0 ; j<field.length ; j++){
	                String name = field[j].getName();
	          	
	                String type = field[j].getGenericType().toString();
	                if(type.equals("class java.lang.String")){
	                    Method m = model.getClass().getMethod("set"+InitialCapitalization(name),String.class);
	                    String value = r.getStr(name);
	                    if(value!=null)
	                    		m.invoke(model, value);
	                }
	                if(type.equals("class java.lang.Integer")){     
	                    Method m = model.getClass().getMethod("set"+InitialCapitalization(name),Integer.class);
	                    Integer value = r.getInt(name);
	                    if(value != null){
	                    		m.invoke(model, value);
	                    }
	                }
	                if(type.equals("class java.lang.Short")){     
	                    Method m = model.getClass().getMethod("set"+InitialCapitalization(name),Short.class);
	                    Short value = (Short) r.get(name);
	                    if(value != null){
	                    		m.invoke(model, value);                    }
	                }       
	                if(type.equals("class java.lang.Double")){     
	                    Method m = model.getClass().getMethod("set"+InitialCapitalization(name),Double.class);
	                    Double value = r.getDouble(name);
	                    if(value != null){                    
	                    		m.invoke(model, value);
	                    }
	                }                  
	                if(type.equals("class java.lang.Boolean")){
	                    Method m = model.getClass().getMethod("set"+InitialCapitalization(name),Boolean.class);    
	                    Integer value = r.getInt(name);
	                    if(value != null){
	                    		if(value>0)
	                    			m.invoke(model, true);
	                    		else
	                    			m.invoke(model, false);
	                    }
	                }
	                if(type.equals("class java.util.Date")){
	                    Method m = model.getClass().getMethod("set"+InitialCapitalization(name),Date.class);                    
	                    Date value = r.getDate(name);
	                    if(value != null){
	                    		m.invoke(model, value);
	                    }
	                }                
	       }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String InitialCapitalization(String word){
		if(word==null || word.isEmpty())
			return word;
		else{
			char initial = word.charAt(0);
			StringBuffer bf = new StringBuffer();
			bf.append(Character.toUpperCase(initial));
			bf.append(word.substring(1));
			return bf.toString();
		}
	}
}
