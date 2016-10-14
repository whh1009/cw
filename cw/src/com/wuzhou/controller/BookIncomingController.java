package com.wuzhou.controller;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.wuzhou.model.CwSaleModel;
import com.wuzhou.service.BookIncomingService;

/**
 * 销售收入控制器
 * 
 * @author wanghonghui
 * 
 */
public class BookIncomingController extends Controller {
	Logger log = Logger.getLogger(BookIncomingController.class);
	BookIncomingService service = new BookIncomingService();

	public void index() {
		render("/cw/BookIncoming.jsp");
	}

	public void getIncomingByPage() {
		int page = getParaToInt("page", 1);
		String mySearch = getPara("mySearchSql", "");
		try {
			Page<CwSaleModel> list = service.getBookIncomingByPage(page, mySearch);
			renderJson("out", list);
		} catch (Exception ex) {
			renderJson("out", "");
			log.error(ex);
			ex.printStackTrace();
		}
	}

	public void saveInComingExcel() {
		String xml = getPara("xml");
		if (StrKit.isBlank(xml)) {
			renderText("0");
		} else {
			try {
				renderText(service.saveInComingExcel(xml));
			} catch (Exception ex) {
				renderText("-1");
				ex.printStackTrace();
				log.error(ex);
			}
		}
	}

	public void getDistinctPlatform() {
		try {
			renderJson(service.getDistinctPlatform());
		} catch (Exception ex) {
			renderJson("");
			ex.printStackTrace();
			log.error(ex);
		}
	}

	public void getDistinctSaleTime() {
		try {
			renderJson(service.getDistinctSaleTime());
		} catch (Exception ex) {
			renderJson("");
			ex.printStackTrace();
			log.error(ex);
		}
	}

	public void getDistinctBookLan() {
		try {
			renderJson(service.getDistinctBookLan());
		} catch (Exception ex) {
			renderJson("");
			ex.printStackTrace();
			log.error(ex);
		}
	}

	public void getBookPriceCount() {
		String mySearch = getPara("mySearchSql", "");
		try {
			renderJson(service.getBookPriceCount(mySearch));
		} catch (Exception ex) {
			renderJson("");
			ex.printStackTrace();
			log.error(ex);
		}
	}

}
