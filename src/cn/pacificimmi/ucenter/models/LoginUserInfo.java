package cn.pacificimmi.ucenter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.pacificimmi.common.Menu;

public class LoginUserInfo implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4263563624088406808L;

	/**
	 * 功能列表
	 */
	private ArrayList<Menu> menus = new ArrayList<Menu>();
	
	/**
	 * 路径与menucode的映射
	 */
	private Hashtable<String,String> pathCode = new Hashtable<String,String>();

	/**
	 * 用户id
	 */
	private String uid="";
	
	/**
	 * 登录名
	 */
	private String loginName="";
	
	/**
	 * 姓名
	 */
	private String userName="";
	
	/***
	 * 头像
	 */
	private String headImg="";
	
	/**
	 * 数据分流级别
	 */
	private int dataRange=0;
	
	/**
	 * 部门内部员工id集合
	 */
	private String subUsers="";
	
	private String token="";
	
	/***
	 * 判断是否有权访问当前路径
	 * **/
	public boolean allowVisit(String path){
		//超级管理员直接返回true
		if(loginName.equals("admin"))
			return true;
		
		return pathCode.containsKey(path);
	}
	
	public Hashtable<String, String> getPathCode() {
		return pathCode;
	}

	public void setPathCode(Hashtable<String, String> pathCode) {
		this.pathCode = pathCode;
	}

	/**
	 * 返回一级菜单
	 * @return
	 */
	public String getMenuListHtml(String path){

		StringBuffer bf = new StringBuffer();
		for(Menu menu:menus){
			bf.append(menu.getMenuHtml(path));
		}
		return bf.toString();
	}

	public ArrayList<Menu> getMenus() {
		return menus;
	}

	public void setMenus(ArrayList<Menu> menus) {
		this.menus = menus;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	
	public int getDataRange() {
		return dataRange;
	}

	public void setDataRange(int dataRange) {
		this.dataRange = dataRange;
	}

	public String getSubUsers() {
		return subUsers;
	}

	public void setSubUsers(String subUsers) {
		this.subUsers = subUsers;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void loadFromJson(String json){
		JSONObject jo = new JSONObject(json);
		this.uid = String.valueOf(jo.getInt("userid"));
		this.userName = jo.getString("userName");
		this.loginName = jo.getString("loginName");
		this.headImg = jo.getString("headImg");
		this.token = jo.getString("token");
		this.dataRange = jo.getInt("dataRange");
		this.subUsers = jo.getString("subUsers");
		JSONArray jresources = jo.getJSONArray("resources");
		
		for(int i=0;i<jresources.length();i++){
			JSONObject jres = jresources.getJSONObject(i);
			String path = jres.getString("path");
			String code = jres.getString("code");
			pathCode.put(path, code);
		}
		
		JSONArray jmenus = jo.getJSONArray("menus");
		for(int i=0;i<jmenus.length();i++){
			JSONObject jmenu = jmenus.getJSONObject(i);
			menus.add(Menu.loadFromJson(jmenu));
		}
	}

	private String getResources(){
		StringBuffer bu = new StringBuffer();
		bu.append("[");
		boolean isfirst = true;
		Iterator<String> iter = pathCode.keySet().iterator();
		while(iter.hasNext()){
			String pathObj = "{";
			String path = iter.next();
			String code = pathCode.get(path);
			pathObj+="\"path\":\"";
			pathObj+=path;
			pathObj+="\"";
			pathObj+=",";
			pathObj+="\"code\":\"";
			pathObj+=code;
			pathObj+="\"";
			pathObj+="}";
			
			if(isfirst){
				bu.append(pathObj);
				isfirst = false;
			}
			else{
				bu.append(",").append(pathObj);
			}
		}
		
		bu.append("]");
		return bu.toString();
	}
	private String getJsonMenus(){
		StringBuffer bu = new StringBuffer();
			bu.append("[");
			boolean isfirst = true;
			for(Menu m:menus){
				if(isfirst){
					bu.append(m.toJson());
					isfirst=false;
				}
				else
					bu.append(",").append(m.toJson());
			}
			bu.append("]");
		return bu.toString();
	}
	
	public String toJsonString(){
		StringBuffer bf = new StringBuffer();
		bf.append("{");
		bf.append("\"").append("userid").append("\"").append(":").append("\"").append(this.getUid()).append("\"").append(",");
		bf.append("\"").append("loginName").append("\"").append(":").append("\"").append(this.getLoginName()).append("\"").append(",");
		bf.append("\"").append("userName").append("\"").append(":").append("\"").append(this.getUserName()).append("\"").append(",");
		bf.append("\"").append("headImg").append("\"").append(":").append("\"").append(this.getHeadImg()).append("\"").append(",");
		bf.append("\"").append("dataRange").append("\"").append(":").append("\"").append(this.getDataRange()).append("\"").append(",");
		bf.append("\"").append("subUsers").append("\"").append(":").append("\"").append(this.getSubUsers()).append("\"").append(",");
		bf.append("\"").append("token").append("\"").append(":").append("\"").append(this.getToken()).append("\"").append(",");
		bf.append("\"").append("menus").append("\"").append(":").append(getJsonMenus()).append(",");
		bf.append("\"").append("resources").append("\"").append(":").append(getResources());
		bf.append("}");
		return bf.toString();
	}
}
