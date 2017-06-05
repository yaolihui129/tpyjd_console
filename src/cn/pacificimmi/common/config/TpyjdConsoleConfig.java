/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package cn.pacificimmi.common.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

import cn.pacificimmi.common.routes.UCenterRoutes;
import cn.pacificimmi.common.controllers.FileUploadController;
import cn.pacificimmi.common.models._MappingKit;
import cn.pacificimmi.common.routes.IndexRoute;

public class TpyjdConsoleConfig extends JFinalConfig {

	/**
	 * 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
	 * @param pro 生产环境配置文件
	 * @param dev 开发环境配置文件
	 */
	public void loadProp(String pro, String dev) {
		try {
			PropKit.use(pro);
			loadPropertyFile(pro);
		}
		catch (Exception e) {
			PropKit.use(dev);
			loadPropertyFile(dev);
		}
	}

	public void configConstant(Constants me) {
		loadProp("product_config.txt", "debug_config.txt");
		
		// 加载少量必要配置，随后可用getProperty(...)获取值
		me.setViewType(ViewType.JSP);
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setError404View("/sorry.html");
		me.setError500View("/sorry.html");
	}

	public void configRoute(Routes me) {
//		me.add("/msg", WeixinMsgController.class);
		me.add(new IndexRoute());
		me.add(new UCenterRoutes());
		me.add("/fileupload", FileUploadController.class);
	}

	public void configPlugin(Plugins me) {
		DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);
		me.add(dp);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		arp.setShowSql(true);
		_MappingKit.mapping(arp);
		me.add(arp);
	}

	public void configInterceptor(Interceptors me) {
		// ApiInterceptor.setAppIdParser(new AppIdParser.DefaultParameterAppIdParser("appId")); 默认无需设置
		// MsgInterceptor.setAppIdParser(new AppIdParser.DefaultParameterAppIdParser("appId")); 默认无需设置
	}

	public void configHandler(Handlers me) {

	}

	public void afterJFinalStart() {
		
	}

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
	}
}
