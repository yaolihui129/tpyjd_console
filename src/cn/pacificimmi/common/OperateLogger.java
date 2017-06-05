package cn.pacificimmi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Controller;

import cn.pacificimmi.common.models.OperationLog;
import cn.pacificimmi.ucenter.models.LoginUserInfo;


public class OperateLogger {
	private static Logger logger = LoggerFactory.getLogger(OperateLogger.class);
	public static void log(Controller ctr,String optModule,String optType,String optDescription){
		LoginUserInfo lui = ctr.getSessionAttr("LoginUserInfo");
		if(lui!=null){
			String loginUser= lui.getLoginName();
			OperationLog ol = new OperationLog();
			ol.setOptModule(optModule);
			ol.setOptType(optType);
			ol.setOptUser(loginUser);
			ol.setOptDescription(optDescription);
			ol.save();
			logger.info(optModule+":"+loginUser+":"+optType+":"+optDescription);
		}
		else{
			logger.error("session已经超时！");
		}
	}
}
