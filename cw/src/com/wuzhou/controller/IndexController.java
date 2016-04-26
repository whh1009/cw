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
	 * 书号检索列表页面
	 */
	public void bookList() {
		setAttr("searchConditionJson", JsonKit.toJson(BookBaseBean.getInstence().searchConditionMap));
		render("/cw/BookList.jsp");
	}
	
	public void bookInfo() {
		int id = getParaToInt("id", 0);
		if(id==0) {
			setAttr("bookBaseModel", "");
		} else {
			try{
				setAttr("bookBaseModel", service.getBookInfo(id));
			} catch(Exception ex) {
				setAttr("bookBaseModel", "");
				ex.printStackTrace();
				log.error(ex);
			}
		}
		render("/cw/EditBook.jsp");
	}
	
	/**
	 * 书号列表
	 */
	public void getBookList() {
		String mySearchSql = getPara("mySearchSql", "1=1");
		int pageNumber = getParaToInt("page", 1);
		try {
			renderJson(service.getBookList(pageNumber, mySearchSql, 1));
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
	public void getSummary() {
		String mySearchSql = getPara("mySearchSql", "1=1");
		try {
			renderJson(service.getSummary(mySearchSql));
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
	public void getExcelList() {
		renderJson(excelMapService.getExcelList());
	}
	
	/**
	 * 上传excel页面
	 */
	public void importBaseBookPage() {
		render("/cw/ImportBookBase.jsp");
	}
	
	/**
	 * 上传excel
	 */
	public void uploadBaseBookExcel() {
		UploadFile file = getFile("file");
		String uploadExcelName = file.getOriginalFileName();
		String serverExcelName = new Date().getTime()+".xlsx";
		String filePath = PathKit.getWebRootPath()+File.separator+"uploadFiles"+File.separator+serverExcelName;
		file.getFile().renameTo(new File(filePath));
		try{
			excelMapService.addExcelMap(uploadExcelName, serverExcelName);
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
			int out = service.parserExcel(excelPath.toString());
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
	 * 被“删除”的图书
	 */
	public void bookDelPage() {
		setAttr("userJson", JsonKit.toJson(userService.getUserList()));
		render("/cw/DelBookList.jsp");
	}
	
	
	
	/**
	 * 保存书号
	 */
	public void saveBookNum() {
		String bookName = getPara("bookName", "");
		String bookNum = getPara("bookNum", "");
		if("".equals(bookName)||"".equals(bookName)) {
			renderJson("-1");
		} else {
			try {
				renderJson(service.saveBookNum(bookName, bookNum, (UserModel)getSessionAttr("USER_SESSION")));
			} catch(Exception ex) {
				renderJson("-2");
				ex.printStackTrace();
				log.error(ex);
			}
		}
	}
	
	
	
	/**
	 * 被“删除”的图书列表
	 */
	public void getDelBookList() {
//		String searchCondition = getPara("searchCondition", "");
//		String searchVal = getPara("searchVal", "");
//		int pageNumber = getParaToInt("page", 1);
//		try {
//			renderJson(service.getBookList(pageNumber, searchCondition, searchVal, 0));
//		} catch(Exception ex) {
//			renderJson("-1");
//			ex.printStackTrace();
//		}
	}
	
	/**
	 * 假删除图书
	 */
	public void updateBookById() {
		int bookId = getParaToInt("bookId", 0);
		if(bookId==0) {
			renderJson("0");
			return;
		}
		try {
			if(service.updateBookById(bookId, 0)) {
				renderJson("1");
			} else {
				renderJson("2");
			}
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
	/**
	 * 恢复图书
	 */
	public void recoveryBookById() {
		int bookId = getParaToInt("bookId", 0);
		if(bookId==0) {
			renderJson("0");
			return;
		}
		try {
			if(service.updateBookById(bookId, 1)) {
				renderJson("1");
			} else {
				renderJson("2");
			}
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
	/**
	 * 删除图书
	 */
	public void deleteBookById() {
		int bookId = getParaToInt("bookId", 0);
		if(bookId==0) {
			renderJson("0");
			return;
		}
		try {
			if(service.deleteBookById(bookId)) {
				renderJson("1");
			} else {
				renderJson("2");
			}
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
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
