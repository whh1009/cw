package com.wuzhou.controller;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.wuzhou.Result;
import com.wuzhou.config.Constraint;
import com.wuzhou.service.ExcelImportService;
import com.wuzhou.service.ExcelMapService;
import com.wuzhou.service.UserService;

/**
 * excel导入控制
 * @author wanghonghui
 *
 */
public class ExcelImportController extends Controller{
	Logger log = Logger.getLogger(ExcelImportController.class);
	
	UserService userService = Enhancer.enhance(UserService.class);
	ExcelMapService excelMapService = Enhancer.enhance(ExcelMapService.class);
	ExcelImportService service = Enhancer.enhance(ExcelImportService.class);
	
	Result result = new Result();
	
	/**
	 * 图书基本信息导入页面
	 */
	public void importBaseBookPage() {
		render("/cw/ImportBookBase.jsp");
	}
	
	/**
	 * 亚马逊美国账单导入
	 */
	public void importAmazonUSBookPage() {
		render("/cw/ImportAmazonUS.jsp");
	}
	
	/**
	 * 亚马逊中国账单导入
	 */
	public void importAmazonCNBookPage() {
		render("/cw/ImportAmazonCN.jsp");
	}
	
	/**
	 * App store苹果商店
	 */
	public void importAppStorePage() {
		render("/cw/ImportAppStore.jsp");
	}
	
	/**
	 * 上传基本图书excel
	 */
	public void uploadBaseBookExcel() {
		UploadFile file = getFile("file");
		String uploadExcelName = file.getOriginalFileName();
		String serverExcelName = new Date().getTime()+".xlsx";
		String filePath = PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+serverExcelName;
		file.getFile().renameTo(new File(filePath));
		try{
			excelMapService.addExcelMap(uploadExcelName, serverExcelName, 1);
			setSessionAttr("excelPath", filePath);
			renderJson("0");
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson(ex.getMessage());
			log.error(ex);
		}
	}
	
	/**
	 * 上传亚马逊美国excel
	 */
	public void uploadAmazonUSBookExcel() {
		UploadFile file = getFile("file");
		String uploadExcelName = file.getOriginalFileName();
		String serverExcelName = new Date().getTime()+".xlsx";
		String filePath = PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+serverExcelName;
		file.getFile().renameTo(new File(filePath));
		try{
			excelMapService.addExcelMap(uploadExcelName, serverExcelName, 2);
			setSessionAttr("excelAmazonPath", filePath);
			renderJson("0");
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson(ex.getMessage());
			log.error(ex);
		}
	}
	
	/**
	 * 上传appstore账单
	 */
	public void uploadAppStoreExcel() {
		UploadFile file = getFile("file");
		String uploadExcelName = file.getOriginalFileName();
		String serverExcelName = new Date().getTime()+".zip";
		String filePath = PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+serverExcelName;
		file.getFile().renameTo(new File(filePath));
		try{
			excelMapService.addExcelMap(uploadExcelName, serverExcelName, 2);
			setSessionAttr("appStorePath", filePath);
			renderJson("0");
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson(ex.getMessage());
			log.error(ex);
		}
	}
	
	
	/**
	 * 解析excel并返回
	 */
	public void saveBaseBookExcel() {
		Object excelPath = getSessionAttr("excelPath");
		if(excelPath==null) {
			result.setStatus(false);
			result.setMessage("未加载到EXCEL");
			return;
		}
		try{
			int out = service.parserBookBaseExcel(excelPath.toString());
			result.setStatus(true);
			result.setMessage("共入库["+out+"]本");
		} catch(Exception ex) {
			ex.printStackTrace();
			result.setStatus(false);
			result.setMessage(ex.getMessage());
			log.error(ex);
		}
		renderJson(result);
	}
	
	/**
	 * 亚马逊excel入库
	 */
	public void saveAmazonUSBookExcel() {
		Object excelPath = getSessionAttr("excelAmazonPath");
		if(excelPath==null) {
			result.setStatus(false);
			result.setMessage("未加载到EXCEL");
			return;
		}
		try{
			int out = service.parserAmazonUSExcel(excelPath.toString());
			result.setStatus(true);
			result.setMessage("共入库["+out+"]本");
		} catch(Exception ex) {
			ex.printStackTrace();
			result.setStatus(false);
			result.setMessage(ex.getMessage());
			log.error(ex);
		}
		renderJson(result);
	}
	
	/**
	 * 
	 */
	public void saveAppStoreBook() {
		Object appZipPath = getSessionAttr("appStorePath");
		if(appZipPath==null) {
			result.setStatus(false);
			result.setMessage("未加载到AppStore的文件");
			return;
		}
		try{
			int out = service.parserAppStoreZip(appZipPath.toString(), PathKit.getWebRootPath()+File.separator+Constraint.UNZIP_FOLD);
			result.setStatus(true);
			result.setMessage("共入库["+out+"]本");
		} catch(Exception ex) {
			ex.printStackTrace();
			result.setStatus(false);
			result.setMessage(ex.getMessage());
			log.error(ex);
		}
		renderJson(result);
	}
	
}
