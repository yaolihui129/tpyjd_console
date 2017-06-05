package cn.pacificimmi.ucenter.controllers;

import java.util.ArrayList;
import java.util.Date;
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
import cn.pacificimmi.common.models.Resource;
import cn.pacificimmi.common.models.Role;
import cn.pacificimmi.common.models.Websys;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.ucenter.models.LoginUserInfo;
import cn.pacificimmi.ucenter.models.view.RoleInfo;
import cn.pacificimmi.common.utils.StringUtil;

@Before(LoginInterCeptor.class) 
public class RoleController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(RoleController.class);
	public void index(){
		this.renderText("路径错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}

	public void checkExist(){
		ParamsParser pp = new ParamsParser(this);
		String name = pp.getNormStr("name");
		if(name!=null){
			Role role = Role.dao.findFirst("select * from console_role where name='"+name+"' and delete_flag='0'");
			if(role!=null){
				this.renderJson("{\"status\":500,\"msg\":\"exist item\"}");
			}
			else
				this.renderJson("{\"status\":0,\"msg\":\"ok\"}");
		}
		else
			this.renderJson("{\"status\":1,\"msg\":\"param error\"}");
	}
	
	public void add(){
		/**
		 * 创建参数解析类
		 */
		ParamsParser pp = new ParamsParser(this);
		String uuid = UUID.randomUUID().toString();
		if(pp.getNormStr("operation")!=null){
			/***
			 * 通过数据模型，保存数据
			 */
			Role  mw = new Role();
			//mw.set("role_id", MajorKeyFactory.getInstance().getMajorKey());
			//角色名称
			String name = pp.getNormStr("name");
			if(name!=null)
				mw.set("name", name);//mw.setName(name);
			//所属平台
			String sys_id = pp.getNormStr("sys_id");
			if(sys_id!=null && !sys_id.equals("0"))
				mw.set("sys_id", sys_id);//mw.setDomain(domain);
			//角色描述
			String description = pp.getNormStr("description");
			if(description!=null){
				mw.set("description", description);
			}
			
			//保存操作人信息
			LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
			if(lui!=null){
				mw.set("create_user",lui.getUserName());
				mw.set("update_user",lui.getUserName());
			}
			mw.set("update_time",StringUtil.yyyymmddhmsTime(new Date()));
			
			//是否有效
			mw.set("delete_flag", "0").save();
			OperateLogger.log(this, "角色管理", "新增", "增加角色："+name+"|uuid:"+uuid);
			logger.info("|add|uuid:"+uuid+"|"+mw.toJson());
			
			/***
			 * 跳转到跳出时列表页地址
			 */
			String fromurl = getPara("fromurl");
			this.redirect(fromurl);
		}
		else{
			/***
			 * 初始化系统选项列表
			 */
			List<Websys> websyss = Websys.dao.find("select * from console_websys where delete_flag='0'");
			StringBuffer bf = new StringBuffer();
			bf.append("<select class=\"medium m-wrap\" name=\"sys_id\" id=\"sys_id\">");
			//bf.append("<option value=\"0\">全部</option>");
			for(Websys mw:websyss){
				bf.append("<option value=\""+mw.getSysId()+"\">");
				bf.append(mw.getName());
				bf.append("</option>");
			}
			bf.append("</select>");
			this.setAttr("sys_id", bf.toString());
			
			this.setAttr("operation", "新增");
			this.renderJsp("/views/role.jsp");
		}
	}
	
	public void update(){
		/**
		 * 创建参数解析类
		 */
		ParamsParser rp = new ParamsParser(this);
		String uuid = UUID.randomUUID().toString();
		
		String id = rp.getId();
		if(id!=null && !id.isEmpty()){
			String operation = rp.getNormStr("operation");
			if(operation!=null){
					Role mr = Role.dao.findById(id);
					//角色名称
					String name = rp.getNormStr("name");
					if(name!=null)
						mr.set("name", name);//mw.setName(name);
					//所属平台
					//String sys_id = rp.getNormStr("sys_id");
					//if(sys_id!=null && !sys_id.equals("0"))
					//	mr.set("sys_id", sys_id);//mw.setDomain(domain);
					//角色描述
					String description = rp.getNormStr("description");
					if(description!=null){
						mr.set("description", description);
					}
					
					//保存操作人信息
					LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
					if(lui!=null){
						mr.set("update_user",lui.getUserName());
					}
					mr.set("update_time",StringUtil.yyyymmddhmsTime(new Date()));
					
					mr.update();
					OperateLogger.log(this, "角色管理", "修改", "修改ID为:"+id+"资源,uuid:"+uuid);
					logger.info("|update|uuid:"+uuid+"|"+mr.toJson());
					//跳转
				String fromurl = getPara("fromurl");
				this.redirect(fromurl);
			}
			else{
				String sql="select roles.*,websys.name as sys_name from console_role as roles left join console_websys as websys on roles.sys_id=websys.sys_id where roles.delete_flag='0' and websys.delete_flag='0' and role_id='"+id+"'";
				Record rd = Db.findFirst(sql);
				RoleInfo ri = new RoleInfo();
				if(rd!=null){
					ri.bindingData(ri, rd);
					this.setAttr("role", ri);
					this.setAttr("operation", "编辑");
					
					/***
					 * 初始化系统选项列表
					 */
					List<Websys> websyss = Websys.dao.find("select * from console_websys  where delete_flag='0'");
					StringBuffer bf = new StringBuffer();
					bf.append("<select class=\"medium m-wrap\" name=\"sys_id\" id=\"sys_id\" disabled>");
					//bf.append("<option value=\"0\">全部</option>");
					for(Websys mw:websyss){
						String selected = String.valueOf(ri.getRole_id());
						if(mw.getSysId().equals(selected))
							bf.append("<option value=\""+mw.getSysId()+"\" selected>");
						else
							bf.append("<option value=\""+mw.getSysId()+"\">");
						bf.append(mw.getName());
						bf.append("</option>");
					}
					bf.append("</select>");
					this.setAttr("sys_id", bf.toString());
					
					this.renderJsp("/views/role.jsp");
				}
				else{
					this.renderText("无此数据!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
				}
			}
		}
		else
			this.renderText("参数错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}
	
	/**
	 * 资源分配视图
	 */
	public void resource(){
		ParamsParser rp = new ParamsParser(this);
		String uuid = UUID.randomUUID().toString();
		String id = rp.getId();
		if(id!=null && !id.isEmpty()){
			String operation = rp.getNormStr("operation");
			if(operation!=null){
				Role role = Role.dao.findById(id);
				//保存权限数据
				String[] checks = rp.getIntegers("chks");
				Db.update("delete from console_role_resource where role_id='"+id+"'");
				
				StringBuffer desc = new StringBuffer();
				for(String ck:checks){
					Db.update("insert into console_role_resource (role_id,res_id) values('"+id+"','"+ck+"')");
					Resource res = Resource.dao.findById(ck);
					desc.append(res.getName()).append("、");
				}
				
				OperateLogger.log(this, "角色管理", "分配资源", "为角色"+role.getName()+"赋予"+desc.toString()+"资源 uuid|"+uuid);
				logger.info("|resource|uuid:"+uuid+"|"+"id:"+id+"|checks:"+checks.toString());
				//跳转
				String fromurl = getPara("fromurl");
				this.redirect(fromurl);
			}
			else{
				//回显数据
				
				String sql="select roles.*,websys.name as sys_name from console_role as roles left join console_websys as websys on roles.sys_id=websys.sys_id where roles.delete_flag='0' and role_id='"+id+"'";
				Record rd = Db.findFirst(sql);
				RoleInfo ri = new RoleInfo();
				if(rd!=null){
					//加载角色基本信息
					ri.bindingData(ri, rd);
					this.setAttr("role", ri);
					this.setAttr("operation", "权限分配");
					
					//加载资源分配树图
					List<Record> mrrs;
					LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
					//特殊资源只能在admin账户下进行分配
					if(lui.getLoginName().equals("admin"))
						mrrs = Db.find("select b.res_code from console_role_resource as a,console_resource as b  where a.res_id=b.res_id and a.role_id='"+id+"' and b.delete_flag='0'");
					else
						mrrs = Db.find("select b.res_code from console_role_resource as a,console_resource as b  where a.res_id=b.res_id and b.special='0' and a.role_id='"+id+"' and delete_flag='0'");
					ArrayList<String> resources = new ArrayList<String>();
					for(Record mrr:mrrs){
						resources.add(mrr.getStr("res_code"));
					}
					String tree = loadTree(resources,String.valueOf(ri.getSys_id()));
					this.setAttr("tree", tree);
					this.renderJsp("/views/role_resource.jsp");
				}
				else{
					this.renderText("无此数据!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
				}
			}
		}
		else
			this.renderText("参数错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}
	
///////////////////////树状图加载////////////////////////////////
	
	private String loadTree(List<String> codes,String sys_id){
		List<Record> records;
		LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
		//特殊资源只能在admin账户下进行分配
		if(lui.getLoginName().equals("admin"))
			records = Db.find("select * from console_resource where delete_flag='0' and sys_id='"+sys_id+"'  order by res_code asc");
		else
			records = Db.find("select * from console_resource where delete_flag='0' and special='0' and sys_id='"+sys_id+"'  order by res_code asc");
		NodeTree nt = new NodeTree("resTree","权限资源",10);
	
		/***
		* 初始化时将选中的节点，描红显示
		*/
		if(codes!=null)
			nt.setSelectedCodes(codes);
		/**
		* 显示选择框
		*/
		nt.setSelectMode(true);
		
		/**
		* 不显示编辑按钮
		*/
		nt.setAllownEdit(false);
		
		for(Record r:records){
			Node node = new Node();
			node.setName(r.getStr("name"));
			node.setCode(r.getStr("res_code"));
			node.setId(String.valueOf(r.getInt("res_id")));
			
			node.setExtInfo(r.getStr("res_type"));
			nt.addNode(node);
		}
		
		return nt.buildTree();
	}
}
