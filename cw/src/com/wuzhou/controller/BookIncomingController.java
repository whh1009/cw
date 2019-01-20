package com.wuzhou.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
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
		try{
			Set<String> set = new LinkedHashSet<String>();
			String saleTime = service.getDistinctSaleTime(set);
			setAttr("saleTime", saleTime);
			setAttr("years", set.toString().replaceAll("[\\[\\]\\s]", ""));
		} catch(Exception ex) {
			setAttr("saleTime", "");
			setAttr("years", "");
		}
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
	
	/**
	 * 导出
	 */
	public void createExcelBySearch() {
		String mySearch = getPara("mySearchSql");
		try {
			String fileName = service.createExcelBySearch(mySearch);
			renderText(fileName);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			renderText("");
		}
	}
	
	public void downXlsx() {
		String fileName = getPara("n");
		renderFile("/out/"+fileName);
	}
	
	/**
	 * 排行榜页面
	 */
	public void priceList() {
		try{
			Set<String> set = new LinkedHashSet<String>();
			String saleTime = service.getDistinctSaleTime(set);
			setAttr("saleTime", saleTime);
			setAttr("years", set.toString().replaceAll("[\\[\\]\\s]", ""));
			setAttr("platform", JsonKit.toJson(service.getDistinctPlatform()));
		}catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			setAttr("years", "");
			setAttr("saleTime", "");
			setAttr("platform", "");
		}
		render("/cw/PriceList.jsp"); 
	}
	
	/**
	 * top 10 排序
	 */
	public void getPriceList() {
		String year = getPara("year");
		String month = getPara("month");
		String type = getPara("type", "销量");
		if(StrKit.isBlank(year)) {
			renderJson("");
		} else {
			String platform = getPara("platform");
			renderJson(service.getPriceList(year, month, type, platform));
		}
	}

	/**
	 * 图标分析
	 */
	public void incomingPic() {
		try{
			Set<String> set = new LinkedHashSet<String>();
			service.getDistinctSaleTime(set);
			setAttr("years", set.toString().replaceAll("[\\[\\]\\s]", ""));
			setAttr("platform", JsonKit.toJson(service.getDistinctPlatform()));
		}catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			setAttr("years", "");
			setAttr("platform", "");
		}
		render("/cw/IncomingPic.jsp");
	}
	
	
	public void getIncomingPicByYear() {
		String year = getPara("year");
		String bookNum = getPara("bookNum");
		String platform = getPara("platform");
		String out = "";
		try {
			out = service.incomingPic(year, bookNum, platform);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex);
		}
		renderText(out);
	}
	
	final static String [] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
	public void groupByBookNumPage() {
		render("/cw/GroupByBookNum.jsp");
	}
	
	
	public void groupByBookNum() {
		String startTime = getPara("startTime", "");
		String endTime = getPara("endTime", "");
		String platform = getPara("platform", "");
		int page = getParaToInt("page", 1);
		try {
			renderJson(service.groupByBookNum(startTime, endTime, platform, page));
		} catch(Exception ex) {
			ex.printStackTrace();
			renderJson("");
		}
	}
	
	public void exportGroupByBookNum() {
		String startTime = getPara("startTime", "");
		String endTime = getPara("endTime", "");
		String platform = getPara("platform", "");
		try {
			String fileName = service.createXsltGroupByBookNum(startTime, endTime, platform);
			renderText(fileName);
		} catch(Exception ex) {
			ex.printStackTrace();
			renderText("");
		}
	}
	
}
