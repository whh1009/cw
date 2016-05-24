package com.wuzhou.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
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
	 * OverDrive
	 */
	public void importOverDrivePage() {
		render("/cw/ImportOverDrive.jsp");
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
	 * 上传各平台的销售excel
	 */
	public void uploadExcel() {
		UploadFile file = getFile("file");
		String type = getPara("t");
		String uploadExcelName = file.getOriginalFileName();
		String serverExcelName = UUID.randomUUID().toString()+".xlsx";
		String filePath = file.getSaveDirectory()+serverExcelName;
		file.getFile().renameTo(new File(filePath));
		try{
			excelMapService.addExcelMap(uploadExcelName, serverExcelName, Integer.parseInt(type));
			redirect("/import/showExcel?n="+serverExcelName+"&t="+type);
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson(ex.getMessage());
			log.error(ex);
		}
	}
	
	/**
	 * 显示excel
	 */
	public void showExcel() {
		String excelName = getPara("n", ""); 
		String type = getPara("t", ""); //第三方excel
		if("".equals(excelName)||"".equals(type)) {
			setAttr("result","");
			render("/cw/MyExcel.jsp");
		} else {
			try {
				String uploadPath = PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator;
				setAttr("name", excelName);
				if("2".equals(type)) { //亚马逊美国
					setAttr("result", JsonKit.toJson(service.parserAmazonUSExcelToList(uploadPath+excelName)));
				} else if("3".equals(type)) { //亚马逊中国
					setAttr("result", JsonKit.toJson(service.parserAmazonCNExcelToList(uploadPath+excelName)));
				} else if("4".equals(type)) { //appStore
					setAttr("result", JsonKit.toJson(service.parserAppStoreZipToList(uploadPath+excelName)));
				} else if("5".equals(type)) { //That's books
					
				} else if("6".equals(type)) { //overDrive
					
				}
			} catch(Exception ex) {
				setAttr("result", JsonKit.toJson(ex.getMessage()));
				ex.printStackTrace();
				log.error(ex);
			}
		}
		render("/cw/MyExcel.jsp");
	}
	
	
	/**
	 * 保存excel
	 */
	public void saveExcel() {
		String xml = getPara("xml", "");
		if("".equals(xml)) {
			renderJson("0");
		} else {
			renderJson(service.saveExcel(xml));
		}
	}
	
	/**
	 * 上传亚马逊美国excel
	 */
//	public void uploadAmazonUSBookExcel() {
//		UploadFile file = getFile("file");
//		String uploadExcelName = file.getOriginalFileName();
//		String serverExcelName = UUID.randomUUID().toString()+".xlsx";
////		String filePath = PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+serverExcelName;
//		String filePath = file.getSaveDirectory()+serverExcelName;
//		file.getFile().renameTo(new File(filePath));
//		try{
//			excelMapService.addExcelMap(uploadExcelName, serverExcelName, 2);
//			redirect("/import/showMyExcel?n="+serverExcelName+"&t=2");
//		} catch(Exception ex) {
//			ex.printStackTrace();
//			renderJson(ex.getMessage());
//			log.error(ex);
//		}
//	}
	
//	public void showMyExcel() {
//		String excelName = getPara("n", ""); 
//		String type = getPara("t", ""); //第三方excel
//		if("".equals(excelName)||"".equals(type)) {
//			setAttr("result","");
//			render("/cw/MyExcel.jsp");
//		} else {
//			try {
//				if("2".equals(type)) { //亚马逊美国
//					setAttr("result", JsonKit.toJson(service.parserAmazonUSExcelToList(PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+excelName)));
//				} else if("3".equals(type)) { //亚马逊中国
//					
//				} else if("4".equals(type)) { //appStore
//					
//				} else if("5".equals(type)) { //That's books
//					
//				}
//			} catch(Exception ex) {
//				setAttr("result", JsonKit.toJson(ex.getMessage()));
//				ex.printStackTrace();
//			}
//				
//		}
//		render("/cw/MyExcel.jsp");
//	}
	
	
	
	/**
	 * 上传appstore账单
	 */
//	public void uploadAppStoreExcel() {
//		UploadFile file = getFile("file");
//		String uploadExcelName = file.getOriginalFileName();
//		String serverExcelName = new Date().getTime()+".zip";
//		String filePath = PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+serverExcelName;
//		file.getFile().renameTo(new File(filePath));
//		try{
//			excelMapService.addExcelMap(uploadExcelName, serverExcelName, 4);
//			setSessionAttr("appStorePath", filePath);
//			renderJson("0");
//		} catch(Exception ex) {
//			ex.printStackTrace();
//			renderJson(ex.getMessage());
//			log.error(ex);
//		}
//	}
	
	
	
	
	/**
	 * 亚马逊excel入库
	 */
//	public void saveAmazonUSBookExcel() {
//		Object excelPath = getSessionAttr("excelAmazonPath");
//		if(excelPath==null) {
//			result.setStatus(false);
//			result.setMessage("未加载到EXCEL");
//			return;
//		}
//		try{
//			int out = service.parserAmazonUSExcel(excelPath.toString());
//			result.setStatus(true);
//			result.setMessage("共入库["+out+"]本");
//		} catch(Exception ex) {
//			ex.printStackTrace();
//			result.setStatus(false);
//			result.setMessage(ex.getMessage());
//			log.error(ex);
//		}
//		renderJson(result);
//	}
	
	/**
	 * 
	 */
//	public void saveAppStoreBook() {
//		Object appZipPath = getSessionAttr("appStorePath");
//		if(appZipPath==null) {
//			result.setStatus(false);
//			result.setMessage("未加载到AppStore的文件");
//			return;
//		}
//		try{
//			int out = service.parserAppStoreZip(appZipPath.toString(), PathKit.getWebRootPath()+File.separator+Constraint.UNZIP_FOLD);
//			result.setStatus(true);
//			result.setMessage("共入库["+out+"]本");
//		} catch(Exception ex) {
//			ex.printStackTrace();
//			result.setStatus(false);
//			result.setMessage(ex.getMessage());
//			log.error(ex);
//		}
//		renderJson(result);
//	}
	
	public void manageExcelList() {
		render("/cw/ManageExcelList.jsp");
	}
	
	public void getAllExcelList() {
		renderJson(excelMapService.getExcelList(0));
	}
	
	
	public void removeExcel() {
		String excelName= getPara("excelName");
		try {
			if("".equals(excelName)) {
				renderJson("0");
				return;
			}
			try {
				new File(PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+excelName).deleteOnExit();
			} catch(Exception e1) {
				e1.printStackTrace();
				log.error(e1);
			}
			try {
				excelMapService.deleteExcel(excelName);
			} catch(Exception e2) {
				e2.printStackTrace();
				log.error(e2);
			}
			renderJson("1");
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			renderJson("-1");
		}
	}
	
}
