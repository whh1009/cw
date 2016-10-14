package com.wuzhou.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wuzhou.model.BookSaleModel;
import com.wuzhou.model.CwSaleModel;
import com.wuzhou.tool.StringUtil;

/**
 * 销售收入
 * @author wanghonghui
 *
 */
public class BookIncomingService {
	Logger log = Logger.getLogger(BookIncomingService.class);
	/**
	 * 根据条件分页查询
	 * @param pageNumber
	 * @param mySearchSql
	 * @return
	 */
	public Page<CwSaleModel> getBookIncomingByPage(int pageNumber, String mySearchSql) {
		return CwSaleModel.dao.getBookSaleList(pageNumber, mySearchSql);
	}
	
	public List<CwSaleModel> getDistinctSaleTime() {
		return CwSaleModel.dao.getDistinctSaleTime();
	}
	
	public List<CwSaleModel> getDistinctPlatform() {
		return CwSaleModel.dao.getDistinctPlatform();
	}
	
	public List<CwSaleModel> getDistinctBookLan() {
		return CwSaleModel.dao.getDistinctBookLan();
	}
	
	public Record getBookPriceCount(String mySearchSql) {
		return CwSaleModel.dao.getBookPriceCount(mySearchSql);
	}
	
	public String saveInComingExcel(String xml) {
		try {
			Document doc = Jsoup.parse(xml);
			String excelName = doc.select("root").first().attr("name");
			Elements eles = doc.select("item");
			List<String> sqlList = new ArrayList<String>();
			for(Element ele : eles) {
				sqlList.add("insert into cw_sale (server_excel_name, platform, book_num, book_name, book_author, book_lan, sale_count, discount, zkl, sale_rmb, sale_dollar, remark, sale_time) values "
						+ "('"+excelName+"', '"+ele.attr("platform")+"', '"+ele.attr("bookNum")+"', '"+StringUtil.formatMySqlChar(ele.attr("bookName"))+"', '"+StringUtil.formatMySqlChar(ele.attr("bookAuthor"))+"', '"+ele.attr("bookLan")+"', "+ele.attr("saleCount")+", "+ele.attr("bookPrice")+", "+ele.attr("zkl")+", "+ele.attr("bookRmb")+", "+ele.attr("bookDollar")+", '"+ele.attr("remark")+"', '"+ele.attr("saleTime").replace(".00", "")+"')");
			}
			int [] out = BookSaleModel.dao.batchImport(sqlList);
			return intArrayToString(out);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return null;
		}
	}
	
	private String intArrayToString(int[] out) {
		if(out==null||out.length==0) return "";
		else {
			String str = "";
			for(int i : out) {
				str+=i+",";
			}
			return StringUtil.ignoreComma(str);
		}
	}
}
