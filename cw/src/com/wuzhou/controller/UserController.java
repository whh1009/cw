package com.wuzhou.controller;

import org.apache.log4j.Logger;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.wuzhou.model.UserModel;
import com.wuzhou.service.UserService;


public class UserController extends Controller {
	Logger logger = Logger.getLogger(UserController.class);
	UserService service = Enhancer.enhance(UserService.class);
	public void loginPage() {
		render("/Login.jsp");
	}
	
	public void login() {
		String userName = getPara("userName");
		String pwd = getPara("userPassword");
		UserModel um = null;
		try{
			um = service.login(userName, pwd);
			if(um!=null) {
				if(um.getInt("status")==1) {
					setSessionAttr("USER_SESSION", um);
					renderJson("1");
				} else {
					renderJson("2");
				}
			} else {
				renderJson("0");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson("-1");
		}
	}
	
	
	/**
	 * 退出登录
	 */
	public void signout() {
		Object obj = getSessionAttr("USER_SESSION");
		if(obj!=null) {
			setSessionAttr("USER_SESSION", null);
		}
		redirect("/Login.jsp");
	}
}
