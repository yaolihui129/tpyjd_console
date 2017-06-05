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
import com.jfinal.render.ContentType;

import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Orgnization;
import cn.pacificimmi.common.models.OrgnizationWebsys;
import cn.pacificimmi.common.models.Websys;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.common.utils.StringUtil;
import cn.pacificimmi.ucenter.models.LoginUserInfo;

@Before(LoginInterCeptor.class) 
public class SystemController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(SystemController.class);
	public void index(){
		this.renderText("参数错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}
	
	/**
	 * 渲染新增视图
	 */
	public void add(){
		ParamsParser pp = new ParamsParser(this);
		StringBuffer desc = new StringBuffer();
		if(pp.getNormStr("operation")!=null){
			/***
			 * 通过数据模型，保存数据
			 */
			Websys  mw = new Websys();
			//mw.set("sys_id", MajorKeyFactory.getInstance().getMajorKey());
			desc.append("新增系统，");
			String name = pp.getNormStr("name");
			if(name!=null){
				mw.set("name", name);//mw.setName(name);	
				desc.append("名称：").append(name).append("、");
			}
			String domain = pp.getAllStr("domain");
			if(domain!=null){
				mw.set("domain", domain);//mw.setDomain(domain);
				desc.append("域名：").append(domain).append("、");
			}
			String oathurl = pp.getAllStr("oathurl");
			if(oathurl!=null){
				mw.set("oathurl", oathurl);
				desc.append("登录切换地址：").append(oathurl).append("、");
			}
				
			//保存操作人信息
			LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
			if(lui!=null){
				mw.set("create_user",lui.getUserName());
				mw.set("update_user",lui.getUserName());
			}
			mw.set("update_time",StringUtil.yyyymmddhmsTime(new Date()));
			
			mw.set("delete_flag", "0").save();
			
			String org_id = pp.getNormStr("org_id");
			if(org_id!=null){
				OrgnizationWebsys mow = new OrgnizationWebsys();
				mow.set("sys_id", mw.getSysId());
				mow.set("org_id", Integer.valueOf(org_id));
				mow.save();
				desc.append("部门id：").append(org_id).append("、");
			}
			
			OperateLogger.log(this, "系统管理", "新增", desc.toString());
			/***
			 * 跳转到跳出时列表页地址
			 */
			String fromurl = getPara("fromurl");
			this.redirect(fromurl);
		}
		else
		{
			String tree = loadTree();
			this.setAttr("tree", tree);
			this.setAttr("operation", "新增");
			this.renderJsp("/views/system.jsp");
		}
	}
	
	public void update(){
		/**
		 * 创建参数解析类
		 */
		ParamsParser pp = new ParamsParser(this);
		StringBuffer desc = new StringBuffer();
		String uuid = UUID.randomUUID().toString();
		if(pp.getId()!=null){
			if(pp.getNormStr("operation")!=null){
				desc.append("修改系统信息，");
				Websys mw = Websys.dao.findById(pp.getInt("id"));
				String name = pp.getNormStr("name");
				if(name!=null){
					mw.set("name", name);
					desc.append("名称：").append(name).append("、");
				}
				
				String domain = pp.getAllStr("domain");
				if(domain!=null){
					mw.set("domain", domain);
					desc.append("域名：").append(domain).append("、");
				}
				
				String oathurl = pp.getAllStr("oathurl");
				if(oathurl!=null){
					mw.set("oathurl", oathurl);
					desc.append("登录切换地址：").append(oathurl).append("、");
				}
				String org_id = pp.getNormStr("org_id");
				if(org_id!=null){
					Db.update("update console_orgnization_websys set org_id='"+org_id+"' where sys_id='"+pp.getId()+"'");
					desc.append("部门id：").append(org_id);
				}
				
				mw.update();
				
				OperateLogger.log(this, "平台管理", "修改", desc.toString()+"|uuid:"+uuid);
				logger.info("|update|uuid:"+uuid+"|"+mw.toJson());
				String fromurl = getPara("fromurl");
				this.redirect(fromurl);
			}
			else
			{
				Websys mw = Websys.dao.findById(pp.getId());
				if(mw!=null){
					//加载对象数据
					this.setAttr("mw", mw);
					this.setAttr("operation", "编辑");
					
					//关联机构树
					Orgnization mo = Orgnization.dao.findFirst("select b.* from console_orgnization_websys as a,console_orgnization as b where a.org_id=b.org_id and a.sys_id='"+pp.getId()+"' and b.delete_flag='0'");
					String selectCode = "";
					if(mo!=null){
						selectCode = mo.getOrgCode();
					}
					this.setAttr("selectCode", selectCode);
					
					String tree=loadTree(selectCode);
					this.setAttr("tree", tree);
					this.renderJsp("/views/system.jsp");
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
	private String loadTree(){
		return loadTree("");
	}
	
	private String loadTree(String code){
		List<Record> records = Db.find("select * from console_orgnization where delete_flag='0' and org_type='ORG_TYPE_COMPANY' order by org_code asc");
		NodeTree ct = new NodeTree("orgTree","所属机构",10);
		
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
