package cn.pacificimmi.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import cn.pacificimmi.ucenter.models.LoginUserInfo;

public class LoginInterCeptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller ctr = inv.getController();
		LoginUserInfo ui = ctr.getSessionAttr("LoginUserInfo");
		if(ui==null){
			ctr.redirect("/");
		}
		else{
			/***
			 * 判断是否存在访问权限
			 */
			String currentPath = ctr.getRequest().getServletPath();
			//去掉路径中'/'的结尾
			if(!currentPath.equals("/") && currentPath.endsWith("/"))
				currentPath=currentPath.substring(0, currentPath.length()-1);
			//判断路径中是否存在参数
			if(ctr.getPara(0)!=null)
				currentPath=getRealPath(currentPath);
			//当前路径是否有权访问
			if(ui.allowVisit(currentPath)){
				/**
				 * 显示菜单选项
				 */
				String menus = ui.getMenuListHtml(currentPath);
				ctr.setAttr("menus", menus);
				inv.invoke();
			}
			else{
				ctr.redirect("/");
			}
		}
	}

	public String getRealPath(String path){
		int pos = path.lastIndexOf("/");
		if(pos>0){
			path=path.substring(0, pos);
		}
		else
			path="/";
		
		return path;
	}
	
}
