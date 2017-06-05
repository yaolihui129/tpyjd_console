package cn.pacificimmi.ucenter.controllers;

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
import cn.pacificimmi.common.models.Dictionary;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.common.utils.StringUtil;

@Before(LoginInterCeptor.class) 
public class DictionaryController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(DictionaryController.class);
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
				setAttr("dict_code",code);
				/**
				 * 设定表单上级名称
				 */
				Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where dict_code='"+code+"' and delete_flag='0'");
				String parent = dict.getName();
				
				String dict_type="<select class=\"small m-wrap\" name=\"dict_type\">"
                                  +"<option value=\"ITEM\" selected>子项目</option>"
                                  +"<option value=\"GROUP\">分组</option>"							
								+"</select>";
				setAttr("dict_type",dict_type);
				
				String data_type="<select class=\"small m-wrap\" name=\"data_type\">"
                                  +"<option value=\"0\" >无数值</option>"
                                  +"<option value=\"1\" >数字类型</option>"
                                  +"<option value=\"2\" >数字区间</option>"
                                  +"<option value=\"3\" >日期</option>"
                                  +"<option value=\"4\" >日期区间</option>"	
                                  +"<option value=\"5\" >字符串</option>"	
								+"</select>";
				setAttr("data_type",data_type);
				
				setAttr("parent",parent);
			}
			else{
				/**
				 * 编辑
				 */
				setAttr("opt_title","编辑");
				setAttr("opt_type","edit");
				setAttr("dict_code",code);
				/**
				 * 设定表单上级机构名称
				 */
				String parent="通用字典";
				Dictionary dict;
				String parentCode = StringUtil.getParentCode(code);
				if(!parentCode.equals("0000")){
					dict = Dictionary.dao.findFirst("select * from console_dictionary where dict_code='"+parentCode+"' and delete_flag='0'");
					parent= dict.getName();
				}
				setAttr("parent",parent);
				
				/***
				 * 设定表单当前编辑的节点名称
				 */
				dict = Dictionary.dao.findFirst("select * from console_dictionary where dict_code='"+code+"' and delete_flag='0'");
				String name = dict.getName();
				String orgType = dict.getDictType();
				
				String short_name = "";
				if(dict.getShortName()!=null)
					short_name = dict.getShortName();
				setAttr("short_name",short_name);
				
				String sort = String.valueOf(dict.getSort());
				setAttr("sort",sort);
				
				String value = "";
				if(dict.getValue()!=null)
					value = dict.getValue();
				setAttr("value",value);
				
				String description = "";
				if(dict.getDescription()!=null)
					description = dict.getDescription();
				setAttr("description",description);
				
				String dict_type="<select class=\"small m-wrap\" name=\"dict_type\">";
					if(orgType.equals("ITEM"))
						dict_type+="<option value=\"ITEM\" selected>子项目</option>";
					if(orgType.equals("GROUP"))
						dict_type+="<option value=\"GROUP\" selected>分组</option>";				
	                 dict_type+="</select>";
				setAttr("dict_type",dict_type);
				setAttr("dict_id",dict.getDictId());
				
				int data_type = dict.getDataType();
				StringBuffer bf = new StringBuffer();
				bf.append("<select class=\"small m-wrap\" name=\"data_type\">");
				if(data_type==0)
					bf.append("<option value=\"0\" selected>无数值</option>");
				else
					bf.append("<option value=\"0\" >无数值</option>");
				if(data_type==1)
					bf.append("<option value=\"1\"  selected>数字类型</option>");
				else
					bf.append("<option value=\"1\" >数字类型</option>");
				if(data_type==2)
					bf.append("<option value=\"2\"  selected>数字区间</option>");
				else
					bf.append("<option value=\"2\" >数字区间</option>");
				if(data_type==3)
					bf.append("<option value=\"3\"  selected>日期</option>");
				else
					bf.append("<option value=\"3\" >日期</option>");
				if(data_type==4)
					bf.append("<option value=\"4\"  selected>日期区间</option>");
				else
					bf.append("<option value=\"4\" >日期区间</option>");
				if(data_type==5)
					bf.append("<option value=\"5\"  selected>字符串</option>");
				else
					bf.append("<option value=\"5\" >字符串</option>");
				bf.append("</select>");
				setAttr("data_type",bf.toString());
		
				setAttr("name",name);
			}
		}else{
			tree = loadTree();
			/**
			 * 新增
			 */
			setAttr("opt_title","新增");
			setAttr("opt_type","add");
			setAttr("dict_code","0000");
			setAttr("parent","通用字典");
			
			String dict_type="<select class=\"small m-wrap\" name=\"dict_type\">"
                    +"<option value=\"GROUP\" selected>分组</option>"
                    +"<option value=\"ITEM\">子项目</option>"
					+"</select>";
			setAttr("dict_type",dict_type);
			
			String data_type="<select class=\"small m-wrap\" name=\"data_type\">"
                    +"<option value=\"0\" selected>无数值</option>"
                    +"<option value=\"1\" >数字类型</option>"
                    +"<option value=\"2\" >数字区间</option>"
                    +"<option value=\"3\" >日期</option>"
                    +"<option value=\"4\">日期区间</option>"	
                    +"<option value=\"5\">字符串</option>"	
					+"</select>";
			setAttr("data_type",data_type);
		}
		setAttr("dictTree", tree);
		this.render("/views/dictionary.jsp");
	}
	
	public String loadTree(){
		return loadTree("");
	}
	
	public String loadTree(String code){
		List<Record> records = Db.find("select * from console_dictionary where delete_flag='0' order by dict_code asc");
		NodeTree ct = new NodeTree("dictTree","通用字典",0);
		
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
			node.setCode(r.getStr("dict_code"));
			node.setId(String.valueOf(r.getInt("dict_id")));
			
			node.setExtInfo(r.getStr("dict_type"));
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
		String type = pp.getNormStr("dict_type");
		String short_name = pp.getAllStr("short_name");
		String description = pp.getNormStr("description");
		String data_type=pp.getNormStr("data_type");
		String value = pp.getAllStr("value");
		String sort = pp.getInt("sort");
		String uuid = UUID.randomUUID().toString();
		if(type==null)
			type="GROUP";
		if(code!=null && opt!=null){
			if(opt.equals("add") && name!=null){
				Dictionary dict = new Dictionary();
				String ncode =createSubCode(code,"console_dictionary","dict_code");
				if(!ncode.isEmpty())
					dict.set("dict_code", ncode)
					.set("name", name)
					.set("dict_type", type)
					.set("dict_pcode", code)
					.set("data_type", data_type)
					.set("delete_flag", "0");
					if(short_name!=null)
						dict.set("short_name", short_name);
					if(value!=null)
						dict.set("value", value);
					if(description!=null)
						dict.set("description", description);
					if(sort!=null)
						dict.set("sort", sort);
					Dictionary pdict = Dictionary.dao.findFirst("select * from console_dictionary where dict_code='"+code+"' and delete_flag='0'");
					if(pdict!=null)
						dict.set("dict_pid", pdict.getDictId());
					
					dict.save();
					OperateLogger.log(this, "字典管理", "创建节点", "创建"+name+"节点 uuid:"+uuid);
					logger.info("|add|uuid:"+uuid+"|"+dict.toJson());
			}
			else if(opt.equals("edit") && name!=null){
				Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where dict_code='"+code+"' and delete_flag='0'");
				dict.set("name", name)
				.set("dict_type", type)
				.set("data_type", data_type)
				.set("delete_flag", "0");
				if(short_name!=null)
					dict.set("short_name", short_name);
				if(value!=null)
					dict.set("value", value);
				if(description!=null)
					dict.set("description", description);
				if(sort!=null)
					dict.set("sort", sort);
				dict.update();
				OperateLogger.log(this, "字典管理", "修改节点", "修改code:"+code+"节点信息");
				logger.info("|update|uuid:"+uuid+"|"+dict.toJson());
			}
			else if(opt.equals("del")){
				Db.update("update console_dictionary set delete_flag='1' where dict_code='"+code+"' and delete_flag='0'");
				Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where dict_code='"+code+"'");
				
				OperateLogger.log(this, "字典管理", "删除节点", "删除"+dict.getName()+"节点");
				logger.info("|delete|uuid:"+uuid+"|"+dict.toJson());
			}
		}
		this.redirect("/ucenter/dictionary");
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
