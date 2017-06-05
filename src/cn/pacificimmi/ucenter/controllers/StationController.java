package cn.pacificimmi.ucenter.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.ContentType;

import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Role;
import cn.pacificimmi.common.models.RoleResource;
import cn.pacificimmi.common.models.Station;
import cn.pacificimmi.common.models.User;
import cn.pacificimmi.common.models.Websys;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.ucenter.models.LoginUserInfo;
import cn.pacificimmi.ucenter.models.view.RoleInfo;
import cn.pacificimmi.ucenter.models.view.StationInfo;
import cn.pacificimmi.common.utils.MajorKeyFactory;
import cn.pacificimmi.common.utils.StringUtil;

@Before(LoginInterCeptor.class) 
public class StationController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(StationController.class);
	public void index(){
		this.renderText("路径错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}
	
	public void checkExist(){
		ParamsParser pp = new ParamsParser(this);
		String name = pp.getNormStr("name");
		if(name!=null){
			Station station = Station.dao.findFirst("select * from console_station where name='"+name+"' and delete_flag='0'");
			if(station!=null){
				this.renderJson("{\"status\":500,\"msg\":\"exist item\"}");
			}
			else
				this.renderJson("{\"status\":0,\"msg\":\"ok\"}");
		}
		else
			this.renderJson("{\"status\":1,\"msg\":\"param error\"}");
	}
	
	/**
	 * 增加岗位
	 */
	public void add(){
		/**
		 * 创建参数解析类
		 */
		ParamsParser pp = new ParamsParser(this);
		String uuid = UUID.randomUUID().toString();
	
		if(pp.getNormStr("operation")!=null){
			StringBuffer desc = new StringBuffer();
			//保存数据操作
			Station ms = new Station();
			//ms.set("station_id", MajorKeyFactory.getInstance().getMajorKey());
			
			//岗位名称
			String name = pp.getNormStr("name");
			if(name!=null){
				ms.set("name", name);
				desc.append("岗位名称:"+name);
			}
			//岗位描述
			String description = pp.getNormStr("description");
			if(description!=null){
				ms.set("description", description);
				desc.append("岗位描述:"+description).append("/");
			}
			
			//所属部门
			String org_id =  pp.getNormStr("org_id");
			if(org_id!=null){
				ms.set("com_id", org_id);
				desc.append("部门id:"+org_id);
			}
			
			//保存操作人信息
			LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
			if(lui!=null){
				ms.set("create_user",lui.getUserName());
				ms.set("update_user",lui.getUserName());
			}
			ms.set("update_time",StringUtil.yyyymmddhmsTime(new Date()));
			
			//有效数据
			ms.set("delete_flag", "0");
			//保存数据到数据库
			ms.save();
			
			OperateLogger.log(this, "岗位管理", "新增", desc.toString());
			logger.info("|add|uuid:"+uuid+"|"+ms.toJson());
			/***
			 * 跳转到跳出时列表页地址
			 */
			String fromurl = getPara("fromurl");
			this.redirect(fromurl);
		}
		else{
			String tree = loadTree("");
			this.setAttr("tree", tree);
			
			this.setAttr("operation", "新增");
			this.renderJsp("/views/station.jsp");
		}
	}
	
	//更新数据
	public void update(){
		ParamsParser pp = new ParamsParser(this);
		String uuid = UUID.randomUUID().toString();
		
		String id = pp.getId();
		if(id!=null && !id.isEmpty()){
			String operation = pp.getNormStr("operation");
			if(operation!=null){
				StringBuffer desc = new StringBuffer();
				//编辑处理
				Station ms = Station.dao.findById(id);
				
				//岗位名称
				String name = pp.getNormStr("name");
				if(name!=null){
					ms.set("name", name);
					desc.append("岗位名称：").append(name).append("/");
				}
				//岗位描述
				String description = pp.getNormStr("description");
				if(description!=null){
					ms.set("description", description);
					desc.append("描述：").append(description).append("/");
				}
				
				//所属部门
				String org_id =  pp.getNormStr("org_id");
				if(org_id!=null){
					ms.set("com_id", org_id);
					desc.append("部门id："+org_id);
				}
				
				//保存操作人信息
				LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
				if(lui!=null){
					ms.set("update_user",lui.getUserName());
				}
				ms.set("update_time",StringUtil.yyyymmddhmsTime(new Date()));
				
				ms.update();
				OperateLogger.log(this, "岗位管理", "修改", "修改岗位信息(id:"+id+")为："+desc.toString()+"|uuid:"+uuid);
				logger.info("|update|uuid:"+uuid+"|"+ms.toJson());
				/***
				 * 跳转到跳出时列表页地址
				 */
				String fromurl = getPara("fromurl");
				this.redirect(fromurl);
			}
			else{
				String sql="select stations.name as station_name,"
						+ "stations.station_id as station_id,"
						+ "stations.description as station_description,"
						+ "org.name as orgnization_name,"
						+ "stations.create_time as station_create_time,"
						+ "org.org_code as orgnization_code,"
						+ "stations.com_id as orgnization_id"
						+ " from console_station as stations"
						+ " left join console_orgnization as org"
						+ " on stations.com_id=org.org_id"
						+ " where stations.delete_flag='0' and stations.station_id='"+id+"'";
				Record rd = Db.findFirst(sql);
				StationInfo si = new StationInfo();
				if(rd!=null){
					si.bindingData(si, rd);
					this.setAttr("station", si);
					this.setAttr("operation", "编辑");
					
					String selectCode = si.getOrgnization_code();
					String tree = loadTree(selectCode);
					this.setAttr("tree", tree);
					
					this.renderJsp("/views/station.jsp");
				}
				else{
					this.renderText("无此数据!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
				}
			}
		}
		else
			this.renderText("参数错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}
	
	//分配角色
	public void roles(){
		ParamsParser pp = new ParamsParser(this);
		String uuid = UUID.randomUUID().toString();
		String id = pp.getId();
		if(id!=null && !id.isEmpty()){
			String[] roles = pp.getIntegers("target");
			if(pp.getAllStr("fromurl")!=null){
				StringBuffer desc = new StringBuffer();
				Station s = Station.dao.findById(id);
				desc.append("为岗位").append(s.getName()).append("分配");
				Db.update("delete from console_role_station where station_id='"+id+"'");
				if(roles!=null){
					for(String role:roles){
						Role r = Role.dao.findById(role);
						Db.update("insert into console_role_station (station_id,role_id) values('"+id+"','"+role+"')");
						desc.append(r.getName()).append("、");
					}
				}
				
				OperateLogger.log(this, "岗位管理", "角色分配", desc.toString());
				logger.info("|roles|uuid:"+uuid+"|"+roles.toString());
				/***
				 * 跳转到跳出时列表页地址
				 */
				String fromurl = getPara("fromurl");
				this.redirect(fromurl);
			}
			else{
				String sql="select stations.name as station_name,"
						+ "stations.station_id as station_id,"
						+ "stations.description as station_description,"
						+ "org.name as orgnization_name,"
						+ "stations.create_time as station_create_time,"
						+ "org.org_code as orgnization_code,"
						+ "stations.com_id as orgnization_id"
						+ " from console_station as stations"
						+ " left join console_orgnization as org"
						+ " on stations.com_id=org.org_id "
						+ "where stations.delete_flag='0' and stations.station_id='"+id+"'";
				Record rd = Db.findFirst(sql);
				StationInfo si = new StationInfo();
				if(rd!=null){
					si.bindingData(si, rd);
					this.setAttr("station", si);
					
					//待选列表
					Hashtable<String,ArrayList<option> > options = new Hashtable<String,ArrayList<option> >();
					List<Record> list = Db.find("select m_r.*,m_w.name as sys_name from console_role as m_r,console_websys as m_w where m_r.sys_id=m_w.sys_id and  m_r.delete_flag='0' and m_w.delete_flag='0' and m_r.role_id not in (select role_id from console_role_station where station_id='"+id+"') order by m_r.sys_id asc");
					for(Record rc:list){
						String sys_name = rc.getStr("sys_name");
						String role_id = String.valueOf(rc.getInt("role_id"));
						String name = rc.getStr("name");
						option opt = new option();
						opt.setOptName(name);
						opt.setOptValue(role_id);
						if(options.containsKey(sys_name)){
							options.get(sys_name).add(opt);
						}
						else{
							ArrayList<option> opts = new ArrayList<option>();
							opts.add(opt);
							options.put(sys_name, opts);
						}
					}
					String optionsShow=getOptions(options);
					this.setAttr("options_from", optionsShow);
					
					//已选列表
					list = Db.find("select m_r.*,m_w.name as sys_name from console_role as m_r,console_websys as m_w where m_r.sys_id=m_w.sys_id and  m_r.delete_flag='0' and m_w.delete_flag='0' and m_r.role_id in (select role_id from console_role_station where station_id='"+id+"') order by m_r.sys_id asc");
					options.clear();
					for(Record rc:list){
						String sys_name = rc.getStr("sys_name");
						String role_id = String.valueOf(rc.getInt("role_id")) ;
						String name = rc.getStr("name");
						option opt = new option();
						opt.setOptName(name);
						opt.setOptValue(role_id);
						
						if(options.containsKey(sys_name)){
							options.get(sys_name).add(opt);
						}
						else{
							ArrayList<option> opts = new ArrayList<option>();
							opts.add(opt);
							options.put(sys_name, opts);
						}
					}
					optionsShow=getOptions(options);
					this.setAttr("options_target", optionsShow);
					
					this.renderJsp("/views/station_role.jsp");
				}
				else{
					this.renderText("无此数据!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
				}
			}
		}
	}
	
	private String getOptions(Hashtable<String,ArrayList<option> > os){
		StringBuffer buf = new StringBuffer();
		Iterator<String> iter =os.keySet().iterator();
		while(iter.hasNext()){
			String key = (String)iter.next();
			buf.append("<optgroup label='").append(key).append("'>");
			ArrayList<option> ops = os.get(key);
			for(option o:ops){
				buf.append("<option value='")
					.append(o.optValue)
					.append("'>")
					.append(o.optName)
					.append("</option>");
			}
			buf.append("</optgroup>");
		}
		return buf.toString();
	}
	private class option{
		private String optName="";
		private String optValue="";
		public String getOptName() {
			return optName;
		}
		public void setOptName(String optName) {
			this.optName = optName;
		}
		public String getOptValue() {
			return optValue;
		}
		public void setOptValue(String optValue) {
			this.optValue = optValue;
		}
		
	}
///////////////////////树状图加载////////////////////////////////
	
	public String loadTree(String code){
		List<Record> records = Db.find("select * from console_orgnization where delete_flag='0' order by org_code asc");
		NodeTree ct = new NodeTree("orgTree","所属部门",10);
		
		/***
		 * 初始化时将选中的节点，描红显示
		 */
		if(!code.isEmpty()){
			ct.getSelectedCodes().add(code);
		}
		/**
		 * 显示选择框
		 */
		ct.setSelectMode(true);
		
		/**
		 * 不显示编辑按钮
		 */
		ct.setAllownEdit(false);
		
		for(Record r:records){
			Node node = new Node();
			node.setName(r.getStr("name"));
			node.setCode(r.getStr("org_code"));
			node.setId(String.valueOf(r.getInt("org_id")));
			
			node.setExtInfo(r.getStr("org_type"));
			ct.addNode(node);
		}
		
		return ct.buildTree();
	}
}
