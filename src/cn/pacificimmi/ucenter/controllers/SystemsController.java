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
import cn.pacificimmi.common.models.Websys;
import cn.pacificimmi.common.utils.StringUtil;
import cn.pacificimmi.ucenter.models.view.SystemInfo;

@Before(LoginInterCeptor.class) //用户权限拦截器
public class SystemsController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(SystemsController.class);
	public void index(){
		SystemParams sp = new SystemParams(this);
		ArrayList<SystemInfo> result = new ArrayList<SystemInfo>();
		/***
		 * 获取数据列表
		 */
		Page<Record> page = Db.paginate(sp.getPageNum(), sp.getPageSize(), sp.getSelectStr(), sp.getFromStr());
		List<Record> systems = page.getList();
		for(Record rd:systems){
			SystemInfo si = new SystemInfo();
			si.setSys_id(rd.getInt("sys_id"));
			si.setSys_name(rd.getStr("name"));
			si.setSys_domain(rd.getStr("domain"));
			si.setSys_create_time(StringUtil.yyyymmddhmsTime(rd.getDate("create_time")));
			result.add(si);
		}
		
		/***
		 * 保存数据列表
		 */
		this.setAttr("list", result);
		
		/***
		 * 保存翻页
		 */
		String pagesView = PagesBar.getShortPageBar(sp.getPageNum(), page.getTotalPage(), page.getPageSize(), page.getTotalRow(), 5);
		setAttr("pageBar",pagesView);
		
		/**
		 * 渲染视图
		 */
		this.renderJsp("/views/systems.jsp");
	}
	
	/**
	 * 查询参数解析类
	 * @author jeff
	 *
	 */
	private class SystemParams extends ParamsParser{

		public SystemParams(Controller ctr) {
			super(ctr);
			/***
			 * 处理传入参数
			 */
			this.setSelectStr("select * ");
			this.setFromStr("from console_websys ");
			String sys_name = this.getNormStr("sys_name");
			this.addWhereSegmentByAnd("delete_flag='0' ");
			if(sys_name!=null){
				this.addWhereSegmentByAnd(" name like '%"+sys_name+"%' ");
			}
			
			//默认排序
			this.setDefaultOrderStr("order by create_time desc");//或this.addOrderStr("create_time","desc");
			
			
			/****
			 * 或直接采用如下更灵活方式
			 */
//			this.setSelectStr("select * ");
//			StringBuffer bf = new StringBuffer();
//			bf.append("from console_websys ");
//			if(sys_name!=null && StringUtil.validateSearchKey(sys_name)){
//				bf.append(" name like '%"+sys_name+"%' ");
//			}
//			bf.append("order by create_time desc");
//			this.setFromStr(bf.toString());	
		}
	}
	
	/***
	 * 
	 */
	//@Before(Tx.class)
	public void remove(){
		SystemParams sp = new SystemParams(this);
		String uuid = UUID.randomUUID().toString();
		StringBuffer desc = new StringBuffer();
		desc.append("删除系统：");
		Map<String,Object> rst = new HashMap<String,Object>();
		if(!sp.getIds().isEmpty()){
			Db.update("update console_websys set delete_flag='1' where sys_id in ("+sp.getIds()+")");
			List<Websys> ss = Websys.dao.find("select * from console_websys where sys_id in ("+sp.getIds()+")");
			for(Websys s:ss){
				desc.append(s.getName()).append("、");
			}
			OperateLogger.log(this, "系统管理", "删除", desc.toString());
			logger.info("|delete|uuid:"+uuid+"|ids:"+sp.getIds());
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
}
