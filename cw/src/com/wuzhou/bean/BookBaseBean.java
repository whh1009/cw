package com.wuzhou.bean;

import java.util.HashMap;
import java.util.Map;

public class BookBaseBean {
	private static BookBaseBean single;
	public static Map<String, String> importMap;
	public static Map<String, String> exportMap;
	public static Map<String, String> searchConditionMap;
	public static Map<String, String> bookPriceSummaryConditionMap;
	public BookBaseBean() {	}
	
	public static BookBaseBean getInstence() {
		if(single==null) {
			single = new BookBaseBean();
			single.initSearchCondition();
			single.initBookPriceSummarySearchCondition();
			single.initExportMap();
		}
		return single;
	}
	
	/**
	 * 初始化检索条件映射
	 */
	private void initSearchCondition() {
		searchConditionMap = new HashMap<String, String>();
		searchConditionMap.put("book_name", "书名");
		searchConditionMap.put("book_isbn", "ISBN");
		searchConditionMap.put("book_lang", "文种");
		searchConditionMap.put("book_author", "责编");
		searchConditionMap.put("trans_company", "转码公司");
	}
	
	/**
	 * 初始化图书价格汇总检索条件
	 */
	private void initBookPriceSummarySearchCondition() {
		bookPriceSummaryConditionMap = new HashMap<String, String>();
		bookPriceSummaryConditionMap.put("book_name", "书名");
		bookPriceSummaryConditionMap.put("book_isbn", "ISBN");
		bookPriceSummaryConditionMap.put("book_isbn", "销售月份"); //时间段
		bookPriceSummaryConditionMap.put("platform", "平台");
	}
	
	/**
	 * 初始化导出映射
	 */
	private void initExportMap() {
		exportMap = new HashMap<String, String>();
		exportMap.put("book_name", "书名");
		exportMap.put("book_isbn", "ISBN");
		exportMap.put("book_lang", "文种");
		exportMap.put("book_author", "责编");
		exportMap.put("pdf_price", "PDF价格");
		exportMap.put("epub_price", "EPUB价格");
		exportMap.put("ad_price", "广告费");
		exportMap.put("yz_price", "样章");
		exportMap.put("author_royalty", "作者版税");
		exportMap.put("trans_time", "转码时间");
		exportMap.put("trans_company", "转码公司");
		exportMap.put("is_self", "本社/第三方");
		exportMap.put("ext_price1", "其他费用1");
		exportMap.put("ext_price2", "其他费用2");
		exportMap.put("ext_price3", "其他费用3");
		exportMap.put("ext_price4", "其他费用4");
		exportMap.put("ext_price5", "其他费用5");
	}
	
}
