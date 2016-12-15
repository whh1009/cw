package com.wuzhou.config;

import java.io.File;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.render.ViewType;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.wuzhou.controller.BookController;
import com.wuzhou.controller.BookIncomingController;
import com.wuzhou.controller.ExcelImportController;
import com.wuzhou.controller.IndexController;
import com.wuzhou.controller.UserController;
import com.wuzhou.handler.WZHandler;
import com.wuzhou.model.BookBaseModel;
import com.wuzhou.model.BookSaleModel;
import com.wuzhou.model.CwSaleModel;
import com.wuzhou.model.ExcelMapModel;
import com.wuzhou.model.UserModel;



public class WZConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		PropKit.use("db_config.txt");
		me.setViewType(ViewType.JSP);
		me.setUploadedFileSaveDirectory("uploadFiles" + File.separator);
		me.setDevMode(true);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);
		me.add("/import", ExcelImportController.class);
		me.add("/user", UserController.class);
		me.add("/book", BookController.class);
		me.add("/incoming", BookIncomingController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		MysqlDataSource ds = new MysqlDataSource();
		ds.setUrl(PropKit.get("jdbcUrl"));
		ds.setUser(PropKit.get("user"));
		ds.setPassword(PropKit.get("password").trim());
		ActiveRecordPlugin arp = new ActiveRecordPlugin(ds);
		me.add(arp);
		
		arp.addMapping("wz_user", UserModel.class);
		arp.addMapping("book_base", BookBaseModel.class);
//		arp.addMapping("book_sale", BookSaleModel.class);
		arp.addMapping("excel_map", ExcelMapModel.class);
		arp.addMapping("cw_sale", CwSaleModel.class);
		arp.setShowSql(true);
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		
		
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
		me.add(new WZHandler());
	}

	
}
