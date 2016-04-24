package com.wuzhou.interceptor;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class LoginInterceptor implements Interceptor {
	Logger logger = Logger.getLogger(LoginInterceptor.class);
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		Object obj = controller.getSessionAttr("USER_SESSION");
		if(obj==null) {
			logger.warn(inv.getControllerKey()+"==被拦截");
			controller.redirect("/Login.jsp");
		} else {
			inv.invoke();
		}
	}
}
