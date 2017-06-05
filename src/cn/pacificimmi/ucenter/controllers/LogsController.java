package cn.pacificimmi.ucenter.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.pacificimmi.common.PagesBar;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.OperationLog;

@Before(LoginInterCeptor.class) 
public class LogsController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(LogsController.class);
	public void index(){
		RolesParams rp = new RolesParams(this);
		
		/***
		 * 获取数据列表
		 */
		Page<OperationLog> page = OperationLog.dao.paginate(rp.getPageNum(), rp.getPageSize(), rp.getSelectStr(), rp.getFromStr());
		List<OperationLog> result = page.getList();
		
		/***
		 * 保存数据列表
		 */
		this.setAttr("list", result);
		
		/***
		 * 保存翻页
		 */
		String pagesView = PagesBar.getShortPageBar(rp.getPageNum(), page.getTotalPage(), page.getPageSize(), page.getTotalRow(), 5);
		setAttr("pageBar",pagesView);
		
		/**
		 * 渲染视图
		 */
		this.renderJsp("/views/logs.jsp");
	}	
	
	private class RolesParams extends ParamsParser{
		public RolesParams(Controller ct) {
			super(ct);
			//默认选择列
			this.setSelectStr("select * ");
			this.setFromStr("from console_operation_log ");
			
			String name=this.getNormStr("name"); 
				
			//搜索名称
			if(name!=null){
				this.addWhereSegmentByOr("opt_type = '"+name+"'");
				this.addWhereSegmentByOr("opt_module = '"+name+"'");
				this.addWhereSegmentByOr("opt_user = '"+name+"'");
			}
			
			this.setDefaultOrderStr("order by opt_time desc");
		}
	}
}
