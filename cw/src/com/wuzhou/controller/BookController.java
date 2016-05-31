package com.wuzhou.controller;

import org.apache.log4j.Logger;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.wuzhou.bean.BookBaseBean;
import com.wuzhou.service.BookService;
import com.wuzhou.service.ExcelMapService;
import com.wuzhou.service.UserService;

/**
 * 
 * @author wanghonghui
 *
 */
public class BookController extends Controller {
	Logger log = Logger.getLogger(BookController.class);
	UserService userService = Enhancer.enhance(UserService.class);
	BookService service = Enhancer.enhance(BookService.class);
	ExcelMapService excelMapService = Enhancer.enhance(ExcelMapService.class);
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
		renderJson(excelMapService.getExcelList(1));
	}
	
	/**
	 * 图书价格汇总
	 */
	public void bookPriceSummary() {
		render("/cw/BookPriceSummary.jsp");
	}
	////
	public void getBookSaleByPlatform() {
		int pageNumber = getParaToInt("page", 1);
		renderJson(service.getBookSaleByPlatform(pageNumber));
	}
	
	/**
	 * 被“删除”的图书
	 */
	public void bookDelPage() {
		setAttr("userJson", JsonKit.toJson(userService.getUserList()));
		render("/cw/DelBookList.jsp");
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
//		int bookId = getParaToInt("bookId", 0);
//		if(bookId==0) {
//			renderJson("0");
//			return;
//		}
//		try {
//			if(service.updateBookById(bookId, 1)) {
//				renderJson("1");
//			} else {
//				renderJson("2");
//			}
//		} catch(Exception ex) {
//			renderJson("-1");
//			ex.printStackTrace();
//			log.error(ex);
//		}
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
	 * 图书价格列表
	 */
	public void bookPriceList() {
		render("/cw/BookPriceList.jsp");
	}
	
	/**
	 * 图书月份售价列表
	 */
	public void getBookPriceList() {
		String mySearchSql = getPara("mySearchSql", "");
		int pageNumber = getParaToInt("page", 1);
		try {
			renderJson(service.getBookPiceList(pageNumber, mySearchSql));
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
	public void getDistinctSaleTime() {
		try {
			renderJson(service.getDistinctSaleTime());
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
	public void getDistinctPlatform() {
		try {
			renderJson(service.getDistinctPlatform());
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
	public void getBookPriceCount() {
		String mySearchSql = getPara("mySearchSql");
		try {
			renderJson(service.getBookPriceCount(mySearchSql));
		} catch(Exception ex) {
			renderJson("-1");
			ex.printStackTrace();
			log.error(ex);
		}
	}
	
}
