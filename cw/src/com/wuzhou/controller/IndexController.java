package com.wuzhou.controller;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.wuzhou.Result;
import com.wuzhou.bean.BookBaseBean;
import com.wuzhou.model.UserModel;
import com.wuzhou.service.ExcelMapService;
import com.wuzhou.service.IndexService;
import com.wuzhou.service.UserService;

public class IndexController extends Controller {
	Logger log = Logger.getLogger(IndexController.class);
	IndexService service = Enhancer.enhance(IndexService.class);
	UserService userService = Enhancer.enhance(UserService.class);
	ExcelMapService excelMapService = Enhancer.enhance(ExcelMapService.class);
	Result result = new Result();
	/**
	 * 首页
	 */
	public void index() {
		render("/cw/Index.jsp");
	}
	
	
	
	
	/**
	 * 添加excel入库
	 */
	public void addExcelData() {
		String xml = getPara("xml"); 
		try {
			int out = service.addExcelData(xml, (UserModel)getSessionAttr("USER_SESSION"));
			renderJson(out);
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson(-1);
			log.error(ex);
		}
	}
	
	/**
	 * 导出excel
	 */
	public void exportDataByBookName() {
		String searchCondition = getPara("searchCondition", "");
		String searchVal = getPara("searchVal", "");
		int userId = getParaToInt("userId", 0);
		try {
			String excelTempPath = PathKit.getWebRootPath()+File.separator+"temp"+File.separator+"temp.xlsx";
			renderJson(service.exportDataByBookName(searchCondition, searchVal, userId, excelTempPath));
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson("");
			log.error(ex);
		}
	}
	
	/**
	 * 下载excel
	 */
	public void downExcel() {
		String fileName = getPara("fileName", "");
		if(!"".equals(fileName))
			renderFile("/out/"+fileName);
	}
}
