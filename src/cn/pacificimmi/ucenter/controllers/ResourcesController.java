package cn.pacificimmi.ucenter.controllers;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Resource;
import cn.pacificimmi.common.models.Websys;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.common.utils.MajorKeyFactory;
import cn.pacificimmi.common.utils.StringUtil;

@Before(LoginInterCeptor.class)
public class ResourcesController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(ResourcesController.class);
	public void index(){
		String tree="";
		String code=getPara("code");
		String opt_type=getPara("opt_type");
		
		if(code!=null && !code.isEmpty() && StringUtil.isNumeric(code) && !code.equals("0000")){	
			
			/***
			 * 树图
			 */
			tree = loadTree(code);
			/***
			 * 判断是否为新增或编辑
			 */
			if(opt_type!=null && !opt_type.isEmpty() && opt_type.equals("add")){
				/**
				 * 新增
				 */
				setAttr("opt_title","新增");
				setAttr("opt_type","add");
				setAttr("res_code",code);
				/**
				 * 设定表单上级资源名称
				 */
				Resource mo = Resource.dao.findFirst("select * from console_resource where res_code='"+code+"' and delete_flag='0'");
				String parent = mo.getName();
				String sys_id = String.valueOf(mo.getSysId());
				setAttr("parent",parent);
				
				String res_type="<select class=\"small m-wrap\" name=\"res_type\">"
	               		+"<option value=\"SYS_MENU\" selected=\"true\">功能菜单</option>"
	               		+"<option value=\"SYS_OPERATION\">操作</option>"
	               		+"</select>";
				setAttr("res_type",res_type);
				/**
				 * 系统列表
				 */
				String sysList = getWebsysList(sys_id);
				setAttr("sysList",sysList);
			}
			else{
				/**
				 * 编辑
				 */
				setAttr("opt_title","编辑");
				setAttr("opt_type","edit");
				setAttr("res_code",code);
				/**
				 * 设定表单上级机构名称
				 */
				String parent="权限资源";
				String parentCode = StringUtil.getParentCode(code);
				
				Resource mo;
				if(!parentCode.equals("0000")){
					mo= Resource.dao.findFirst("select * from console_resource where res_code='"+StringUtil.getParentCode(code)+"'  and delete_flag='0'");
					parent = mo.getName();
				}
				setAttr("parent",parent);
				
				/***
				 * 设定表单当前编辑的节点名称
				 */
				mo = Resource.dao.findFirst("select * from console_resource where res_code='"+code+"'  and delete_flag='0'");
				String name = mo.getName();
				String resType = mo.getResType();
				String description = mo.getDescription();
				String sys_id = String.valueOf(mo.getSysId());
				String url = mo.getUrl();
				String sort = String.valueOf(mo.getSort());
				String icon = mo.getIcon();
				
				setAttr("description",description);
				setAttr("url",url);
				setAttr("name",name);
				setAttr("sort",sort);
				setAttr("icon",icon);
						
				String res_type="<select class=\"small m-wrap\" name=\"res_type\">";
					if(resType.equals("SYS_MENU"))
						res_type +="<option value=\"SYS_MENU\" selected=\"true\">功能菜单</option>";
					else
						res_type +="<option value=\"SYS_MENU\">功能菜单</option>";
					
					if(resType.equals("SYS_OPERATION"))
						res_type +="<option value=\"SYS_OPERATION\" selected=\"true\">操作</option>";
					else
						res_type +="<option value=\"SYS_OPERATION\">操作</option>";
					res_type +="</select>";
				setAttr("res_type",res_type);
				/**
				 * 系统列表
				 */
				String sysList = getWebsysList(sys_id);
				setAttr("sysList",sysList);
			}
			
		}else{
			/**
			 * 系统列表
			 */
			String sysList = getWebsysList("");
			
			/**
			 * 初始化树图
			 */
			tree = loadTree();
			/**
			 * 新增
			 */
			setAttr("opt_title","新增");
			setAttr("opt_type","add");
			setAttr("res_code","0000");
			setAttr("parent","组织机构");
			
			String res_type="<select class=\"small m-wrap\" name=\"res_type\">"
               		+"<option value=\"SYS_SYSTEM\" selected=\"true\">系统平台</option>"
               		+"</select>";
			setAttr("res_type",res_type);
			
			setAttr("sysList",sysList);
		}
		setAttr("resTree", tree);
		this.renderJsp("/views/resource.jsp");
	}
	
	/***
	 * 所有平台列表
	 * @param sys_id
	 * @return
	 */
	private String getWebsysList(String sys_id){
		String websysList="<select class=\"small m-wrap\" name=\"sys_id\">";
		String sql="";
		if(sys_id.isEmpty())
			sql="select * from console_websys where delete_flag='0'";
		else
			sql="select * from console_websys where delete_flag='0' and sys_id='"+sys_id+"'";
		
		List<Websys> websyss = Websys.dao.find(sql);
		for(Websys mw:websyss){
			if(sys_id.isEmpty()){
				websysList+="<option value=\""+mw.getSysId()+"\">"+mw.getName()+"</option>";
			}
			else if(sys_id.equals(String.valueOf(mw.getSysId()))){
				websysList+="<option value=\""+mw.getSysId()+"\">"+mw.getName()+"</option>";
			}
		}
		websysList+="</select>";
		return websysList;
	}
	
	public String loadTree(){
		return loadTree("");
	}
	
	public String loadTree(String code){
		List<Record> records = Db.find("select * from console_resource where delete_flag='0' order by res_code asc");
		NodeTree ct = new NodeTree("resTree","权限资源",1);
		/***
		 * 初始化时将选中的节点，描红显示
		 */
		if(!code.isEmpty())
			ct.getSelectedCodes().add(code);
		/**
		 * 显示选择框
		 */
		//ct.setSelectMode(true);
		for(Record r:records){
			Node node = new Node();
			node.setName(r.getStr("name"));
			node.setCode(r.getStr("res_code"));
			node.setId(String.valueOf(r.getInt("res_id")));
			
			String res_type=r.getStr("res_type");
			if(!(res_type.equals("SYS_MENU") || res_type.equals("SYS_SYSTEM")))
				node.setAllownAdd(false);
			
			node.setExtInfo(res_type);
			ct.addNode(node);
		}
		
		return ct.buildTree();
	}
	
	
	/***
	 * 节点处理
	 */
	public void opt(){
		ParamsParser pp = new ParamsParser(this);
		String code = pp.getNormStr("code");
		String opt = pp.getNormStr("opt_type");
		String name = pp.getAllStr("name");
		String type = pp.getNormStr("res_type");
		String sort = pp.getNormStr("sort");
		String icon = pp.getAllStr("icon");
		String description = pp.getAllStr("description");
		String sys_id = pp.getNormStr("sys_id");
		String url = pp.getAllStr("url");
		
		if(type==null)
			type="SYS_OPERATION";
		if(description==null)
			description="";
		if(sort==null)
			sort="0";
		if(icon==null)
			icon="";
		if(code!=null && opt!=null){
			if(opt.equals("add") && name!=null){
				Resource mo = new Resource();
				String ncode =createSubCode(code,"console_resource","res_code");
				if(!ncode.isEmpty())
					mo.set("res_code", ncode)
					.set("name", name)
					.set("res_type", type)
					.set("sys_id", sys_id)
					.set("delete_flag", "0")
					.set("description", description)
					.set("sort", sort)
					.set("url", url)
					.set("icon",icon)
					.set("create_time", new Date(System.currentTimeMillis()))
					.save();
				StringBuffer desc = new StringBuffer();
				desc.append("资源code："+ncode)
				.append(" 名称："+ name)
				.append(" 资源类型："+ type)
				.append(" 系统ID："+ sys_id)
				.append(" 资源描述："+ description)
				.append(" 排序权重："+ sort)
				.append(" url："+ url)
				.append(" 图标："+icon);
				OperateLogger.log(this, "资源管理", "新增", "新增资源:"+mo.getResId()+",信息:"+desc.toString());
			}
			else if(opt.equals("edit") && name!=null){
				Db.update("update console_resource set name='"+name+"',url='"+url+"',res_type='"+type+"',sort='"+sort+"',description='"+description+"',icon='"+icon+"' where res_code='"+code+"' and delete_flag='0'");
				StringBuffer desc = new StringBuffer();
				desc.append("资源code："+code)
				.append(" 名称："+ name)
				.append(" 资源类型："+ type)
				.append(" 系统ID："+ sys_id)
				.append(" 资源描述："+ description)
				.append(" 排序权重："+ sort)
				.append(" url："+ url)
				.append(" 图标："+icon);
				OperateLogger.log(this, "资源管理", "修改", "修改code为:"+code+"资源,信息:"+desc.toString());
			}
			else if(opt.equals("del")){
				Db.update("update console_resource set delete_flag='1' where res_code='"+code+"' and delete_flag='0'");
				Db.update("delete from console_role_resource where res_id in (select res_id from console_resource where res_code='"+code+"')");
				OperateLogger.log(this, "资源管理", "删除", "删除code为:"+code+"资源");
			}
		}
		this.redirect("/ucenter/resources");
	}
	
	/**
	 * 创建当前节点下新自节点的code编码值
	 * @param code
	 * @param table
	 * @param code_field
	 * @return
	 */
	private String createSubCode(String code,String table,String code_field){
		String ncode="";
		if(code.equals("0000")){
			String sql="select * from "+table+" where delete_flag='0' and length("+code_field+")=4 order by "+code_field+" desc";
			Record rs = Db.findFirst(sql);
			if(rs!=null){
				String lastcode = rs.getStr(code_field);
				ncode = StringUtil.getNextCategoryCode(lastcode);
			}
			else{
				ncode="0001";
			}
		}
		else{
			String sql="select * from "+table+" where delete_flag='0' and "+code_field+" like '"+code+"%' and length("+code_field+")="+(code.length()+4)+" order by "+code_field+" desc";
			Record rs = Db.findFirst(sql);
			if(rs!=null){
				String lastcode = rs.getStr(code_field);
				ncode = StringUtil.getNextCategoryCode(lastcode);
			}
			else{
				ncode=code+"0001";
			}
		}
		return ncode;
	}
}
