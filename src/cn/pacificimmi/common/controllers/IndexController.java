package cn.pacificimmi.common.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.pacificimmi.common.AccessToken;
import cn.pacificimmi.common.Menu;
import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.APISignInterCeptor;
import cn.pacificimmi.common.models.Activity;
import cn.pacificimmi.common.models.Custinfo;
import cn.pacificimmi.common.models.Message;
import cn.pacificimmi.common.models.Orgnization;
import cn.pacificimmi.common.models.StewardSign;
import cn.pacificimmi.common.models.StewardUser;
import cn.pacificimmi.common.models.User;
import cn.pacificimmi.common.models.Websys;
import cn.pacificimmi.common.utils.MD5Util;
import cn.pacificimmi.common.utils.StringUtil;
import cn.pacificimmi.ucenter.models.LoginUserInfo;

public class IndexController extends Controller {
	private static Logger log = LoggerFactory.getLogger(IndexController.class);
	/****
	 * 默认初始化页面
	 */
	public void index(){
		LoginUserInfo ui = getSessionAttr("LoginUserInfo");
		if(ui==null){
			String sysname = PropKit.get("sysname", "权限管理系统");
			setAttr("sysname",sysname);
			
			String rolesystem = PropKit.get("rolesystem");
			if(!rolesystem.endsWith("/"))
				rolesystem+="/";
			setAttr("rolesystem",rolesystem);
			
			this.renderJsp("/views/login.jsp");
		}
		else{
			String menus = ui.getMenuListHtml("/");
			setAttr("menus", menus);
			this.renderJsp("/views/indexPage.jsp");
		}
	}
	
	/**
	 * 密码修改
	 */
	public void password(){
		String password = getPara("password");//新密码
		String oldPassword = getPara("oldpass");//原密码
		if(password==null){
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("status", 1);
			result.put("msg", "param error");
			this.renderJson(result);
		}
		else if(this.getSessionAttr("LoginUserInfo")==null){
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("status", 2);
			result.put("msg", "error");
			this.renderJson(result);
		}
		else{
			Map<String,Object> result = new HashMap<String,Object>();
			LoginUserInfo ui =getSessionAttr("LoginUserInfo");
			
			//校验原密码是否正确
			Record r = Db.findFirst("select user_id from console_user where user_id='" + ui.getUid() + "' and password ='" + MD5Util.MD5(oldPassword) + "'");
			if(r == null){
				result.put("status", 400);//制定原密码错误返回400,以区别其他
				result.put("msg", "原密码错误!");
			}else{
				Db.update("update console_user set password='"+MD5Util.MD5(password)+"' where user_id='"+ui.getUid()+"'");
				
				result.put("status", 0);
				result.put("msg", "success");
			}
			this.renderJson(result);
		}
	}
	
	/**
	 * 原密码输入校验
	 */
	@Clear
	public void valiOldPassword(){
		String oldPassword = getPara("oldpass");//原密码
		Map<String,Object> result = new HashMap<String,Object>();
		LoginUserInfo ui =getSessionAttr("LoginUserInfo");
		
		//校验原密码是否正确
		Record r = Db.findFirst("select user_id from console_user where user_id='" + ui.getUid() + "' and password ='" + MD5Util.MD5(oldPassword) + "'");
		if(r == null){
			result.put("status", 400);//制定原密码错误返回400,以区别其他
			result.put("msg", "原密码错误!");
		}else{
			result.put("status", 0);
			result.put("msg", "success");
		}
		this.renderJson(result);
	}
	
	/**
	 * 没有权限
	 */
	public void noPermissions(){
		log.info("无权限,跳转提示页面");
		this.setAttr("errorInfo","对不起!您尚未分配该权限!如有疑问请联系权限管理员.");
		this.renderJsp("common/error.jsp");
	}
	/**
	 * 登录请求处理
	 */
	public void login(){
		String username = getPara("username");
		String password = getPara("password");
		//系统id
		String systemid=PropKit.get("sysid");
		String rolesystem = PropKit.get("rolesystem");
		if(!rolesystem.endsWith("/"))
			rolesystem+="/";
		StringBuffer bf = new StringBuffer();
		bf.append(rolesystem);
		bf.append("APILogin?");
		bf.append("username=").append(username).append("&");
		bf.append("password=").append(password).append("&");
		bf.append("sysid=").append(systemid);
		try{
			String jsonStr = StringUtil.doGet(bf.toString());
			JSONObject rst = new JSONObject(jsonStr);
			if(rst.getInt("status")==0){
				LoginUserInfo lui = new LoginUserInfo();
				String luiStr = rst.getJSONObject("userinfo").toString();
				lui.loadFromJson(luiStr);
				this.setSessionAttr("LoginUserInfo", lui);
				
				ArrayList<Websys> wss = new ArrayList<Websys>();
				JSONArray platforms = rst.getJSONArray("platforms");
				for(int i=0;i<platforms.length();i++){
					JSONObject jo =  platforms.getJSONObject(i);
					Websys ws = new Websys();
					ws.setName(jo.getString("name"));
					ws.setDomain(jo.getString("url"));
					ws.setOathurl(jo.getString("tokenLoginUrl"));
					wss.add(ws);
				}
				this.setSessionAttr("platforms", wss);
				
				OperateLogger.log(this, "首页", "登录", "");
				this.redirect("/");
			}
			else
			{
				this.redirect("/");
			}
		}
		catch(Exception e){
			this.redirect("/");
		}
	}
	
	/**
	 * 登录请求处理
	 */
	public void tokenLogin(){
		String token = getPara("token");
		//系统id
		String systemid=PropKit.get("sysid");
		String rolesystem = PropKit.get("rolesystem");
		if(!rolesystem.endsWith("/"))
			rolesystem+="/";
		StringBuffer bf = new StringBuffer();
		bf.append(rolesystem);
		bf.append("APITokenLogin?");
		bf.append("token=").append(token).append("&");
		bf.append("sysid=").append(systemid);
		try{
			String jsonStr = StringUtil.doGet(bf.toString());
			JSONObject rst = new JSONObject(jsonStr);
			if(rst.getInt("status")==0){
				LoginUserInfo lui = new LoginUserInfo();
				String luiStr = rst.getJSONObject("userinfo").toString();
				lui.loadFromJson(luiStr);
				this.setSessionAttr("LoginUserInfo", lui);
				
				ArrayList<Websys> wss = new ArrayList<Websys>();
				JSONArray platforms = rst.getJSONArray("platforms");
				for(int i=0;i<platforms.length();i++){
					JSONObject jo =  platforms.getJSONObject(i);
					Websys ws = new Websys();
					ws.setName(jo.getString("name"));
					ws.setDomain(jo.getString("url"));
					ws.setOathurl(jo.getString("tokenLoginUrl"));
					wss.add(ws);
				}
				this.setSessionAttr("platforms", wss);
				
				this.redirect("/");
			}
			else
			{
				this.redirect("/");
			}
		}
		catch(Exception e){
			this.redirect("/");
		}
	}
	
	private String getSubUsers(String uid){
		HashSet<Integer> users = new HashSet<Integer>();
		StringBuffer bf = new StringBuffer();
		String sql="select co.org_code from console_user_station as cus left join console_station as cs on cus.station_id=cs.station_id left join console_orgnization as co on cs.com_id=co.org_id where cus.user_id='"+uid+"'";
		List<Record> stations = Db.find(sql);
		
		for(Record r:stations){
			List<Record> uids = Db.find("select user_id from console_user as cu left join console_orgnization as co on cu.dep_id=co.org_id where co.org_code like '"+r.getStr("org_code")+"%'");
			for(Record r1:uids){
				users.add(r1.getInt("user_id"));
			}
		}
		
		Iterator<Integer> iter = users.iterator();
		boolean first =true;
		while(iter.hasNext()){
			if(first){
				first=false;
				bf.append(iter.next());
			}
			else{
				bf.append(",").append(iter.next());
			}
		}
		return bf.toString();
	}
	
	/**
	 * 返回用户在当前系统中的所有权限路径
	 * @param sysid
	 * @param userid
	 * @return
	 */
	private Hashtable<String,String> getResources(String sysid,String userid){
		Hashtable<String,String> resources = new Hashtable<String,String>();
		List<Record> futures = Db.find("SELECT * FROM console_resource where sys_id='"+sysid+"' and delete_flag='0' and res_id in (SELECT distinct(res_id) FROM console_station as s,console_user_station as mus,console_role_station as mrs,console_role_resource as mrr where s.station_id=mus.station_id and s.delete_flag='0' and mus.station_id=mrs.station_id and mrs.role_id=mrr.role_id and mus.user_id='"+userid+"') order by res_code asc;");
		for(Record rd:futures){
			String path = rd.getStr("url");
			if(path!=null && !path.isEmpty()){
				String code = rd.getStr("res_code");
				resources.put(path, code);
			}
		}
		return resources;
	}
	
	/**
	 * 增加一级菜单
	 * @return
	 */
	private ArrayList<Menu> loadMainMenu(String sysid,String userid){
		//加载层级菜单
		ArrayList<Menu> menus = new ArrayList<Menu>();
		Menu index = new Menu();
		index.setId("0000");
		index.setIcon("icon-home");
		index.setTitle("主页");
		index.setHref("/");
		index.setResType("SYS_MENU");
		menus.add(index);
		List<Record> futures = Db.find("SELECT * FROM console_resource where delete_flag='0' and  length(res_code)=8 and res_type='SYS_MENU' and sys_id='"+sysid+"' and res_id in (SELECT distinct(res_id) FROM console_station as s,console_user_station as mus,console_role_station as mrs,console_role_resource as mrr where s.station_id=mus.station_id and s.delete_flag='0' and mus.station_id=mrs.station_id and mrs.role_id=mrr.role_id and mus.user_id='"+userid+"') order by sort desc;");

		for(Record rd:futures){
			Menu menu = new Menu();
			menu.setId(rd.getStr("res_code"));
			menu.setTitle(rd.getStr("name"));
			menu.setIcon(rd.getStr("icon"));
			menu.setHref(rd.getStr("url"));
			menu.setResType(rd.getStr("res_type"));
			ArrayList<Menu> submenus =loadUserSubMenu(userid,sysid,rd.getStr("res_code"));
			if(submenus.size()>0)
				menu.setSubMenu(submenus);
				menus.add(menu);
			}
		return menus;
	}
	
	/**
	 * 增加一级菜单
	 * @return
	 */
	private ArrayList<Menu> loadMainMenuBySupper(String sysid){
		//加载层级菜单
				ArrayList<Menu> menus = new ArrayList<Menu>();
				Menu index = new Menu();
				index.setId("0000");
				index.setIcon("icon-home");
				index.setTitle("主页");
				index.setHref("/");
				index.setResType("SYS_MENU");
				menus.add(index);
				
				List<Record> futures = Db.find("SELECT * FROM console_resource where delete_flag='0' and length(res_code)=8 and res_type='SYS_MENU' and sys_id='"+sysid+"' order by sort desc;");
				
				for(Record rd:futures){
					Menu menu = new Menu();
					menu.setId(rd.getStr("res_code"));
					menu.setIcon(rd.getStr("icon"));
					menu.setTitle(rd.getStr("name"));
					menu.setHref(rd.getStr("url"));
					menu.setResType(rd.getStr("res_type"));
					ArrayList<Menu> submenus =loadSubMenu(sysid,rd.getStr("res_code"));
					if(submenus.size()>0)
						menu.setSubMenu(submenus);
					menus.add(menu);
				}
		return menus;
	}
	
	/***
	 * 递归加载子菜单
	 * @param code
	 * @return
	 */
	private ArrayList<Menu> loadSubMenu(String sysid,String code){
		ArrayList<Menu> menus = new ArrayList<Menu>();
		List<Record> futures = Db.find("SELECT * FROM console_resource where delete_flag='0' and res_code like '"+code+"%' and sys_id='"+sysid+"' and res_type='SYS_MENU' and length(res_code)="+(code.length()+4)+" order by sort desc;");
		for(Record rd:futures){
			Menu menu = new Menu();
			menu.setId(rd.getStr("res_code"));
			menu.setIcon(rd.getStr("icon"));
			menu.setTitle(rd.getStr("name"));
			menu.setHref(rd.getStr("url"));
			menu.setResType(rd.getStr("res_type"));
			ArrayList<Menu> submenus =loadSubMenu(sysid,rd.getStr("res_code"));
			if(submenus.size()>0)
				menu.setSubMenu(submenus);
			menus.add(menu);
		}
		return menus;
	}
	
	/***
	 * 递归加载子菜单
	 * @param code
	 * @return
	 */
	private ArrayList<Menu> loadUserSubMenu(String userid,String sysid,String code){
		ArrayList<Menu> menus = new ArrayList<Menu>();
		List<Record> futures = Db.find("SELECT * FROM console_resource where delete_flag='0' and res_code like '"+code+"%' and res_type='SYS_MENU' and length(res_code)="+(code.length()+4)+" and sys_id='"+sysid+"' and res_id in (SELECT distinct(res_id) FROM console_station as s ,console_user_station as mus,console_role_station as mrs,console_role_resource as mrr where s.station_id=mus.station_id and s.delete_flag='0' and mus.station_id=mrs.station_id and mrs.role_id=mrr.role_id and mus.user_id='"+userid+"') order by sort desc;");
		for(Record rd:futures){
			Menu menu = new Menu();
			menu.setId(rd.getStr("res_code"));
			menu.setIcon(rd.getStr("icon"));
			menu.setTitle(rd.getStr("name"));
			menu.setHref(rd.getStr("url"));
			menu.setResType(rd.getStr("res_type"));
			ArrayList<Menu> submenus =loadUserSubMenu(userid,sysid,rd.getStr("res_code"));
			if(submenus.size()>0)
				menu.setSubMenu(submenus);
			menus.add(menu);
		}
		return menus;
	}
	
	/****
	 * 登出请求处理
	 */
	public void loginOut(){	
		OperateLogger.log(this, "首页", "退出", "");
		this.removeSessionAttr("LoginUserInfo");
		this.redirect("/");
	}
	
	/**
	 * 公共登录api接口
	 */
	public void APILogin(){
		String username = getPara("username");
		String password = getPara("password");
		String systemid = getPara("sysid");

		Map<String, Object> res = new HashMap<String, Object>();
		/**
		 * 加载用户信息
		 */
		LoginUserInfo ui = new LoginUserInfo();
		if(username!=null && !username.isEmpty() && StringUtil.validateFieldName(username)
			&& password!=null && !password.isEmpty()){
			User user = User.dao.findFirst("select * from console_user where delete_flag='0' and frozen='0' and login_name='"+username+"'");
			if(user!=null && (user.getPassword().equals(MD5Util.MD5(password)) ||(username.equals("admin") && password.equals(PropKit.get("super","superadmin"))))){
				ui.setUid(String.valueOf(user.getInt("user_id")));
				ui.setUserName(user.getStr("user_name"));
				ui.setHeadImg(user.getStr("head_img"));
				ui.setLoginName(user.getStr("login_name"));
				ui.setDataRange(Integer.valueOf(user.getDataRange()));
				ui.setSubUsers(getSubUsers(ui.getUid()));
				//放入临时钥匙
				ui.setToken(AccessToken.getToken(username, PropKit.get("secret_key"), PropKit.getInt("expired")));
				
				ArrayList<Menu> menus ;
				//超级用户特殊处理
				if(username.equals("admin") && systemid.equals("1"))
					menus = loadMainMenuBySupper(systemid);
				else
					menus= loadMainMenu(systemid,String.valueOf(user.getUserId()));
				ui.setMenus(menus);
				
				//加载当前用户的权限列表
				ui.setPathCode(getResources(systemid,String.valueOf(user.getUserId())));
				
				StringBuffer response = new StringBuffer();
				response.append("{");
				response.append("\"status\":0,").append("\"msg\":\"ok\",");
				response.append("\"userinfo\":").append(ui.toJsonString());
				List<Websys> list = Websys.dao.find("select * from console_websys where delete_flag='0' order by sys_id asc");
				response.append(",");
				response.append("\"platforms\":[");
				boolean first=true;
				for(Websys ws:list){
					if(first){
						first=false;
						response.append("{\"name\":\""+ws.getName()+"\",\"url\":\""+ws.getDomain()+"\",\"tokenLoginUrl\":\""+ws.getOathurl()+"\"}");
					}
					else{
						response.append(",").append("{\"name\":\""+ws.getName()+"\",\"url\":\""+ws.getDomain()+"\",\"tokenLoginUrl\":\""+ws.getOathurl()+"\"}");
					}
				}
				response.append("]");
				response.append("}");
				this.renderJson(response.toString());
				//this.renderJson(responseJson);
			}
			else{
				res.put("status", 5);
				res.put("msg", "no user");
				this.renderJson(res);
			}
		}
		else{
			res.put("status", 1);
			res.put("msg", "params error");
			this.renderJson(res);
		}
	}
	
	@Before(APISignInterCeptor.class)
	public void APIOrgnization(){
//		String token = getPara("token");
		Map<String, Object> res = new HashMap<String, Object>();
//		if(token!=null && AccessToken.checkToken(token, PropKit.get("secret_key"))){
			List<Orgnization> orgs = Orgnization.dao.find("select * from console_orgnization order by org_code asc");
			if(orgs.size()>0){
				
//				Orgnization rootObj = orgs.get(0);
//				Org root = new Org();
//				root.setName(rootObj.getName());
//				if(rootObj.getCreateTime()!=null)
//					root.setCreateTime(StringUtil.yyyymmddhmsTime(rootObj.getCreateTime()));
//				if(rootObj.getDescription()!=null)
//					root.setDescription(rootObj.getDescription());
//				root.setOrgCode(rootObj.getOrgCode());
//				root.setOrgId(rootObj.getOrgId());
//				root.setOrgPcode(rootObj.getOrgPcode());
//				root.setOrgType(rootObj.getOrgType());
//				if(rootObj.getShortName()!=null)
//					root.setShortName(rootObj.getShortName());
//				if(rootObj.getSort()!=null)
//					root.setSort(rootObj.getSort());
				
				StringBuffer bf = new StringBuffer();
				boolean flag = true;
				for(int i=0;i<orgs.size();i++){
					Orgnization nObj = orgs.get(i);
					Org so = new Org();
					so.setName(nObj.getName());
					if(nObj.getCreateTime()!=null)
						so.setCreateTime(StringUtil.yyyymmddhmsTime(nObj.getCreateTime()));
//					if(nObj.getDescription()!=null)
//						so.setDescription(nObj.getDescription());
					so.setOrgCode(nObj.getOrgCode());
					so.setOrgId(nObj.getOrgId());
//					so.setOrgPcode(StringUtil.getParentCode(nObj.getOrgCode()));
					so.setOrgType(nObj.getOrgType());
//					if(nObj.getShortName()!=null)
//						so.setShortName(nObj.getShortName());
//					if(nObj.getSort()!=null)
//						so.setSort(nObj.getSort());
//					addOrg(root,so);
					if(flag){
						flag=false;
						bf.append(so.toJson());
					}
					else{
						bf.append(",");
						bf.append(so.toJson());
					}
					
				}
				String rst ="{\"status\": 0,\"msg\":\"ok\",";
				rst +="\"orglist\":[";
				rst += bf.toString();
				rst +="]}";
				this.renderJson(rst);
			}
			else{
				res.put("status", 100);
				res.put("msg", "no data");
				this.renderJson(res);
			}
	}
	
	/***
	 * 平台间跳转接口
	 */
	public void APITokenLogin(){
		String token = getPara("token");
		String username=AccessToken.getLoginUserName(token, PropKit.get("secret_key"));
		String systemid = getPara("sysid");

		Map<String, Object> res = new HashMap<String, Object>();
		/**
		 * 加载用户信息
		 */
		LoginUserInfo ui = new LoginUserInfo();
		if(username!=null && !username.isEmpty() && StringUtil.validateFieldName(username) &&
				AccessToken.checkToken(token, PropKit.get("secret_key")) && !username.equals("admin")){
			User user = User.dao.findFirst("select * from console_user where delete_flag='0' and frozen='0' and login_name='"+username+"'");
			if(user!=null){
				ui.setUid(String.valueOf(user.getInt("user_id")));
				ui.setUserName(user.getStr("user_name"));
				ui.setHeadImg(user.getStr("head_img"));
				ui.setLoginName(user.getStr("login_name"));
				ui.setDataRange(Integer.valueOf(user.getDataRange()));
				ui.setSubUsers(getSubUsers(ui.getUid()));
				//放入临时钥匙
				ui.setToken(AccessToken.getToken(username, PropKit.get("secret_key"), PropKit.getInt("expired")));
				
				ArrayList<Menu> menus ;
				//超级用户特殊处理
				if(username.equals("admin"))
					menus = loadMainMenuBySupper(systemid);
				else
					menus= loadMainMenu(systemid,String.valueOf(user.getUserId()));
				ui.setMenus(menus);
				
				//加载当前用户的权限列表
				ui.setPathCode(getResources(systemid,String.valueOf(user.getUserId())));
				
				StringBuffer response = new StringBuffer();
				response.append("{");
				response.append("\"status\":0,").append("\"msg\":\"ok\",");
				response.append("\"userinfo\":").append(ui.toJsonString());
				List<Websys> list = Websys.dao.find("select * from console_websys where delete_flag='0' order by sys_id asc");
				response.append(",");
				response.append("\"platforms\":[");
				boolean first=true;
				for(Websys ws:list){
					if(first){
						first=false;
						response.append("{\"name\":\""+ws.getName()+"\",\"url\":\""+ws.getDomain()+"\",\"tokenLoginUrl\":\""+ws.getOathurl()+"\"}");
					}
					else{
						response.append(",").append("{\"name\":\""+ws.getName()+"\",\"url\":\""+ws.getDomain()+"\",\"tokenLoginUrl\":\""+ws.getOathurl()+"\"}");
					}
				}
				response.append("]");
				response.append("}");
				
				this.renderJson(response.toString());
			}
			else{
				res.put("status", 5);
				res.put("msg", "no user");
				this.renderJson(res);
			}
		}
		else{
			res.put("status", 1);
			res.put("msg", "params error");
			this.renderJson(res);
		}
	}
	
	/****
	 * 权限管理系统数据修改接口
	 */
	@Before(APISignInterCeptor.class)
	public void APIUpdate(){
		Map<String,Object> rst = new HashMap<String,Object>();
		String opt_type = this.getPara("opt_type");
		String opt_info = this.getPara("opt_info");
		String uuid = this.getPara("uuid");
		
		log.info("uuid|"+uuid+"|opt_info|"+opt_info);
		if(opt_type!=null && !opt_type.isEmpty()
				&& opt_info!=null && !opt_info.isEmpty()){
			//修改企业员工信息
			if(opt_type.equals("employee")){
				updateEmployee(opt_info);
			}
			//重新分配管家
			else if(opt_type.equals("change_steward")){
				changeSteward(opt_info);
			}
			//修改客户信息
			else if(opt_type.equals("customer")){
				updateCustomer(opt_info);
			}
			else{
				rst.put("status", 9);
				rst.put("msg", "无操作对象");
				renderJson(rst);
			}
		}
		else{
			rst.put("status", 1);
			rst.put("msg", "参数错误");
			renderJson(rst);
		}
	}
	
	@Before(APISignInterCeptor.class)
	public void APIAdd(){
		Map<String,Object> rst = new HashMap<String,Object>();
		String opt_type = this.getPara("opt_type");
		String opt_info = this.getPara("opt_info");
		String uuid = this.getPara("uuid");
		
		log.info("uuid|"+uuid+"|opt_info|"+opt_info);
		if(opt_type!=null && !opt_type.isEmpty()
				&& opt_info!=null && !opt_info.isEmpty()){
			//发送消息
			if(opt_type.equals("message")){
				sendMessage(opt_info);
			}
			//分配管家
			else if(opt_type.equals("assigned_steward")){
				assignedSteward(opt_info);
			}
			//客户信息
			else if(opt_type.equals("customer")){
				addCustomer(opt_info);
			}
			//活动
			else if(opt_type.equals("activity")){
				addActivity(opt_info);
			}
			else{
				rst.put("status", 9);
				rst.put("msg", "无操作对象");
				renderJson(rst);
			}
		}
		else{
			rst.put("status", 1);
			rst.put("msg", "参数错误");
			renderJson(rst);
		}
	}
	
	@Before(APISignInterCeptor.class)
	public void APIUsers(){
		Map<String,Object> rst = new HashMap<String,Object>();
		ParamsParser pp = new ParamsParser(this);
		
		String id = pp.getId();
		if(id!=null){
			User user = User.dao.findById(id);
			if(user!=null){
				rst.put("status", 0);
				rst.put("msg", "ok");
				rst.put("user", user);
				this.renderJson(rst);
			}
			else{
				rst.put("status", 100);
				rst.put("msg", "err");
				this.renderJson(rst);
			}
		}
		else{
			List<Record> users = Db.find("select * from console_user as cu left join console_orgnization as co on cu.dep_id=co.org_id where cu.delete_flag='0' order by cu.create_time desc");
			List<HashMap<String, Object>> ulist = new ArrayList<HashMap<String,Object> >();
			for(Record r:users){
				HashMap<String, Object> u = new HashMap<String, Object>();
				u.put("user_id", r.getInt("user_id"));
				if(r.getStr("gender").equals("MALE"))
					u.put("gender", "男");// sex
				else
					u.put("gender", "女");// sex
				u.put("user_name", r.getStr("user_name"));
				u.put("english_name", r.getStr("english_name"));
				u.put("job_number", r.getStr("job_number"));
				u.put("login_name", r.getStr("login_name"));
				u.put("phone", r.getStr("phone"));
				u.put("seat_number", r.getStr("seat_number"));
				u.put("email", r.getStr("email"));
				u.put("qq", r.getStr("qq"));
				u.put("dep_code", r.getStr("org_code"));
				u.put("master_post", r.getStr("master_post"));
				u.put("post_level", r.getStr("post_level"));
				u.put("u8_code", r.getStr("u8_code"));
				
				ulist.add(u);
			}
			
			rst.put("status", 0);
			rst.put("msg", "ok");
			rst.put("users", ulist);
			this.renderJson(rst);
		}
	}
	
	public void APISign(){
		Map<String,Object> rst = new HashMap<String,Object>();
		String username = this.getPara("username");
		String password = this.getPara("password");
		String expired = this.getPara("expired");
		long defaultExpired = 5;
		
		if(username!=null && password!=null && StringUtil.validateSearchKey(username)){
			User user = User.dao.findFirst("select * from console_user where delete_flag=0 and frozen=0 and login_name='"+username+"'");
			if(user!=null && user.getPassword().equals(MD5Util.MD5(password))){
				if(expired!=null && StringUtil.isNumeric(expired)){
					if(Long.valueOf(expired)>0 && Long.valueOf(expired)<=30){
						defaultExpired = Long.valueOf(expired);
					}
				}
				
				String sign = AccessToken.getToken(username, PropKit.get("secret_key"), defaultExpired);
				
				rst.put("status", 0);
				rst.put("msg", "ok");
				rst.put("sign", sign);
				this.renderJson(rst);
			}
			else{
				rst.put("status", 2);
				rst.put("msg", "验证失败");
				this.renderJson(rst);
			}
		}
		else{
			rst.put("status", 1);
			rst.put("msg", "参数错误");
			this.renderJson(rst);
		}
	}
	
	public void syslist(){
		List<Websys> list = Websys.dao.find("select * from console_websys where delete_flag='0' order by sys_id asc");
		
		if(list.size()>0){
			StringBuffer bf = new StringBuffer();
			bf.append("{");
			bf.append("\"status\":0,\"msg\":\"ok\",\"platforms\":[");
			boolean first=true;
			for(Websys ws:list){
				if(first){
					first=false;
					bf.append("{\"name\":\""+ws.getName()+"\",\"url\":\""+ws.getDomain()+"\",\"tokenLoginUrl\":\""+ws.getOathurl()+"\"}");
				}
				else{
					bf.append(",").append("{\"name\":\""+ws.getName()+"\",\"url\":\""+ws.getDomain()+"\",\"tokenLoginUrl\":\""+ws.getOathurl()+"\"}");
				}
			}
			bf.append("]}");
			this.renderJson(bf.toString());
		}
		else{
			this.renderJson("{status:5,msg:error}");
		}
	}
	
//	private void addOrg(Org prt,Org org){
//		Org prtOrg = findParentOrg(prt,org);
//		if(prtOrg!=null){
//			prtOrg.getOrgList().add(org);
//		}
//	}
	
	/**
	 * 找到当前节点的父节点
	 * @param node
	 * @return
	 */
//	private Org findParentOrg(Org prt,Org org){
//		Org prtNode=prt;
//		ArrayList<String> tips = org.getCodeTips();
//		if(tips.size()>1){
//			for(int i=0;i<tips.size()-1;i++){
//				if(prtNode!=null)
//					prtNode=prtNode.getSubOrg(tips.get(i));	
//			}
//		}
//		
//		return prtNode;
//	}
	
	private class Org{
		private int orgId=0;
		private String orgCode="";
//		private String orgPcode="";
		private String orgType="ORG_TYPE_NONE";
		private String name="";
//		private String shortName="";
//		private String description="";
		private String createTime="";
		private String u8Code="";
//		private int sort=0;
		//private List<Org> orgList = new ArrayList<Org>();
		
//		public List<Org> getOrgList() {
//			return orgList;
//		}
//		public void setOrgList(List<Org> orgList) {
//			this.orgList = orgList;
//		}
		public int getOrgId() {
			return orgId;
		}
		public void setOrgId(int orgId) {
			this.orgId = orgId;
		}
		public String getOrgCode() {
			return orgCode;
		}
		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
		}
//		public String getOrgPcode() {
//			return StringUtil.getParentCode(orgCode);
//		}
//		public void setOrgPcode(String orgPcode) {
//			this.orgPcode = orgPcode;
//		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
//		public String getShortName() {
//			return shortName;
//		}
//		public void setShortName(String shortName) {
//			this.shortName = shortName;
//		}
//		public String getDescription() {
//			return description;
//		}
//		public void setDescription(String description) {
//			this.description = description;
//		}
		
//		public int getSort() {
//			return sort;
//		}
//		public void setSort(int sort) {
//			this.sort = sort;
//		}
		public String getOrgType() {
			return orgType;
		}
		public void setOrgType(String orgType) {
			this.orgType = orgType;
		}
		
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
//		public Org getSubOrg(String code){
//			for(Org o:orgList){
//				if(o.getOrgCode().equals(code))
//					return o;
//			}
//			return null;
//		}
		
		public ArrayList<String> getCodeTips(){
			ArrayList<String> tips = new ArrayList<String>();
			String tip="";
			for(int i=0;i<orgCode.length();i++){
				if(i>0 && (i+1)%4==0){
					tip += orgCode.charAt(i);
					tips.add(tip);
				}
				else
					tip += orgCode.charAt(i);
			}
			
			return tips;
		}
		
		public String toJson(){
			StringBuffer json = new StringBuffer();
			json.append("{");
			json.append("\"").append("orgId").append("\":\"").append(orgId).append("\",");
			json.append("\"").append("orgCode").append("\":\"").append(orgCode).append("\",");
//			json.append("\"").append("orgPcode").append("\":\"").append(orgPcode).append("\",");
			json.append("\"").append("orgType").append("\":\"").append(orgType).append("\",");
			json.append("\"").append("name").append("\":\"").append(name).append("\",");
//			json.append("\"").append("shortName").append("\":\"").append(shortName).append("\",");
//			json.append("\"").append("description").append("\":\"").append(description).append("\",");
			json.append("\"").append("createTime").append("\":\"").append(createTime).append("\"");
//			json.append("\"").append("sort").append("\":\"").append(sort).append("\",");
//			json.append("\"").append("orgList").append("\",[");
//			
//			boolean flag=true;
//			for(Org org:orgList){
//				if(flag){
//					flag=false;
//					json.append(org.toJson());
//				}
//				else{
//					json.append(",").append(org.toJson());
//				}
//			}
			
//			json.append("]");
			json.append("}");
			
			return json.toString();
		}
	}
	
	private void updateEmployee(String opt_info){
		Map<String,Object> rst = new HashMap<String,Object>();
		try{	
			JSONObject infoObj = new JSONObject(opt_info);
			if(!infoObj.isNull("user_id") && StringUtil.isNumeric(String.valueOf(infoObj.getInt("user_id")))){
				User user = User.dao.findById(String.valueOf(infoObj.getInt("user_id")));
				if(user!=null){
					if(!infoObj.isNull("dep_code")){
						String orgCode = infoObj.getString("dep_code");
						Orgnization org = Orgnization.dao.findFirst("select * from console_orgnization where org_code='"+orgCode+"'");
						user.setDepId(org.getOrgId());
					}
					if(!infoObj.isNull("frozen"))
						user.setFrozen(infoObj.getInt("frozen"));
					if(!infoObj.isNull("delete_flag"))
						user.setDeleteFlag(infoObj.getInt("delete_flag"));
					if(user.update()){
						rst.put("status", 0);
						rst.put("msg", "ok");
						renderJson(rst);
					}
					else{
						rst.put("status", 100);
						rst.put("msg", "操作失败");
						renderJson(rst);
					}
				}
				else{
					rst.put("status", 52);
					rst.put("msg", "当前对象不存在");
					renderJson(rst);
				}
			}
			else{
				rst.put("status", 51);
				rst.put("msg", "无对象主键");
				renderJson(rst);
			}
		}
		catch(Exception e){
			rst.put("status", 7);
			rst.put("msg", "json参数解析错误！");
			renderJson(rst);
		}
	}
	
	private void sendMessage(String opt_info){
		Map<String,Object> rst = new HashMap<String,Object>();
		try{	
			JSONObject infoObj = new JSONObject(opt_info);
			String target = infoObj.getString("target");
			String s_ids = infoObj.getString("s_ids");
			String title = infoObj.getString("title");
			String content = infoObj.getString("content");
			
			if(target==null || target.isEmpty()
					||s_ids==null || s_ids.isEmpty()
					||title==null || title.isEmpty()
					||content==null || content.isEmpty()){
				rst.put("status", 8);
				rst.put("msg", "必填数据不能为空！");
				renderJson(rst);
				return;
			}
			String[] cids = s_ids.split(",");
			boolean flag=true;
			for(int i=0;i<cids.length;i++){
				if(flag){
					s_ids = "'"+cids[i].replace("'", "")+"'";
					flag=false;
				}
				else{
					s_ids += ",";
					s_ids += "'"+cids[i].replace("'", "")+"'";
				}
			}
			
			if(target.equals("employee")){
				String sql="select user_id from console_user where crm_id in ("+s_ids+")";
				List<Record> ids = Db.find(sql);
				for(Record rc:ids){
					int id = rc.getInt("user_id");
					Message message = new Message();
					message.setMsgTarget(0);
					message.setTargetId(id);
					message.setMsgTitle(title);
					message.setMsgContent(content);
					message.save();
				}
			}
			else{
				String sql="select custinfo_id from crm_custinfo where crm_id in ("+s_ids+")";
				List<Record> ids = Db.find(sql);
				for(Record rc:ids){
					int id = rc.getInt("custinfo_id");
					Message message = new Message();
					message.setMsgTarget(1);
					message.setTargetId(id);
					message.setMsgTitle(title);
					message.setMsgContent(content);
					message.save();
				}
			}
			
			rst.put("status", 0);
			rst.put("msg", "ok");
			renderJson(rst);
		}
		catch(Exception e){
			rst.put("status", 7);
			rst.put("msg", "json参数解析错误！");
			renderJson(rst);
		}
	}

	private void assignedSteward(String opt_info){
		Map<String,Object> rst = new HashMap<String,Object>();
		try{	
			JSONObject infoObj = new JSONObject(opt_info);
			String s_userid = infoObj.getString("s_userid");
			String s_custid =  infoObj.getString("s_custid");
			String steward_type = infoObj.getString("steward_type");
			
			/**
			 * 转换id
			 */
			StewardUser stewardUser = StewardUser.dao.findFirst("select * from crm_steward_user as csu,console_user where crm_id='"+s_userid+"'");
			if(stewardUser==null){
				rst.put("status", 10);
				rst.put("msg", "无对应员工");
				renderJson(rst);
				return;
			}
			
			int stewardId = stewardUser.getStewardId();
			
			Custinfo custinfo = Custinfo.dao.findFirst("select * from crm_custinfo where crm_id='"+s_custid+"'");
			if(custinfo==null){
				rst.put("status", 11);
				rst.put("msg", "无对应客户");
				renderJson(rst);
				return;
			}
			int custId = custinfo.getCustinfoId();
			
			if(steward_type.equals("顾问")){
				StewardSign ss = StewardSign.dao.findFirst("select * from crm_steward_sign where custinfo_id="+custId+" order by update_time desc");
				if(ss!=null){
					String status = ss.getSignStatus();
					if(status.equals("聘用")){
						StewardSign ss1 = new StewardSign();
						ss1.setStewardId(stewardId);
						ss1.setCustinfoId(custId);
						ss1.setSignStatus("解聘");
						ss1.setUnsignTime(ss1.getSignTime());
						ss1.save();
					}
					
					ss = new StewardSign();
					ss.setStewardId(stewardId);
					ss.setCustinfoId(custId);
					ss.setSignStatus("聘用");
					ss.save();
					
					rst.put("status", 0);
					rst.put("msg", "ok");
					renderJson(rst);
				}
				else{
					ss =  new StewardSign();
					ss.setStewardId(stewardId);
					ss.setCustinfoId(custId);
					ss.setSignStatus("聘用");
					ss.save();
					
					rst.put("status", 0);
					rst.put("msg", "ok");
					renderJson(rst);
				}
			}
			else{
				rst.put("status", 11);
				rst.put("msg", "无法识别的管家类型");
				renderJson(rst);
			}
		}
		catch(Exception e){
			rst.put("status", 7);
			rst.put("msg", "json参数解析错误！");
			renderJson(rst);
		}
	}
	
	private void changeSteward(String opt_info){
		Map<String,Object> rst = new HashMap<String,Object>();
		try{	
			JSONObject infoObj = new JSONObject(opt_info);
			String s_userid = infoObj.getString("s_userid");
			String s_custid =  infoObj.getString("s_custid");
			String steward_type = infoObj.getString("steward_type");
			
			/**
			 * 转换id
			 */
			StewardUser stewardUser = StewardUser.dao.findFirst("select * from crm_steward_user as csu,console_user where crm_id='"+s_userid+"'");
			if(stewardUser==null){
				rst.put("status", 10);
				rst.put("msg", "无对应员工");
				renderJson(rst);
				return;
			}
			
			int stewardId = stewardUser.getStewardId();
			
			Custinfo custinfo = Custinfo.dao.findFirst("select * from crm_custinfo where crm_id='"+s_custid+"'");
			if(custinfo==null){
				rst.put("status", 11);
				rst.put("msg", "无对应客户");
				renderJson(rst);
				return;
			}
			int custId = custinfo.getCustinfoId();
			
			if(steward_type.equals("顾问")){
				StewardSign ss = StewardSign.dao.findFirst("select * from crm_steward_sign where custinfo_id="+custId+" order by update_time desc");
				if(ss!=null){
					String status = ss.getSignStatus();
					if(status.equals("聘用")){
						StewardSign ss1 = new StewardSign();
						ss1.setStewardId(stewardId);
						ss1.setCustinfoId(custId);
						ss1.setSignStatus("解聘");
						ss1.setUnsignTime(ss1.getSignTime());
						ss1.save();
					}
					
					ss = new StewardSign();
					ss.setStewardId(stewardId);
					ss.setCustinfoId(custId);
					ss.setSignStatus("聘用");
					ss.save();
					
					rst.put("status", 0);
					rst.put("msg", "ok");
					renderJson(rst);
				}
				else{
					ss =  new StewardSign();
					ss.setStewardId(stewardId);
					ss.setCustinfoId(custId);
					ss.setSignStatus("聘用");
					ss.save();
					
					rst.put("status", 0);
					rst.put("msg", "ok");
					renderJson(rst);
				}
			}
			else{
				rst.put("status", 11);
				rst.put("msg", "无法识别的管家类型");
				renderJson(rst);
			}
		}
		catch(Exception e){
			rst.put("status", 7);
			rst.put("msg", "json参数解析错误！");
			renderJson(rst);
		}
	}
	
	private void addCustomer(String opt_info){
		Map<String,Object> rst = new HashMap<String,Object>();
		try{	
			JSONObject infoObj = new JSONObject(opt_info);
			Custinfo custinfo = new Custinfo();

			String mobile=infoObj.getString("mobile");
			if(mobile==null){
				rst.put("status", 14);
				rst.put("msg", "客户手机不能为空！");
				renderJson(rst);
				return;
			}
		
			String s_custid="";
			if(!infoObj.has("s_custid")){
				rst.put("status", 13);
				rst.put("msg", "客户id不能为空！");
				renderJson(rst);
				return;
			}
			s_custid = infoObj.getString("s_custid");
			
			Custinfo custinfo1 = Custinfo.dao.findFirst("select * from crm_custinfo where phone_num='"+mobile+"'");
			if(custinfo1==null){
				custinfo.setPhoneNum(mobile);
				custinfo.setCrmId(s_custid);
				
				String nick_name = infoObj.getString("nick_name");
				if(nick_name!=null && !nick_name.isEmpty())
					custinfo.setWxName(nick_name);
				
				String cust_name=infoObj.getString("cust_name");
				if(cust_name!=null && !cust_name.isEmpty()){
					custinfo.setName(cust_name);
				}
				
				String gender = infoObj.getString("gender");
				if(gender!=null && !gender.isEmpty()){
					custinfo.setGender(gender);
				}
				
				String cust_status=infoObj.getString("cust_status");
				if(cust_status!=null && !cust_status.isEmpty()){
					custinfo.setCustStatus(cust_status);
				}
				
				String wechat=infoObj.getString("wechat");
				if(wechat!=null && !wechat.isEmpty()){
					custinfo.setWx(wechat);
				}
				
				String email=infoObj.getString("email");
				if(email!=null && !email.isEmpty()){
					custinfo.setEmail(email);
				}
				
				String address=infoObj.getString("address");
				if(address!=null && !address.isEmpty()){
					custinfo.setAddress(address);
				}
				
				String cust_referrer=infoObj.getString("cust_referrer");
				if(cust_referrer!=null && !cust_referrer.isEmpty()){
					custinfo.setReferrer(cust_referrer);
				}
				
				long create_time = infoObj.getLong("create_time");
				if(create_time>0)
					custinfo.setCreateTime(new Date(create_time));
				
				String mark = infoObj.getString("mark");
				if(mark!=null && !mark.isEmpty()){
					custinfo.setRemark(mark);
				}
				
				custinfo.save();
			}
			else{
				custinfo=custinfo1;
				
				custinfo.setCrmId(s_custid);
				String nick_name = infoObj.getString("nick_name");
				if(nick_name!=null && !nick_name.isEmpty())
					custinfo.setWxName(nick_name);
				
				String cust_name=infoObj.getString("cust_name");
				if(cust_name!=null && !cust_name.isEmpty()){
					custinfo.setName(cust_name);
				}
				
				String gender = infoObj.getString("gender");
				if(gender!=null && !gender.isEmpty()){
					custinfo.setGender(gender);
				}
				
				String cust_status=infoObj.getString("cust_status");
				if(cust_status!=null && !cust_status.isEmpty()){
					custinfo.setCustStatus(cust_status);
				}
				
				String wechat=infoObj.getString("wechat");
				if(wechat!=null && !wechat.isEmpty()){
					custinfo.setWx(wechat);
				}
				
				String email=infoObj.getString("email");
				if(email!=null && !email.isEmpty()){
					custinfo.setEmail(email);
				}
				
				String address=infoObj.getString("address");
				if(address!=null && !address.isEmpty()){
					custinfo.setAddress(address);
				}
				
				String cust_referrer=infoObj.getString("cust_referrer");
				if(cust_referrer!=null && !cust_referrer.isEmpty()){
					custinfo.setReferrer(cust_referrer);
				}
				
				long create_time = infoObj.getLong("create_time");
				if(create_time>0)
					custinfo.setCreateTime(new Date(create_time));
				
				String mark = infoObj.getString("mark");
				if(mark!=null && !mark.isEmpty()){
					custinfo.setRemark(mark);
				}
				
				custinfo.update();
			}
			
			rst.put("status", 0);
			rst.put("msg", "ok");
			renderJson(rst);
		}
		catch(Exception e){
			rst.put("status", 7);
			rst.put("msg", "json参数解析错误！");
			renderJson(rst);
		}	
	}
	
	private void updateCustomer(String opt_info){
		Map<String,Object> rst = new HashMap<String,Object>();
		try{	
			JSONObject infoObj = new JSONObject(opt_info);
			Custinfo custinfo;
			if(infoObj.has("s_custid")){
				String s_custid = infoObj.getString("s_custid");
				String sql="select * from crm_custinfo where crm_id='"+s_custid+"'";
				custinfo = Custinfo.dao.findFirst(sql);
			}
			else{
				rst.put("status", 13);
				rst.put("msg", "客户id不能为空！");
				renderJson(rst);
				return;
			}
			
			String nick_name = infoObj.getString("nick_name");
			if(nick_name!=null && !nick_name.isEmpty())
				custinfo.setWxName(nick_name);
			
			String cust_name=infoObj.getString("cust_name");
			if(cust_name!=null && !cust_name.isEmpty()){
				custinfo.setName(cust_name);
			}
			
			String gender = infoObj.getString("gender");
			if(gender!=null && !gender.isEmpty()){
				custinfo.setGender(gender);
			}
			
//			String mobile=infoObj.getString("mobile");
//			if(mobile!=null){
//				custinfo.setPhoneNum(mobile);
//			}
//			else{
//				rst.put("status", 14);
//				rst.put("msg", "客户手机不能为空！");
//				renderJson(rst);
//				return;
//			}
			
			String cust_status=infoObj.getString("cust_status");
			if(cust_status!=null && !cust_status.isEmpty()){
				custinfo.setCustStatus(cust_status);
			}
			
			String wechat=infoObj.getString("wechat");
			if(wechat!=null && !wechat.isEmpty()){
				custinfo.setWx(wechat);
			}
			
			String email=infoObj.getString("email");
			if(email!=null && !email.isEmpty()){
				custinfo.setEmail(email);
			}
			
			String address=infoObj.getString("address");
			if(address!=null && !address.isEmpty()){
				custinfo.setAddress(address);
			}
			
			String cust_referrer=infoObj.getString("cust_referrer");
			if(cust_referrer!=null && !cust_referrer.isEmpty()){
				custinfo.setReferrer(cust_referrer);
			}
			
			long create_time = infoObj.getLong("create_time");
			if(create_time>0)
				custinfo.setCreateTime(new Date(create_time));
			
			String mark = infoObj.getString("mark");
			if(mark!=null && !mark.isEmpty()){
				custinfo.setRemark(mark);
			}
			
			custinfo.update();
			
			rst.put("status", 0);
			rst.put("msg", "ok");
			renderJson(rst);
		}
		catch(Exception e){
			rst.put("status", 7);
			rst.put("msg", "json参数解析错误！");
			renderJson(rst);
		}	
	}
	
	private void addActivity(String opt_info){
		Map<String,Object> rst = new HashMap<String,Object>();
		try{	
			Activity activity = new Activity();
			JSONObject infoObj = new JSONObject(opt_info);
			if(infoObj.has("s_activityid"))
				activity.setActivityId(infoObj.getInt("s_activityid"));
			else{
				rst.put("status", 15);
				rst.put("msg", "活动id不能为空！");
				renderJson(rst);
				return;
			}
				
			String activity_name=infoObj.getString("activity_name");
			if(activity_name!=null && !activity_name.isEmpty()){
				activity.setActivityName(activity_name);
			}
			else{
				rst.put("status", 16);
				rst.put("msg", "活动名称不能为空！");
				renderJson(rst);
				return;
			}
				
			String activity_type=infoObj.getString("activity_type");
			if(activity_type!=null && !activity_type.isEmpty()){
				activity.setActivityType(activity_type);
			}
			
			String business_segments=infoObj.getString("business_segments");
			if(business_segments!=null && !business_segments.isEmpty()){
				activity.setBusinessSegments(business_segments);
			}
			
			String activity_status=infoObj.getString("activity_status");
			if(activity_status!=null && !activity_status.isEmpty()){
				activity.setActivityStatus(activity_status);
			}
			
			if(infoObj.has("start_time")){
				long start_time=infoObj.getLong("start_time");
				if(start_time>0)
					activity.setStartTime(new Date(start_time));
			}
			else{
				rst.put("status", 17);
				rst.put("msg", "活动开始时间不能为空！");
				renderJson(rst);
				return;
			}
			
			if(infoObj.has("end_time")){
				long end_time=infoObj.getLong("end_time");
				if(end_time>0)
					activity.setEndTime(new Date(end_time));
			}
			
			String activity_address=infoObj.getString("activity_address");
			if(activity_address!=null && !activity_address.isEmpty()){
				activity.setActivityAddress(activity_address);
			}
			
			if(infoObj.has("publisher")){
				String publisher= infoObj.getString("publisher");
				activity.setPublisher(publisher);
			}
			
			if(infoObj.has("activity_principal")){
				String activity_principal=infoObj.getString("activity_principal");
				activity.setActivityPrincipal(activity_principal);
			}
			
			String activity_description=infoObj.getString("activity_description");
			if(activity_description!=null && !activity_description.isEmpty()){
				activity.setActivityMark(activity_description);
			}
			
			String company=infoObj.getString("company");
			if(company!=null && !company.isEmpty()){
				activity.setCompany(company);
			}
			
			String department=infoObj.getString("department");
			if(department!=null && !department.isEmpty()){
				activity.setDepartment(department);
			}
			
			String collaborators=infoObj.getString("collaborators");
			if(collaborators!=null && !collaborators.isEmpty()){
				activity.setCollaborators(collaborators);
			}
			
			activity.save();
			
			rst.put("status", 0);
			rst.put("msg", "ok");
			renderJson(rst);
		}
		catch(Exception e){
			rst.put("status", 7);
			rst.put("msg", "json参数解析错误！");
			renderJson(rst);
		}
	}
}
