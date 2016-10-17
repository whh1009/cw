package com.wuzhou.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wuzhou.model.BookSaleModel;
import com.wuzhou.model.CwSaleModel;
import com.wuzhou.tool.POITools;
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
	
	
	/**
	 * 生成excel下载
	 * @param mySearchSql
	 * @return
	 */
	public String createExcelBySearch(String mySearchSql) {
		List<CwSaleModel> list = CwSaleModel.dao.getAll(mySearchSql);
		if(list==null||list.isEmpty()){
			return "";
		} else {
			try {
				Workbook wb = POITools.getWorkbook(PathKit.getWebRootPath()+File.separator+"temp"+File.separator+"incomingTemp.xlsx");
				Sheet sheet = POITools.getSheet(0, wb);
				for(int i = 0; i < list.size(); i++) {
					CwSaleModel model = list.get(i);
					Row row = POITools.createRow(sheet, (short)(i+1));
					POITools.setCellValue(row.createCell(0), model.getStr("platform"), null);
					POITools.setCellValue(row.createCell(1), model.getStr("sale_time"), null);
					POITools.setCellValue(row.createCell(2), model.getStr("book_num"), null);
					POITools.setCellValue(row.createCell(3), model.getStr("book_name"), null);
					POITools.setCellValue(row.createCell(4), model.getStr("book_author"), null);
					POITools.setCellValue(row.createCell(5), model.getStr("book_lan"), null);
					POITools.setCellValue(row.createCell(6), model.getInt("sale_count"), null);
					POITools.setCellValue(row.createCell(7), model.getBigDecimal("discount"), null);
					POITools.setCellValue(row.createCell(8), model.getBigDecimal("zkl"), null);
					POITools.setCellValue(row.createCell(9), model.getBigDecimal("sale_rmb"), null);
					POITools.setCellValue(row.createCell(10), model.getBigDecimal("sale_dollar"), null);
					POITools.setCellValue(row.createCell(11), model.getStr("remark"), null);
				}
				String fileName = new Date().getTime()+".xlsx";
				POITools.saveAsWorkbook(wb, PathKit.getWebRootPath()+File.separator+"out"+File.separator+fileName);
				return fileName;
			} catch (EncryptedDocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
}
