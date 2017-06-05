package cn.pacificimmi.ucenter.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.ContentType;

import cn.pacificimmi.common.Dictionary;
import cn.pacificimmi.common.DictionaryManager;
import cn.pacificimmi.common.OperateLogger;
import cn.pacificimmi.common.ParamsParser;
import cn.pacificimmi.common.interceptor.LoginInterCeptor;
import cn.pacificimmi.common.models.Country;
import cn.pacificimmi.common.models.Orgnization;
import cn.pacificimmi.common.models.Province;
import cn.pacificimmi.common.models.Station;
import cn.pacificimmi.common.models.User;
import cn.pacificimmi.common.models.UserStation;
import cn.pacificimmi.common.tree.Node;
import cn.pacificimmi.common.tree.NodeTree;
import cn.pacificimmi.common.utils.StringUtil;
import cn.pacificimmi.ucenter.models.LoginUserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.pacificimmi.common.utils.Client;
import cn.pacificimmi.common.utils.CrmUtils;
import cn.pacificimmi.common.utils.MD5Util;
import cn.pacificimmi.common.utils.MajorKeyFactory;

@Before(LoginInterCeptor.class) 
public class UserController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	public void index(){
		this.renderText("参数错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}
	
	public void checkExist(){
		ParamsParser pp = new ParamsParser(this);
		String loginname = pp.getNormStr("loginname");
		if(loginname!=null){
			User user = User.dao.findFirst("select * from console_user where login_name='"+loginname+"' and delete_flag='0'");
			if(user!=null){
				this.renderJson("{\"status\":500,\"msg\":\"exist item\"}");
			}
			else
				this.renderJson("{\"status\":0,\"msg\":\"ok\"}");
		}
		else
			this.renderJson("{\"status\":1,\"msg\":\"param error\"}");
	}
	
	public void checkJN(){
		ParamsParser pp = new ParamsParser(this);
		String jobnumber = pp.getNormStr("jobnumber");
		if(jobnumber!=null){
			User user = User.dao.findFirst("select * from console_user where job_number='"+jobnumber+"' and delete_flag='0'");
			if(user!=null){
				this.renderJson("{\"status\":500,\"msg\":\"exist item\"}");
			}
			else
				this.renderJson("{\"status\":0,\"msg\":\"ok\"}");
		}
		else
			this.renderJson("{\"status\":1,\"msg\":\"param error\"}");
	}
	
	/**
	 * 渲染新增视图
	 */
	public void add(){
		ParamsParser pp = new ParamsParser(this);
		StringBuffer desc = new StringBuffer();
		desc.append("新增用户：");
		if(pp.getNormStr("operation")!=null){
			/***
			 * 通过数据模型，保存数据
			 */
			User  mu = new User();
			//mu.set("user_id", MajorKeyFactory.getInstance().getMajorKey());
			
			//登录名
			String login_name= pp.getNormStr("login_name");
			if(login_name!=null){
				mu.set("login_name", login_name);
				desc.append("用户名:").append(login_name).append(";");
			}
			//密码
			String password = pp.getNormStr("password");
			if(password!=null){
				mu.set("password", MD5Util.MD5(password));
				desc.append("密码:").append(password).append(";");
			}
			//姓名
			String user_name = pp.getAllStr("user_name");
			if(user_name!=null){
				mu.set("user_name", user_name);
				desc.append("姓名:").append(user_name).append(";");
			}
			//主岗
			String master_post = pp.getNormStr("master_post");
			if(master_post!=null){
				mu.set("master_post", master_post);
				desc.append("主岗:").append(master_post).append(";");
			}
			//职务级别
			String post_level = pp.getNormStr("post_level");
			if(post_level!=null){
				mu.set("post_level", post_level);
				desc.append("职务级别:").append(post_level).append(";");
			}
			//座机
			String tel = pp.getAllStr("tel");
			if(tel!=null){
				mu.set("tel", tel);
				desc.append("座机:").append(tel).append(";");
			}
			//工号
			String job_number = pp.getNormStr("job_number");
			if(job_number!=null){
				mu.set("job_number", job_number);
				desc.append("工号:").append(job_number).append(";");
			}
			
			//职称
			String job_title = pp.getNormStr("job_title");
			if(job_title!=null){
				mu.set("job_title", job_title);
				desc.append("职称:").append(job_title).append(";");
			}
			//性别
			String gender = pp.getNormStr("gender");
			if(gender!=null){
				mu.set("gender", gender);
				desc.append("性别:").append(gender).append(";");
			}
			//电话
			String phone = pp.getNormStr("phone");
			if(phone!=null){
				mu.set("phone", phone);
				desc.append("电话:").append(phone).append(";");
			}
			//头像
			String head_img = this.getPara("head_img");
			if(head_img!=null){
				mu.set("head_img", head_img);
				desc.append("头像:").append(head_img).append(";");
			}
			//部门
			String org_id = pp.getNormStr("org_id");
			if(org_id!=null){
				mu.set("dep_id", org_id);
				desc.append("部门:").append(org_id).append(";");
				//获取所在公司
				String com_id = getComId(org_id);
				mu.set("com_id", com_id);
			}
			
			
			//数据分流设置
			String dataRange = pp.getNormStr("data_range");
			if(dataRange!=null){
				mu.set("data_range", dataRange);
				desc.append("数据分流设置:").append(dataRange).append(";");
			}
			
			//openid绑定状态
//			String bind_status = pp.getNormStr("bind_status");
//			if(bind_status!=null)
//				mu.set("bind_status", bind_status);
			
			//英文名
			String english_name = pp.getAllStr("english_name");
			if(english_name!=null){
				mu.set("english_name", english_name);
				desc.append("英文名:").append(english_name).append(";");
			}
			
			//qq
			String qq = pp.getNormStr("qq");
			if(qq!=null){
				mu.set("qq", qq);
				desc.append("qq:").append(qq).append(";");
			}
			
			//微信
			String wechat = pp.getAllStr("wechat");
			if(wechat!=null){
				mu.set("wechat", wechat);
				desc.append("微信:").append(wechat).append(";");
			}
			
			
			//电子邮箱
			String email =getPara("email");
			if(email!=null){
				mu.set("email", email);
				desc.append("邮箱:").append(email).append(";");
			}
			
			//所在地区
			String province_id =getPara("province_id");
			if(province_id!=null){
				mu.set("province_id", province_id);
				desc.append("所在省:").append(province_id).append(";");
			}
			
			//选择市
			String city_id =getPara("city_id");
			if(city_id!=null){
				mu.set("city_id", city_id);
				desc.append("所在市:").append(city_id).append(";");
			}
			
			//坐席号
			String seat_number = pp.getNormStr("seat_number");
			if(seat_number!=null){
				mu.set("seat_number", seat_number);
			}
			
			//个人简介
			String introduce = pp.getAllStr("introduce");
			if(introduce!=null){
				mu.set("introduce", introduce);
				desc.append("个人简介:").append(introduce).append(";");
			}
			
			//保存操作人信息
			LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
			if(lui!=null){
				mu.set("create_user",lui.getUserName());
				mu.set("update_user",lui.getUserName());
			}
			mu.set("update_time",StringUtil.yyyymmddhmsTime(new Date()));
			mu.set("delete_flag", "0").save();
			
			OperateLogger.log(this, "用户管理", "新增", desc.toString());
			
			/***
			 * 跳转到跳出时列表页地址
			 */
			String fromurl = getPara("fromurl");
			this.redirect(fromurl);
		}
		else
		{
			StringBuffer bf = new StringBuffer();
            Dictionary dict = DictionaryManager.getInstance().getAllSubDictionaries("0015", 1);
            setAttr("bind_status",dict);
            
            //性别
            bf.setLength(0);
            bf.append("<select class=\"m-wrap span6\" name=\"gender\">");	
            bf.append("<option value=\"MALE\" >男</option>");	
            bf.append("<option value=\"FEMALE\" selected='selected'>女</option>");	
            bf.append("</select>");
            this.setAttr("gender", bf.toString());
           
            Dictionary dataRange = DictionaryManager.getInstance().getAllSubDictionaries("0012", 1);
			setAttr("dataRange",dataRange);
			
            //部门树
			String tree = loadTree();
			this.setAttr("tree", tree);

			this.setAttr("operation", "新增");
			
			Dictionary provinces = DictionaryManager.getInstance().getAllSubDictionaries("000300020001", 1);
			this.setAttr("provinces",provinces);
			
			this.renderJsp("/views/user.jsp");
		}
	}
	
	public void update(){
		/**
		 * 创建参数解析类
		 */
		ParamsParser pp = new ParamsParser(this);
		StringBuffer desc = new StringBuffer();
		desc.append("修改用户：");
		if(pp.getId()!=null){
			if(pp.getNormStr("operation")!=null){
				User mu = User.dao.findById(pp.getId()); 
				//登录名
				String login_name= pp.getNormStr("login_name");
				if(login_name!=null){
					mu.set("login_name", login_name);
					desc.append("用户名:").append(login_name).append(";");
				}
				//密码
				String password = pp.getNormStr("password");
				if(password!=null && !password.equals("888888")){
					mu.set("password", MD5Util.MD5(password));
					desc.append("密码:").append(password).append(";");
				}
				//主岗
				String master_post = pp.getNormStr("master_post");
				if(master_post!=null){
					mu.set("master_post", master_post);
					desc.append("主岗:").append(master_post).append(";");
				}
				//职务级别
				String post_level = pp.getNormStr("post_level");
				if(post_level!=null){
					mu.set("post_level", post_level);
					desc.append("职务级别:").append(post_level).append(";");
				}
				//座机
				String tel = pp.getAllStr("tel");
				if(tel!=null){
					mu.set("tel", tel);
					desc.append("座机:").append(tel).append(";");
				}
				//姓名
				String user_name = pp.getAllStr("user_name");
				if(user_name!=null){
					mu.set("user_name", user_name);
					desc.append("姓名:").append(user_name).append(";");
				}
				//职称
				String job_title = pp.getNormStr("job_title");
				if(job_title!=null){
					mu.set("job_title", job_title);
					desc.append("职称:").append(job_title).append(";");
				}
				//性别
				String gender = pp.getNormStr("gender");
				if(gender!=null){
					mu.set("gender", gender);
					desc.append("性别:").append(gender).append(";");
				}
				//电话
				String phone = pp.getNormStr("phone");
				if(phone!=null){
					mu.set("phone", phone);
					desc.append("电话:").append(phone).append(";");
				}
				//头像
				String head_img = this.getPara("head_img");
				if(head_img!=null){
					mu.set("head_img", head_img);
					desc.append("头像:").append(head_img).append(";");
				}
				
				//工号
				String job_number = pp.getNormStr("job_number");
				if(job_number!=null){
					mu.set("job_number", job_number);
					desc.append("工号:").append(job_number).append(";");
				}
				//部门
				String org_id = pp.getNormStr("org_id");
				if(org_id!=null){
					mu.set("dep_id", org_id);
					desc.append("部门:").append(org_id).append(";");
					//获取所在公司
					String com_id = getComId(org_id);
					mu.set("com_id", com_id);
				}
				
				
				//数据分流设置
				String dataRange = pp.getNormStr("data_range");
				if(dataRange!=null){
					mu.set("data_range", dataRange);
					desc.append("数据分流设置:").append(dataRange).append(";");
				}
				
				//openid绑定状态
				String bind_status = pp.getNormStr("bind_status");
				if(bind_status!=null){
					mu.set("bind_status", bind_status);
					if(bind_status.equals("2")){
						mu.set("openid", "");
						desc.append("openid绑定状态:").append(bind_status).append(";");
					}
				}
				
				//英文名
				String english_name = pp.getAllStr("english_name");
				if(english_name!=null){
					mu.set("english_name", english_name);
					desc.append("英文名:").append(english_name).append(";");
				}
				
				//qq
				String qq = pp.getNormStr("qq");
				if(qq!=null){
					mu.set("qq", qq);
					desc.append("qq:").append(qq).append(";");
				}
				
				//电子邮箱
				String email =getPara("email");
				if(email!=null){
					mu.set("email", email);
					desc.append("电子邮箱:").append(email).append(";");
				}
				
				//所在地区
				String province_id =getPara("province_id");
				if(province_id!=null){
					mu.set("province_id", province_id);
					desc.append("所在地区:").append(province_id).append(";");
				}
				
				//选择市
				String city_id =getPara("city_id");
				if(city_id!=null){
					mu.set("city_id", city_id);
					desc.append("所在市:").append(city_id).append(";");
				}
				//坐席号
				String seat_number = pp.getNormStr("seat_number");
				if(seat_number!=null){
					mu.set("seat_number", seat_number);
				}
				//个人简介
				String introduce = pp.getAllStr("introduce");
				if(introduce!=null){
					mu.set("introduce", introduce);
					desc.append("个人简介:").append(introduce).append(";");
				}

				//修改创建时间
				LoginUserInfo lui = this.getSessionAttr("LoginUserInfo");
				if(lui!=null){
					mu.set("update_user",lui.getUserName());
				}
				mu.set("update_time",StringUtil.yyyymmddhmsTime(new Date()));
				
				mu.update();
				OperateLogger.log(this, "用户管理", "修改", desc.toString());

				//调用crm接口修改用户信息
				if(mu.getCrmId()!=null && !mu.getCrmId().isEmpty() && !mu.getLoginName().isEmpty()
						&& !mu.getEnglishName().isEmpty()
						&& !mu.getUserName().isEmpty()
						&& !mu.getPhone().isEmpty()
						&& !mu.getEmail().isEmpty()){
					Map<String,String> params = new HashMap<String,String>();
					params.put("serviceName", "update");
					params.put("objectApiName", "ccuser");
					params.put("binding", CrmUtils.getBinding());
					
					String Url = PropKit.get("CRM_API_URL");
					JSONArray data = new JSONArray();
					JSONObject userObj = new JSONObject();
					userObj.put("id", mu.getCrmId());
					userObj.put("name", mu.getUserName());
					userObj.put("ename", mu.getEnglishName());
					userObj.put("mobile", mu.getPhone());
					userObj.put("email", mu.getEmail());
//					userObj.put("title", "");
//					userObj.put("qq", "");
//					userObj.put("wxzh", "");
//					userObj.put("region", "");
//					userObj.put("beizhu", "");
//					userObj.put("createdate", StringUtil.yyyymmddhmsTime(user.getCreateTime()));
					
					if(!mu.getJobNumber().isEmpty())
						userObj.put("employeenum", mu.getJobNumber());
					data.add(userObj);
					
					params.put("data", data.toString());
					
					try {
						logger.info("crm before:"+JsonKit.toJson(params));
						String response = Client.doPost(Url, params);
						logger.info("crm after:"+response);
						JSONObject resObj = JSONObject.fromObject(response);
						if(resObj.getBoolean("result") && resObj.getString("returnCode").equals("1")){
							logger.info("同步crm数据成功!");
						}
						else{
							logger.info("同步crm数据失败!");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				/***
				 * 跳转到跳出时列表页地址
				 */
				
				String fromurl = getPara("fromurl");
				this.redirect(fromurl);
			}
			else
			{
				User mu = User.dao.findById(pp.getId());
				if(mu!=null){
					//加载对象数据
					this.setAttr("user", mu);
					this.setAttr("operation", "编辑");
										
					StringBuffer bf = new StringBuffer();
		            
		            //性别
		            bf.setLength(0);
		            bf.append("<select class=\"m-wrap span6\" name=\"gender\">");	
		            if(mu.getGender().equals("MALE"))
		            		bf.append("<option value=\"MALE\" selected>男</option>");	
		            else
		            		bf.append("<option value=\"MALE\">男</option>");	
		            if(mu.getGender().equals("FEMALE"))
		            		bf.append("<option value=\"FEMALE\" selected>女</option>");	
		            else
		            	bf.append("<option value=\"FEMALE\">女</option>");
		            bf.append("</select>");
		            this.setAttr("gender", bf.toString());
//		            
		            //获取中国的省份
		            Dictionary provinces = DictionaryManager.getInstance().getAllSubDictionaries("000300020001", 1);
		            this.setAttr("provinces",provinces);
					
					//获取城市列表
		            if(mu.getProvinceId()!=null && !mu.getProvinceId().isEmpty()){
		            		Dictionary cities = DictionaryManager.getInstance().getAllSubDictionaries(mu.getProvinceId(), 1);
			            this.setAttr("cities",cities);
		            }
					
					//关联部门树
					Orgnization mo = Orgnization.dao.findFirst("select b.* from console_user as a,console_orgnization as b where a.dep_id=b.org_id and a.user_id='"+pp.getId()+"'");
					String selectCode = "";
					if(mo!=null){
						selectCode = mo.getOrgCode();
					}
					String tree=loadTree(selectCode);
					this.setAttr("tree", tree);
					
					Dictionary dict = DictionaryManager.getInstance().getAllSubDictionaries("0015", 1);
		            setAttr("bind_status",dict);
		            
					Dictionary dataRange = DictionaryManager.getInstance().getAllSubDictionaries("0012", 1);
					setAttr("dataRange",dataRange);
					
					this.renderJsp("/views/user.jsp");
				}
				else{
					this.renderText("无此数据!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
				}
			}
		}
		else
			this.renderText("参数错误!<a href='javascript:window.history.back()'>返回</a>", ContentType.HTML);
	}
	
	private String getComId(String org_id){
		Orgnization mo = Orgnization.dao.findById(org_id);
		String code = mo.getOrgCode();
		String codes = StringUtil.getParentCodes(code);
		mo = Orgnization.dao.findFirst("select * from console_orgnization where delete_flag='0' and org_code in ("+codes+") and org_type='ORG_TYPE_COMPANY' order by org_code desc");
		if(mo!=null)
			return String.valueOf(mo.getOrgId());
		else
			return "0";
	}
	
	///////////////////////树状图加载////////////////////////////////
	private String loadTree(){
		return loadTree("");
	}
	 //获取中国的省份
//	private List<Province> getProvinceInChina(){
//		List<Country> listCountrys = Country.dao.find("SELECT  * FROM crm_address_country WHERE CountryName = '中国'");
//		if(listCountrys.size()>0){
//			//中国的CountryId
//			String countryId = listCountrys.get(0).getCountryId();
//			String sql="SELECT  * FROM crm_address_province WHERE CountryId = "+"'"+countryId+"'";
//			List<Province> provinces = Province.dao.find(sql); 
//			return provinces;
//		} 
//		return null;
//	}
	
	private String loadTree(String code){
		List<Record> records = Db.find("select * from console_orgnization where delete_flag='0' order by org_code asc");
		NodeTree ct = new NodeTree("orgTree","所属部门",10);
		
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
	
	@Clear
	public void cities(){
		Map<String,Object> rst = new HashMap<String,Object>();
		String pcode =  this.getPara("pcode");
		if(pcode!=null && StringUtil.isNumString(pcode)){
			StringBuffer bf = new StringBuffer();
			Dictionary cities = DictionaryManager.getInstance().getAllSubDictionaries(pcode, 1);		
			for(Dictionary d:cities.getSubDictionaries()){
				bf.append("<option value=\"")
				.append(d.getDictCode()).append("\">")
				.append(d.getName()).append("</option>");
			}
			
			rst.put("status", 0);
			rst.put("msg", "ok");
			rst.put("cities", bf.toString());
			this.renderJson(rst);
		}
		else{
			rst.put("status", 1);
			rst.put("msg", "参数错误");
			this.renderJson(rst);
		}
	}
}
