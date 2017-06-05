package cn.pacificimmi.common.routes;

import com.jfinal.config.Routes;

import cn.pacificimmi.ucenter.controllers.ChooseStationsController;
import cn.pacificimmi.ucenter.controllers.DictionaryController;
import cn.pacificimmi.ucenter.controllers.LogsController;
import cn.pacificimmi.ucenter.controllers.OrgnizationController;
import cn.pacificimmi.ucenter.controllers.ResourcesController;
import cn.pacificimmi.ucenter.controllers.RoleController;
import cn.pacificimmi.ucenter.controllers.RolesController;
import cn.pacificimmi.ucenter.controllers.StationController;
import cn.pacificimmi.ucenter.controllers.StationsController;
import cn.pacificimmi.ucenter.controllers.SystemController;
import cn.pacificimmi.ucenter.controllers.SystemsController;
import cn.pacificimmi.ucenter.controllers.UserController;
import cn.pacificimmi.ucenter.controllers.UserStationsController;
import cn.pacificimmi.ucenter.controllers.UsersController;

public class UCenterRoutes extends Routes {

	@Override
	public void config() {
		add("/ucenter/orgnizations",OrgnizationController.class);
		add("/ucenter/dictionary",DictionaryController.class);
		add("/ucenter/users/choose_stations",ChooseStationsController.class);
		add("/ucenter/users/user_stations",UserStationsController.class);
		add("/ucenter/users/user",UserController.class);
		add("/ucenter/users",UsersController.class);
		add("/ucenter/resources",ResourcesController.class);
		add("/ucenter/roles",RolesController.class);
		add("/ucenter/roles/role",RoleController.class);
		add("/ucenter/stations/station",StationController.class);
		add("/ucenter/stations",StationsController.class);
		add("/ucenter/systems",SystemsController.class);
		add("/ucenter/systems/system",SystemController.class);
		add("/ucenter/logs",LogsController.class);
	}
/*	/ucenter/users/user*/
}
