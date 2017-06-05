package cn.pacificimmi.common;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class DictionaryManager {
	
	private static DictionaryManager instance=null;
	
	private DictionaryManager(){}
	public static synchronized DictionaryManager getInstance(){
		if(instance==null){
			instance = new DictionaryManager();
		}
		
		return instance;
	}
	
/**
 * 返回当前节点及子节点对象
 * @param code 当前节点dict_code编码
 * @param degree 层级 0:返回所有子节点 1:表示当前节点及下一级子节点 2:表示当前节点及下两层级子节点 n:以此类推...
 * @return 当前节点对象
 */
	public Dictionary getAllSubDictionaries(String code,int degree){
		Dictionary root = new Dictionary();
		
		List<Record> dicts;		
		if(degree==0){
			dicts = Db.find("select * from console_dictionary where delete_flag='0' and dict_code like '"+code+"%' order by dict_code asc");
		}
		else{
			int len = code.length();
			len = len + 4*degree;
			dicts = Db.find("select * from console_dictionary where delete_flag='0' and dict_code like '"+code+"%' and length(dict_code)<="+len+" order by dict_code asc");
		}
		
		boolean isfirst = true;
		for(Record rd:dicts){
			Dictionary dict = new Dictionary();
			dict.setDictId(rd.getInt("dict_id"));
			dict.setName(rd.getStr("name"));
			dict.setDictCode(rd.getStr("dict_code"));
			dict.setDictPcode(rd.getStr("dict_pcode"));
			dict.setDictPid(rd.getInt("dict_pid"));
			dict.setSort(rd.getInt("sort"));
			
			if(rd.getStr("short_name")!=null)
				dict.setShortName(rd.getStr("short_name"));
			if(rd.getStr("value")!=null)
				dict.setValue(rd.getStr("value"));
			if(rd.getInt("data_type")!=null)
				dict.setDataType(rd.getInt("data_type"));
			if(isfirst){
				root = dict;
				isfirst=false;
			}
			else
				addDictionary(root,dict);
		}
		return root;
	}
	
	private void addDictionary(Dictionary prt_dict,Dictionary new_dict){
		if(prt_dict.getDictCode().equals(new_dict.getDictPcode())){
				prt_dict.addDictionary(new_dict);
		}
		else{
			for(Dictionary dict:prt_dict.getSubDictionaries()){
				addDictionary(dict,new_dict);
			}
		}
	}
	
	/***
	 * 返回当前节点及某一指定层级的子节点对象
	 * @param code 当前节点dict_code编码
	 * @param degree 层级 1：子集 2：孙子级别 n:以此类推
	 * @return  当前节点对象
	 */
	public Dictionary getSubDictionariesByDegree(String code,int degree){
		Dictionary root = new Dictionary();
		
		List<Record> dicts = new ArrayList<Record>();		
		if(degree>0){
			int len = code.length();
			len = len + 4*degree;
			dicts = Db.find("select * from console_dictionary where delete_flag='0' and dict_code like '"+code+"%' and (length(dict_code)="+len+" or length(dict_code)="+code.length()+") order by dict_code asc");
		}
		
		boolean isfirst = true;
		for(Record rd:dicts){
			Dictionary dict = new Dictionary();
			dict.setDictId(rd.getInt("dict_id"));
			dict.setName(rd.getStr("name"));
			dict.setDictCode(rd.getStr("dict_code"));
			dict.setDictPcode(rd.getStr("dict_pcode"));
			dict.setDictPid(rd.getInt("dict_pid"));
			dict.setSort(rd.getInt("sort"));
			
			if(rd.getStr("short_name")!=null)
				dict.setShortName(rd.getStr("short_name"));
			if(rd.getStr("value")!=null)
				dict.setValue(rd.getStr("value"));
			if(rd.getInt("data_type")!=null)
				dict.setDataType(rd.getInt("data_type"));
			if(isfirst){
				root = dict;
				isfirst=false;
			}
			else{
				root.addDictionary(dict);
			}
		}
		return root;
	}

}
