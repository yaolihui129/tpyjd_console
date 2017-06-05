package cn.pacificimmi.ucenter.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Orgnization;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.common.utils.MajorKeyFactory;
import cn.pacificimmi.common.utils.StringUtil;

@Before(LoginInterCeptor.class) 
public class OrgnizationController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(OrgnizationController.class);
	public void index(){
		String tree="";
		String code=getPara("code");
		String opt_type=getPara("opt_type");
		if(code!=null && !code.isEmpty() && StringUtil.isNumeric(code) && !code.equals("0000")){
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
				setAttr("org_code",code);
				/**
				 * 设定表单上级机构名称
				 */
				Orgnization mo = Orgnization.dao.findFirst("select * from console_orgnization where org_code='"+code+"' and delete_flag='0'");
				String parent = mo.getName();
				
				String org_type="<select class=\"small m-wrap\" name=\"org_type\">"
                                  +"<option value=\"ORG_TYPE_DEPARTMENT\" selected>部门</option>"
                                  +"<option value=\"ORG_TYPE_COMPANY\">公司</option>"							
								+"</select>";
				setAttr("org_type",org_type);
				setAttr("parent",parent);
			}
			else{
				/**
				 * 编辑
				 */
				setAttr("opt_title","编辑");
				setAttr("opt_type","edit");
				setAttr("org_code",code);
				/**
				 * 设定表单上级机构名称
				 */
				String parent="组织机构";
				Orgnization mo;
				String parentCode = StringUtil.getParentCode(code);
				if(!parentCode.equals("0000")){
					mo = Orgnization.dao.findFirst("select * from console_orgnization where org_code='"+parentCode+"' and delete_flag='0'");
					parent= mo.getName();
				}
				setAttr("parent",parent);
				
				/***
				 * 设定表单当前编辑的节点名称
				 */
				mo = Orgnization.dao.findFirst("select * from console_orgnization where org_code='"+code+"' and delete_flag='0'");
				String name = mo.getName();
				String u8_code = mo.getU8Code();
				String orgType = mo.getOrgType();
				
				String org_type="<select class=\"small m-wrap\" name=\"org_type\">";
					if(orgType.equals("ORG_TYPE_DEPARTMENT"))
						org_type+="<option value=\"ORG_TYPE_DEPARTMENT\" selected>部门</option>";
					if(orgType.equals("ORG_TYPE_COMPANY"))
						org_type+="<option value=\"ORG_TYPE_COMPANY\" selected>公司</option>";				
	                 org_type+="</select>";
				setAttr("org_type",org_type);
				
				setAttr("name",name);
				setAttr("u8_code",u8_code);
			}
		}else{
			tree = loadTree();
			/**
			 * 新增
			 */
			setAttr("opt_title","新增");
			setAttr("opt_type","add");
			setAttr("org_code","0000");
			setAttr("parent","组织机构");
			
			String org_type="<select class=\"small m-wrap\" name=\"org_type\">"
                    +"<option value=\"ORG_TYPE_DEPARTMENT\" selected>部门</option>"
                    +"<option value=\"ORG_TYPE_COMPANY\">公司</option>"							
					+"</select>";
			setAttr("org_type",org_type);
		}
		setAttr("orgTree", tree);
		this.render("/views/orgnization.jsp");
	}
	
	public String loadTree(){
		return loadTree("");
	}
	
	public String loadTree(String code){
		List<Record> records = Db.find("select * from console_orgnization where delete_flag='0' order by org_code asc");
		NodeTree ct = new NodeTree("orgTree","组织机构",1);
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
			node.setCode(r.getStr("org_code"));
			node.setId(String.valueOf(r.getInt("org_id")));
			
			node.setExtInfo(r.getStr("org_type"));
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
		String name = pp.getNormStr("name");
		String type = pp.getNormStr("org_tpye");
		String u8_code = pp.getNormStr("u8_code");
		
		String uuid = UUID.randomUUID().toString();
		if(type==null)
			type="ORG_TYPE_DEPARTMENT";
		if(code!=null && opt!=null){
			if(opt.equals("add") && name!=null){
				Orgnization mo = new Orgnization();
				String ncode =createSubCode(code,"console_orgnization","org_code");
				if(!ncode.isEmpty())
					mo.set("org_code", ncode)
					.set("name", name)
					.set("org_type", type)
					.set("u8_code", u8_code)
					.set("found_time", StringUtil.yyyymmddhmsTime(new Date()))
					.set("delete_flag", "0")
					.save();
				
				OperateLogger.log(this, "机构管理", "创建机构", "创建"+name+"机构节点 |uuid:"+uuid);
				logger.info("|add|uuid:"+uuid+"|"+mo.toJson());
			}
			else if(opt.equals("edit") && name!=null){
				Orgnization o = Orgnization.dao.findFirst("select * from console_orgnization where org_code='"+code+"' and delete_flag='0'");
				//
				o.setName(name);
				o.setOrgType(type);
				o.setU8Code(u8_code);
				o.update();
				//Db.update("update console_orgnization set name='"+name+"',org_type='"+type+"',u8_code='"+u8_code+"' where org_code='"+code+"' and delete_flag='0'");
				OperateLogger.log(this, "机构管理", "修改机构", "修改code为："+code+"机构节点名称->"+name+" |uuid:"+uuid);
				Orgnization n = Orgnization.dao.findFirst("select * from console_orgnization where org_code='"+code+"' and delete_flag='0'");
				logger.info("|update|uuid:"+uuid+"|"+o.toJson()+"|"+n.toJson());
			}
			else if(opt.equals("del")){
				//判断部门下是否有子用户
				List<Record> users = Db.find("select * from console_user as a left join console_orgnization as b on a.dep_id=b.org_id where b.org_code like '"+code+"%'");
				if(users.size()==0){
					Db.update("update console_orgnization set delete_flag='1' where org_code='"+code+"' and delete_flag='0'");
					OperateLogger.log(this, "机构管理", "删除机构", "删除code为："+code+"机构节点 |uuid:"+uuid);
					Orgnization n = Orgnization.dao.findFirst("select * from console_orgnization org_code='"+code+"'");
					logger.info("|delete|uuid:"+uuid+"|"+n.toJson());
				}
			}
		}
		this.redirect("/ucenter/orgnizations");
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
