package cn.pacificimmi.common.routes;

import com.jfinal.config.Routes;

import cn.pacificimmi.common.controllers.IndexController;

public class IndexRoute extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		add("/",IndexController.class);
	}

}
