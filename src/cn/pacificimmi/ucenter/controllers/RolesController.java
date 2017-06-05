package cn.pacificimmi.ucenter.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.PagesBar;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Role;
import cn.pacificimmi.common.models.Websys;
import cn.pacificimmi.ucenter.models.view.RoleInfo;

@Before(LoginInterCeptor.class) 
public class RolesController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(RolesController.class);
	public void index(){
		RolesParams rp = new RolesParams(this);
		/***
		 * 获取数据列表
		 */
		Page<Record> page = Db.paginate(rp.getPageNum(), rp.getPageSize(), rp.getSelectStr(), rp.getFromStr());
		List<Record> list = page.getList();
		List<RoleInfo> result = new ArrayList<RoleInfo>();
		for(Record rd:list){
			RoleInfo ri =new RoleInfo();
			ri.bindingData(ri, rd);
			result.add(ri);
		}
		/***
		 * 保存数据列表
		 */
		this.setAttr("list", result);
		
		/***
		 * 初始化系统选项列表
		 */
		List<Websys> websyss = Websys.dao.find("select * from console_websys where delete_flag='0'");
		StringBuffer bf = new StringBuffer();
		bf.append("<select class=\"medium m-wrap\" name=\"sys_id\" id=\"sys_id\">");
		bf.append("<option value=\"0\">全部</option>");
		for(Websys mw:websyss){
			String selected = rp.getInt("sys_id");
			if(mw.getSysId().equals(selected))
				bf.append("<option value=\""+mw.getSysId()+"\" selected>");
			else
				bf.append("<option value=\""+mw.getSysId()+"\">");
			bf.append(mw.getName());
			bf.append("</option>");
		}
		bf.append("</select>");
		this.setAttr("sys_id", bf.toString());
		
		/***
		 * 保存翻页
		 */
		String pagesView = PagesBar.getShortPageBar(rp.getPageNum(), page.getTotalPage(), page.getPageSize(), page.getTotalRow(), 5);
		setAttr("pageBar",pagesView);
		
		/**
		 * 渲染视图
		 */
		this.renderJsp("/views/roles.jsp");
	}
	
	public void remove(){
		Map<String,Object> rst = new HashMap<String,Object>();
		RolesParams rp = new RolesParams(this);
		String uuid = UUID.randomUUID().toString();
		
		String ids = rp.getIds();
		if(ids!=null){
			String sql ="update console_role set delete_flag='1' where role_id in ("+ids+")";
			Db.update(sql);
			sql="delete from console_role_resource where role_id in ("+ids+")";
			Db.update(sql);
			sql="delete from console_role_station where role_id in ("+ids+")";
			Db.update(sql);
			
			List<Role> roles = Role.dao.find("select * from console where role_id in ("+ids+")");
			StringBuffer desc = new StringBuffer();
			for(Role r:roles){
				desc.append(r.getName()).append("、");
			}
			OperateLogger.log(this, "权限管理", "删除", desc.toString());
			logger.info("|delete|uuid:"+uuid+"|ids:"+ids);
			
			rst.put("status", 0);
			rst.put("msg", "删除成功");
			this.renderJson(rst);
		}
		else{
			rst.put("status", 100);
			rst.put("msg", "删除失败");
			this.renderJson(rst);
		}
	}
	
	private class RolesParams extends ParamsParser{
		public RolesParams(Controller ct) {
			super(ct);
			//默认选择列
			this.setSelectStr("select roles.*,websys.name as sys_name");
			this.setFromStr("from console_role as roles left join console_websys as websys on roles.sys_id=websys.sys_id");
			
			String name=this.getNormStr("name"); 
			
			//选择有效数据
			this.addWhereSegmentByAnd("roles.delete_flag='0'");
			this.addWhereSegmentByAnd("websys.delete_flag='0'");
			
			//搜索名称
			if(name!=null){
				this.addWhereSegmentByAnd("roles.name like '%"+name+"%'");
			}
			
			//搜索所属平台
			String sys_id=this.getInt("sys_id");
			if(sys_id!=null && !sys_id.equals("0")){
				this.addWhereSegmentByAnd("roles.sys_id='"+sys_id+"'");
			}
			
			this.setDefaultOrderStr("order by roles.create_time desc");
		}
	}
}
