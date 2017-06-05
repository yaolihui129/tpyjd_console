package cn.pacificimmi.ucenter.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.MultipartRequest;
import com.jfinal.upload.UploadFile;

import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.PagesBar;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.AjaxLoginInterCeptor;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Dictionary;
import cn.pacificimmi.common.models.User;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.common.utils.Client;
import cn.pacificimmi.common.utils.CrmUtils;
import cn.pacificimmi.common.utils.StringUtil;
import cn.pacificimmi.ucenter.models.LoginUserInfo;
import cn.pacificimmi.ucenter.models.view.UserInfo;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Before(LoginInterCeptor.class) //用户权限拦截器
public class UsersController extends Controller {
	private static Logger log = LoggerFactory.getLogger(UsersController.class);
	public void index(){
		UserParams sp = new UserParams(this);
		LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
		setAttr("loginuser",lui);
		/***
		 * 获取数据列表
		 */
		Page<Record> page = Db.paginate(sp.getPageNum(), sp.getPageSize(), sp.getSelectStr(), sp.getFromStr());
		List<Record> list = page.getList();
		List<UserInfo> result = new ArrayList<UserInfo>();
		for(Record rd:list){
			UserInfo ui =new UserInfo();
			ui.bindingData(ui, rd);
			result.add(ui);
		}
		
		/**
		 * 加载机构树
		 */
		ArrayList<String> checked = new ArrayList<String>();
		String org_id = sp.getNormStr("org_id");
		if(org_id!=null){
			checked.add(org_id);
		}
		else
			org_id="0";
		this.setAttr("org_id", org_id);
		
		String org_name = sp.getNormStr("org_name");
		if(org_name==null)
			org_name="所属部门";
		this.setAttr("org_name", org_name);
		
		String tree = loadTree(checked);
		this.setAttr("tree", tree);
		
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
		this.renderJsp("/views/users.jsp");
	}
	
	/**
	 * 查询参数解析类
	 * @author jeff
	 *
	 */
	private class UserParams extends ParamsParser{

		public UserParams(Controller ctr) {
			super(ctr);
			
			this.setSelectStr("select u.*,o.name as dep_name");
			this.setFromStr("from console_user as u left join console_orgnization as o on u.dep_id=o.org_id");
			
			//查找有效数据
			this.addWhereSegmentByAnd("u.delete_flag='0'");
			
			//查询所属公司
			String org_id = this.getNormStr("org_id");
			if(org_id!=null && !org_id.equals("0"))
				this.addWhereSegmentByAnd("u.dep_id='"+org_id+"'");
			
			//查询登录名称
			String name = this.getNormStr("name");
			if(name!=null){
				this.addWhereSegmentByAnd("(u.login_name like '%"+name+"%' or u.user_name like '%"+name+"%')");
				ctr.setAttr("name", name);
			}
			
			this.setDefaultOrderStr("order by u.create_time desc");
		}
	}

	/**
	 * 导出用户列表
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	@Clear
	public void exportUsers() throws IOException, RowsExceededException, WriteException{
		String filename="UserList_";
		LoginUserInfo ui = getSessionAttr("LoginUserInfo");
		if(ui!=null)
			filename+=ui.getLoginName();
		filename+="_";
		filename+=System.currentTimeMillis();
		filename+=".xls";
		
		OutputStream os =this.getResponse().getOutputStream();
		this.getResponse().setCharacterEncoding("UTF-8");
		this.getResponse().setHeader("Content-Disposition","attachment;filename="+filename);
		this.getResponse().setContentType("application/msexcel");
		//创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("员工信息",0);
        
        String sql="select * from console_user where delete_flag='0'";
        List<User> users = User.dao.find(sql);
        Label label;
        //输出excel头信息
        label = new Label(0,0,"姓名");
        sheet.addCell(label);
        label = new Label(1,0,"工号");
        sheet.addCell(label);
        label = new Label(2,0,"登录名");
        sheet.addCell(label);
        label = new Label(3,0,"英文名");
        sheet.addCell(label);
        label = new Label(4,0,"所属公司");
        sheet.addCell(label);
        label = new Label(5,0,"所属部门");
        sheet.addCell(label);
        label = new Label(6,0,"性别");
        sheet.addCell(label);
        label = new Label(7,0,"手机号");
        sheet.addCell(label);
        label = new Label(8,0,"座机");
        sheet.addCell(label);
        label = new Label(9,0,"qq");
        sheet.addCell(label);
        label = new Label(10,0,"邮箱");
        sheet.addCell(label);
        label = new Label(11,0,"主岗");
        sheet.addCell(label);
        label = new Label(12,0,"职务级别");
        sheet.addCell(label);
        label = new Label(13,0,"工作地省/直辖市");
        sheet.addCell(label);
        label = new Label(14,0,"工作地所在城市");
        sheet.addCell(label);
        
        for(int i=0;i<users.size();i++)
        {
        		User user = users.get(i);
        		label = new Label(0,i+1,user.getUserName());
            sheet.addCell(label);
            label = new Label(1,i+1,user.getJobNumber());
            sheet.addCell(label);
            label = new Label(2,i+1,user.getLoginName());
            sheet.addCell(label);
            label = new Label(3,i+1,user.getEnglishName());
            sheet.addCell(label);
            
            if(user.getComId()!=null){
	        		Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where delete_flag='0' and dict_id='"+user.getComId()+"'");
				if(dict!=null){
					label = new Label(4,i+1,dict.getName());
		            sheet.addCell(label);
				}
	        }
            
            if(user.getDepId()!=null){
	        		Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where delete_flag='0' and dict_id='"+user.getDepId()+"'");
				if(dict!=null){
					label = new Label(5,i+1,dict.getName());
		            sheet.addCell(label);
				}
	        }
            
            if(user.getGender()!=null){
            		String gender="";
            		if(user.getGender().equals("MALE"))
            			gender="男";
            		else
            			gender="女";
	            label = new Label(6,i+1,gender);
	            sheet.addCell(label);
            }
            label = new Label(7,i+1,user.getPhone());
            sheet.addCell(label);
            label = new Label(8,i+1,user.getTel());
            sheet.addCell(label);
            label = new Label(9,i+1,user.getQq());
            sheet.addCell(label);
            label = new Label(10,i+1,user.getEmail());
            sheet.addCell(label);
            label = new Label(11,i+1,user.getMasterPost());
            sheet.addCell(label);
            label = new Label(12,i+1,user.getPostLevel());
            sheet.addCell(label);
            
            if(user.getProvinceId()!=null){
            		Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where delete_flag='0' and dict_code='"+user.getProvinceId()+"'");
				if(dict!=null){
					label = new Label(13,i+1,dict.getName());
		            sheet.addCell(label);
				}
            }
            
            if(user.getCityId()!=null){
	        		Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where delete_flag='0' and dict_code='"+user.getCityId()+"'");
				if(dict!=null){
					label = new Label(14,i+1,dict.getName());
		            sheet.addCell(label);
				}
	        }
        }
        
      //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
        
       this.renderNull();
	}
	
	/**
	 * 导入用户
	 */
	@SuppressWarnings("unchecked")
	@Clear
	public void importUsers(){
		String expStr="";
		ServletFileUpload fileupload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> fileitems;
		try {
			jxl.Workbook workbook = null;
			
			fileitems = fileupload.parseRequest(this.getRequest());
			Map<String,String> params = new HashMap<String,String>();
			String value = null;
			String key = null;
			for(FileItem fileitem : fileitems) {
			    if(fileitem.isFormField()) {
			        value = fileitem.getString("utf-8");
			        key = fileitem.getFieldName();
			        
			        params.put(key, value);
			    }else {
			    		InputStream is =fileitem.getInputStream();
			        
			        workbook = Workbook.getWorkbook(is);
			    }
			}
			
			//获取第一张Sheet表
			Sheet rs = workbook.getSheet(0);
			if(rs!=null){
				StringBuffer logout = new StringBuffer();
				int expNum=0;
				
				String opt =params.get("opt");
				String optName ="";
				if(opt.equals("0")){
					logout.append("跳过：");
					optName="跳过";
				}
				else{
					logout.append("覆盖：");
					optName="覆盖";
				}
				
				Cell cell = null;
				for(int i=1;i<rs.getRows();i++){
					boolean exist = false;
					
					String userName="";
					cell = rs.getCell(0, i);
					if(cell!=null){
						userName = cell.getContents();
					}
					
					cell = rs.getCell(2, i);
					
					String loginName="";
					if(cell!=null){
						loginName = cell.getContents();
					}
					
					User user = User.dao.findFirst("select * from console_user where delete_flag='0' and login_name='"+loginName+"'");
					if(user!=null){
						exist=true;
						expNum++;
						logout.append(userName+"-"+loginName+"、");
						
						if(opt.equals("0")){
							continue;
						}
					}
					else{
						user = new User();
						user.setLoginName(loginName);
					}
					
					user.setUserName(userName);
					
					cell = rs.getCell(1, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setJobNumber(contents);
					}
					
					cell = rs.getCell(3, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setEnglishName(contents);
					}
					
					cell = rs.getCell(4, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty()){
							
							if(contents.equals("男")){
								user.setGender("MALE");
							}
							else{
								user.setGender("FEMALE");
							}
						}
					}
					
					cell = rs.getCell(5, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setPhone(contents);
					}
					
					cell = rs.getCell(6, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setTel(contents);
					}
					
					cell = rs.getCell(7, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setQq(contents);
					}
					
					cell = rs.getCell(8, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setEmail(contents);
					}
					
					cell = rs.getCell(9, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setMasterPost(contents);
					}
					
					cell = rs.getCell(10, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty())
							user.setPostLevel(contents);
					}
					
					cell = rs.getCell(11, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty()){
							Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where delete_flag='0' and name='"+contents+"'");
							if(dict!=null){
								user.setProvinceId(dict.getDictCode());
							}
						}
					}
					
					cell = rs.getCell(12, i);
					if(cell!=null){
						String contents = cell.getContents();
						if(!contents.isEmpty()){
							Dictionary dict = Dictionary.dao.findFirst("select * from console_dictionary where delete_flag='0' and name='"+contents+"'");
							if(dict!=null){
								user.setCityId(dict.getDictCode());
							}
						}
					}
					
					if(exist){
						user.update();
					}
					else{
						user.save();
					}
				}
				
				String result="数据导入成功！<br/>";
				result+="本次成功导入"+(rs.getRows()-1)+"条";
				if(expNum>0){
					result+=",";
					result+= optName+expNum+"条<br/>";
				}
				setAttr("msg",result);
				this.renderJsp("/views/msg.jsp");
				return;
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			expStr=e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			expStr=e.getMessage();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			expStr=e.getMessage();
		}
		
		setAttr("msg","数据导入失败！<br/>错误信息："+expStr);
		this.renderJsp("/views/msg.jsp");
	}
	
	/***
	 * 
	 */
	//@Before(Tx.class)
	@Clear
	@Before(AjaxLoginInterCeptor.class)
	public void remove(){
		UserParams sp = new UserParams(this);
		StringBuffer desc = new StringBuffer();
		desc.append("删除用户:");
		Map<String,Object> rst = new HashMap<String,Object>();
		if(!sp.getId().isEmpty()){
			List<Record> list = Db.find("select * from crm_steward_sign as css,crm_steward_user as csu where csu.steward_id=css.steward_id and csu.user_id='"+sp.getId()+"' and sign_status='聘用'");
			if(list.size()>0){
				rst.put("status", 100);
				rst.put("msg", "该管家下有服务中的客户，不能删除！");
				this.renderJson(rst);
			}
			else{
				String sql="update console_user set delete_flag='1',openid='',bind_status='2' where user_id in ("+sp.getId()+")";
				Db.update(sql);
				
				sql="delete from console_user_station where user_id in ("+sp.getId()+")";
				Db.update(sql);
				
				List<User> users = User.dao.find("select * from console_user where user_id in ("+sp.getId()+")");
				for(User u:users){
					desc.append(u.getUserName()).append(";");
				}
				
				OperateLogger.log(this, "用户管理", "删除", desc.toString());
				rst.put("status", 0);
				rst.put("msg", "删除成功");
				this.renderJson(rst);
			}
		}
		else{
			rst.put("status", 100);
			rst.put("msg", "删除失败");
			this.renderJson(rst);
		}
	}

	/**
	 * 冻结、解冻帐号
	 */
	@Clear
	@Before(AjaxLoginInterCeptor.class)
	public void frozen(){
		UserParams sp = new UserParams(this);
		StringBuffer desc = new StringBuffer();
		
		Map<String,Object> rst = new HashMap<String,Object>();
		if(!sp.getId().isEmpty()){
			User user = User.dao.findFirst("select * from console_user where delete_flag='0' and user_id='"+sp.getId()+"'");
			int frozen = user.getFrozen();
			if(frozen==0){
				frozen=1;
				desc.append("冻结用户:").append(user.getLoginName());
				
				OperateLogger.log(this, "用户管理", "冻结用户", desc.toString());
			}
			else{
				frozen=0;
				desc.append("解冻用户:").append(user.getLoginName());
				OperateLogger.log(this, "用户管理", "解冻用户", desc.toString());
			}
			
			if(frozen==1){
				List<Record> list = Db.find("select * from crm_steward_sign as css,crm_steward_user as csu where csu.steward_id=css.steward_id and csu.user_id='"+sp.getId()+"' and sign_status='聘用'");
				if(list.size()>0){
					rst.put("status", 100);
					rst.put("msg", "该管家下有服务中的客户，不能冻结！");
					this.renderJson(rst);
				}
				else{
					user.setFrozen(frozen);
					user.update();
					
					rst.put("status", 0);
					rst.put("msg", "操作成功");
					this.renderJson(rst);	
				}
			}
			else{
				user.setFrozen(frozen);
				user.update();
				
				rst.put("status", 0);
				rst.put("msg", "操作成功");
				this.renderJson(rst);	
			}
		}
		else{
			rst.put("status", 100);
			rst.put("msg", "操作失败");
			this.renderJson(rst);
		}
	}
	
	@Clear
	@Before(AjaxLoginInterCeptor.class)
	public void init(){
		UserParams sp = new UserParams(this);
		Map<String,Object> rst = new HashMap<String,Object>();
		if(!sp.getIds().isEmpty()){
			Db.update("update console_user set password='21218cca77804d2ba1922c33e0151105' where user_id in ("+sp.getIds()+")");
			rst.put("status", 0);
			rst.put("msg", "初始化成功");
			this.renderJson(rst);
		}
		else{
			rst.put("status", 100);
			rst.put("msg", "初始化失败");
			this.renderJson(rst);
		}
	}
	
	@Clear
	@Before(AjaxLoginInterCeptor.class)
	public void crm(){
		ParamsParser pp = new ParamsParser(this);
		Map<String,Object> rst = new HashMap<String,Object>();
		String id = pp.getId();
		if(!id.isEmpty()){
			User user = User.dao.findById(id);
			if(user.getLoginName().isEmpty()){
				rst.put("status", 100);
				rst.put("msg", "用户的登录名不能为空，请补充完整再提交请求！");
				this.renderJson(rst);
			}
			else if(user.getEnglishName().isEmpty()){
				rst.put("status", 100);
				rst.put("msg", "用户的英文名不能为空，请补充完整再提交请求！");
				this.renderJson(rst);
			}
			else if(user.getUserName().isEmpty()){
				rst.put("status", 100);
				rst.put("msg", "用户的姓名不能为空，请补充完整再提交请求！");
				this.renderJson(rst);
			}
			else if(user.getPhone().isEmpty()){
				rst.put("status", 100);
				rst.put("msg", "用户的手机号不能为空，请补充完整再提交请求！");
				this.renderJson(rst);
			}
			else if(user.getEmail().isEmpty()){
				rst.put("status", 100);
				rst.put("msg", "用户的电子邮箱不能为空，请补充完整再提交请求！");
				this.renderJson(rst);
			}
			else{
				Map<String,String> params = new HashMap<String,String>();
				params.put("serviceName", "insert");
				params.put("objectApiName", "ccuser");
				params.put("binding", CrmUtils.getBinding());
				
				String Url = PropKit.get("CRM_API_URL");
				JSONArray data = new JSONArray();
				JSONObject userObj = new JSONObject();
				userObj.put("loginname", user.getLoginName());
				userObj.put("name", user.getUserName());
				userObj.put("ename", user.getEnglishName());
				userObj.put("mobile", user.getPhone());
				userObj.put("email", user.getEmail());
//				userObj.put("title", "");
//				userObj.put("qq", "");
//				userObj.put("wxzh", "");
//				userObj.put("region", "");
//				userObj.put("beizhu", "");
//				userObj.put("createdate", StringUtil.yyyymmddhmsTime(user.getCreateTime()));
				
				if(!user.getJobNumber().isEmpty())
					userObj.put("employeenum", user.getJobNumber());
				data.add(userObj);
				
				params.put("data", data.toString());
				
				try {
					log.info("crm begin:"+JsonKit.toJson(params));
					String response = Client.doPost(Url, params);
					log.info("crm end:"+response);
					JSONObject resObj = JSONObject.fromObject(response);
					if(resObj.getBoolean("result") && resObj.getString("returnCode").equals("1")){
						JSONArray ids = resObj.getJSONObject("data").getJSONArray("ids");
						if(ids!=null && ids.size()==1){
							JSONObject sUser = ids.getJSONObject(0);
							String crm_id = sUser.getString("id");
							user.setCrmId(crm_id);
							user.update();
							
							rst.put("status", 0);
							rst.put("msg", "开通成功！");
							this.renderJson(rst);
							return;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//开通失败
				rst.put("status", 100);
				rst.put("msg", "开通错误！");
				this.renderJson(rst);
			}
		}
		else{
			rst.put("status", 1);
			rst.put("msg", "参数错误！");
			this.renderJson(rst);
		}
	}
	
///////////////////////树状图加载////////////////////////////////
	public String loadTree(List<String> codes){
		List<Record> records = Db.find("select * from console_orgnization where delete_flag='0' order by org_code asc");
		NodeTree ct = new NodeTree("orgTree","所属部门",10);
		
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
