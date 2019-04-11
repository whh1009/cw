package com.wuzhou.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
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
	
	public String getDistinctSaleTime(Set<String> set) throws Exception{
		List<CwSaleModel> list = getDistinctSaleTime();
		if(list==null||list.isEmpty()) {
			return "";
		} else {
			List<String> ll = new ArrayList<String>();
			for(CwSaleModel model : list) {
				ll.add(model.getStr("sale_time"));
			}
			Collections.sort(ll);
			String out = "";
			for(String str : ll) {
				set.add(str.substring(0, 4));
				out+=str+",";
			}
			out = StringUtil.ignoreComma(out);
			return out;
		}
	}
	
	public List<Record> getPriceList(String year, String month, String type, String platform) {
		return CwSaleModel.dao.getPriceList(year, month, type, platform);
	}
	
	public String incomingPic(String year, String bookNum, String platform){
		String out = "";
		List<Record> list = CwSaleModel.dao.incomingPic(year, bookNum, platform);
		if(list==null||list.isEmpty()) {
			return "";
		} else {
			String [] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
			String line1="";
			String line2="";
			String line3="";
			String saleTime = "";
			BigDecimal totalCount = new BigDecimal(0);
			BigDecimal totalRmb = new BigDecimal(0.00);
			BigDecimal totalDollar = new BigDecimal(0.00);
			String myYear = "";
			for(int i = 0; i < months.length; i++) {
				myYear = year+months[i];
				boolean flag = false;
				for(Record r : list) {
					saleTime = r.getStr("saleTime");
					totalCount = r.getBigDecimal("totalCount");
					totalRmb = r.getBigDecimal("totalRmb");
					totalDollar = r.getBigDecimal("totalDollar");
					if(myYear.equals(saleTime)) {
						line1+=totalCount+",";
						line2+=totalRmb+",";
						line3+=totalDollar+",";
						flag = true;
						break;
					} else {
						flag = false;
					}
				}
				if(!flag) {
					line1+="0,";
					line2+="0,";
					line3+="0,";
				}
			}
			line1=StringUtil.ignoreComma(line1);
			line2=StringUtil.ignoreComma(line2);
			line3=StringUtil.ignoreComma(line3);
			out = line1+"@"+line2+"@"+line3;
		}
		System.out.println("========");
		System.out.println(out);
		System.out.println("========");
		return out;
	}
	
	
	
	
	
	public Page<CwSaleModel> groupByBookNum (String startTime, String endTime, String platform, int pageNumber) {
		startTime = startTime.replace("-", "");
		endTime = endTime.replace("-", "");
		String select = "SELECT platform, book_num, book_name, book_author, book_lan, SUM(sale_count) AS sale, discount, SUM(sale_rmb) AS rmb, SUM(sale_dollar) AS dollar";
		String sqlExceptSelect = " FROM cw_sale WHERE 1 = 1";
		if(StrKit.notBlank(startTime, endTime)) {
			sqlExceptSelect += " AND sale_time BETWEEN '" + startTime +"' AND '" + endTime + "' ";
		}
		if(StrKit.notBlank(platform) && !"0".equals(platform)) {
			sqlExceptSelect += " AND platform = '"+platform+"'"; 
		}
		sqlExceptSelect += " GROUP BY platform, book_num ORDER BY id DESC";
		System.out.println(sqlExceptSelect);
		return CwSaleModel.dao.paginate(pageNumber, 15, select, sqlExceptSelect);
	}
	
	public String createXsltGroupByBookNum(String startTime, String endTime, String platform) {
		String sql = "SELECT platform, book_num, book_name, book_author, book_lan, SUM(sale_count) AS sale, discount, SUM(sale_rmb) AS rmb, SUM(sale_dollar) AS dollar FROM cw_sale WHERE 1 = 1";
		startTime = startTime.replace("-", "");
		endTime = endTime.replace("-", "");
		if(StrKit.notBlank(startTime, endTime)) {
			sql += " AND sale_time BETWEEN '" + startTime +"' AND '" + endTime + "' ";
		}
		if(StrKit.notBlank(platform) && !"0".equals(platform)) {
			sql += " AND platform = '"+platform+"'"; 
		}
		sql += " GROUP BY platform, book_num ORDER BY id DESC";
		List<CwSaleModel> list = CwSaleModel.dao.find(sql);
		if(list==null||list.isEmpty()){
			return "";
		} else {
			try {
				Workbook wb = POITools.getWorkbook(PathKit.getWebRootPath()+File.separator+"temp"+File.separator+"groupByBookNumTemp.xlsx");
				Sheet sheet = POITools.getSheet(0, wb);
				for(int i = 0; i < list.size(); i++) {
					CwSaleModel model = list.get(i);
					Row row = POITools.createRow(sheet, (short)(i+1));
					POITools.setCellValue(row.createCell(0), model.getStr("platform"), null);
					POITools.setCellValue(row.createCell(1), model.getStr("book_num"), null);
					POITools.setCellValue(row.createCell(2), model.getStr("book_name"), null);
					POITools.setCellValue(row.createCell(3), model.getStr("book_author"), null);
					POITools.setCellValue(row.createCell(4), model.getStr("book_lan"), null);
					POITools.setCellValue(row.createCell(5), model.getBigDecimal("sale").toBigInteger(), null);
					POITools.setCellValue(row.createCell(6), model.getBigDecimal("rmb"), null);
					POITools.setCellValue(row.createCell(7), model.getBigDecimal("dollar"), null);
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
