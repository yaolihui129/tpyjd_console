package cn.pacificimmi.ucenter.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.PagesBar;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Station;
import cn.pacificimmi.common.models.User;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.ucenter.models.view.StationInfo;

@Before(LoginInterCeptor.class) //用户权限拦截器
public class UserStationsController extends Controller {
	public void index(){
		StationParams sp = new StationParams(this);
		/***
		 * 获取数据列表
		 */
		Page<Record> page = Db.paginate(sp.getPageNum(), sp.getPageSize(), sp.getSelectStr(), sp.getFromStr());
		List<Record> list = page.getList();
		List<StationInfo> result = new ArrayList<StationInfo>();
		for(Record rd:list){
			StationInfo si =new StationInfo();
			si.bindingData(si, rd);
			result.add(si);
		}
		
		/**
		 * 加载机构树
		 */
		//String[] checks = sp.getIntegers("chks");
//		ArrayList<String> checked = new ArrayList<String>();
//		String org_id = sp.getNormStr("org_id");
//		if(org_id!=null){
//			checked.add(org_id);
//		}
//		else
//			org_id="0";
//		this.setAttr("org_id", org_id);
//		
//		String org_name = sp.getNormStr("org_name");
//		if(org_name==null)
//			org_name="所属部门";
//		this.setAttr("org_name", org_name);
//		
//		String tree = loadTree(checked);
//		this.setAttr("tree", tree);
		
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
		this.renderJsp("/views/userstations.jsp");
	}
	
	/**
	 * 查询参数解析类
	 * @author jeff
	 *
	 */
	private class StationParams extends ParamsParser{

		public StationParams(Controller ctr) {
			super(ctr);
			
			this.setSelectStr("select station.name as station_name,station.station_id as station_id,orgnization.name as orgnization_name,station.create_time as station_create_time");
			this.setFromStr("from console_station as station left join console_orgnization as orgnization on station.com_id=orgnization.org_id right join console_user_station as user_station on station.station_id=user_station.station_id");
			
			//查找有效数据
//			this.addWhereSegmentByAnd("station.com_id=orgnization.org_id");
			this.addWhereSegmentByAnd("station.delete_flag='0'");
			//this.addWhereSegmentByAnd("orgnization.delete_flag='0'");
			
			String user_id = "0";
			if(this.getId()!=null){
				user_id=this.getId();
				setAttr("id",user_id);
			}
			this.addWhereSegmentByAnd("user_station.user_id='"+user_id+"'");
			
			//查询名称
			String name = this.getNormStr("name");
			if(name!=null)
				this.addWhereSegmentByAnd("station.name like '%"+name+"%'");
			
			this.setDefaultOrderStr("order by station.create_time desc");
		}
	}
	
	/***
	 * 
	 */
	//@Before(Tx.class)
	public void remove(){
		StationParams sp = new StationParams(this);
		StringBuffer desc = new StringBuffer();
		Map<String,Object> rst = new HashMap<String,Object>();
		if(!sp.getIds().isEmpty() && !sp.getId().isEmpty()){
			User user = User.dao.findById(sp.getId());
			desc.append("删除用户：").append(user.getUserName()).append("的");
			Db.update("delete from console_user_station where user_id='"+sp.getId()+"' and station_id in ("+sp.getIds()+")");
			List<Station> ss = Station.dao.find("select * from console_station where station_id in ("+sp.getIds()+")");
			for(Station s:ss){
				desc.append(s.getName()).append(",");
			}
			desc.append("的角色");
			
			OperateLogger.log(this, "用户管理/岗位绑定", "岗位解绑", desc.toString());
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
	
///////////////////////树状图加载////////////////////////////////
	public String loadTree(List<String> codes){
		List<Record> records = Db.find("select * from console_orgnization where delete_flag='0' order by org_code asc");
		NodeTree ct = new NodeTree("orgTree","所属机构",10);
		
		/***
		* 初始化时将选中的节点，描红显示
		*/
		if(codes.size()>0){
			ct.setSelectedCodes(codes);
		}
		
		/**
		* 显示选择框
		*/
		ct.setSelectMode(false);
		
		/**
		* 不显示编辑按钮
		*/
		ct.setAllownEdit(false);
		
		for(Record r:records){
			Node node = new Node();
			node.setName(r.getStr("name"));
			node.setCode(r.getStr("org_code"));
			node.setId(String.valueOf(r.getInt("org_id")));
			
			node.setExtInfo(String.valueOf(r.getInt("org_id")));
			ct.addNode(node);
		}
		
		return ct.buildTree();
	}
}
