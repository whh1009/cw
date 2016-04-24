package com.wuzhou.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.wuzhou.model.BookBaseModel;
import com.wuzhou.model.UserModel;
import com.wuzhou.tool.FileUtil;
import com.wuzhou.tool.POITools;
import com.wuzhou.tool.StringUtil;

public class IndexService {
	
	
	/**
	 * 图书书号入库
	 * @param bookName
	 * @param bookNum
	 * @return
	 */
	public int saveBookNum(String bookName, String bookNum, UserModel user) {
		bookNum = StringUtil.ignoreComma(bookNum);
		List<String> sqlList = new ArrayList<String>();
		Date date = new Date();
		for(String bn : bookNum.split(",")) {
			String sql = "insert into booknum (book_name, book_num, add_time, uid) values ('"+bookName+"', '"+bn+"', '"+StringUtil.dataFormat(date, "yyyy-MM-dd hh:mm:ss")+"', "+user.getLong("user_id")+");";
			sqlList.add(sql);
		}
		int [] result = BookBaseModel.dao.saveBookNum(sqlList);
		return result.length;
	}
	
	/**
	 * 获取图书列表
	 * 
	 * @return
	 */
	public Page<BookBaseModel> getBookList(int pageNumber, String mySearchSql, int bookStatus) {
		mySearchSql += " and book_status=1 order by id desc";
		return BookBaseModel.dao.getBookList(pageNumber, mySearchSql);
	}
	
	public Object getSummary(String mySearchSql) {
		String sql = "select sum(epub_price) epub, sum(pdf_price) pdf, sum(ad_price) ad, sum(yz_price) yz,sum(ext_price1) ext1,sum(ext_price2) ext2,sum(ext_price3) ext3,sum(ext_price4) ext4,sum(ext_price5) ext5,sum(epub_price)+sum(pdf_price)+sum(ad_price)+sum(yz_price)+sum(ext_price1)+sum(ext_price2)+sum(ext_price3)+sum(ext_price4)+sum(ext_price5) as mytotal from book_base where "+mySearchSql;
		return BookBaseModel.dao.getSumary(sql);
	}
	
	
	
	/**
	 * 解析excel
	 * @param excelPath
	 * @return
	 * @throws Exception
	 */
	public int parserExcel(String excelPath) throws Exception {
		String name = new File(excelPath).getName().replace(".xlsx", "");
		List<String> sqlList = new ArrayList<String>();
		Workbook wb = POITools.getWorkbook(excelPath);
		Sheet sheet = POITools.getSheet("Sheet1", wb); //读Sheet1表
		String transTime = ""; //
		String transCompany = "";
		String isSelf = "";
		String isbn = "";
		String bookName = "";
		String wz = "";
		String zb = "";
		String epubPrice ="0.0";
		String pdfPrice = "0.0";
		String adPrice = "0.0";
		String yzPrice = "0.0";
		String extPrice1="0.0";
		String extPrice2="0.0";
		String extPrice3="0.0";
		String extPrice4="0.0";
		String extPrice5="0.0";
		Date date= new Date();
		for(int rowNumber = 1; rowNumber<POITools.getRowCount(sheet);rowNumber++) { //第一行标题行，从第二行开始
			Row row = POITools.getRow(sheet, rowNumber);
			transTime = POITools.getCellValue(row.getCell(0)); //转码时间
			transCompany = POITools.getCellValue(row.getCell(1)); //转码公司
			isSelf = "".equals(POITools.getCellValue(row.getCell(2)))?"五洲":POITools.getCellValue(row.getCell(2)); //本社或第三方
			isbn = POITools.getCellValue(row.getCell(3)).replace("-", "").replace(" ","").replace(".00","").trim(); //ISBN
			bookName = POITools.getCellValue(row.getCell(4)).replace("'", "’").replaceAll("[\r\n]+", "").trim(); //书名
			wz = POITools.getCellValue(row.getCell(5));
			zb = POITools.getCellValue(row.getCell(6));
			epubPrice = formatFloat(POITools.getCellValue(row.getCell(7)));
			pdfPrice = formatFloat(POITools.getCellValue(row.getCell(8)));
			adPrice = formatFloat(POITools.getCellValue(row.getCell(9)));
			yzPrice = formatFloat(POITools.getCellValue(row.getCell(10)));
			extPrice1 = formatFloat(POITools.getCellValue(row.getCell(11)));
			extPrice2 = formatFloat(POITools.getCellValue(row.getCell(12)));
			extPrice3 = formatFloat(POITools.getCellValue(row.getCell(13)));
			extPrice4 = formatFloat(POITools.getCellValue(row.getCell(14)));
			extPrice5 = formatFloat(POITools.getCellValue(row.getCell(15)));
			sqlList.add("insert into book_base (server_excel_name, is_self, book_isbn, book_name, book_lang, book_author, epub_price, pdf_price, ad_price, author_royalty, insert_time, trans_time, trans_company, yz_price, ext_price1, ext_price2, ext_price3, ext_price4, ext_price5) values" +
					"('"+name+"', '"+isSelf+"', '"+isbn+"', '"+bookName+"', '"+wz+"', '"+zb+"', "+epubPrice+", "+pdfPrice+", "+adPrice+", "+0.0+", '"+StringUtil.dataFormat(date, "yyyy-MM-dd")+"', '"+transTime+"', '"+transCompany+"', "+yzPrice+", "+extPrice1+", "+extPrice2+", "+extPrice3+", "+extPrice4+", "+extPrice5+")");
		}
		wb.close();
		FileUtil.deleteSubFiles(new File(excelPath).getParentFile());
		if(sqlList==null||sqlList.isEmpty()) return 0;
		return BookBaseModel.dao.addExcelData(sqlList).length;
	}
	
	private String formatFloat(String str) {
		if("".equals(str)||null==str) {
			return "0.0";
		} else {
			return str;
		}
	}
	
	
	/**
	 * 更新图书状态
	 * @param bookId
	 * @return
	 */
	public boolean updateBookById(int bookId, int bookStatus) {
		return BookBaseModel.dao.updateBookById(bookId, bookStatus);
	}
	
	/**
	 * 删除图书
	 * @param bookId
	 * @return
	 */
	public boolean deleteBookById(int bookId) {
		return BookBaseModel.dao.deleteBookById(bookId);
	}
	
	/**
	 * 添加excel中的数据到数据库
	 * @param xml
	 * @return
	 */
	public int addExcelData(String xml, UserModel user) {
		Document doc = Jsoup.parse(xml, "",  Parser.xmlParser());
		Elements eles = doc.select("item");
		List<String> sqlList = new ArrayList<String>();
		String sql = "";
		String time = StringUtil.dataFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
		for(int i = 0; i<eles.size(); i++) {
			sql = "insert into booknum (book_name, book_num, add_time, book_status, uid) values ('"+eles.get(i).attr("bname")+"', '"+eles.get(i).attr("bnum")+"', '"+time+"', 1, "+user.getLong("user_id")+")";
			sqlList.add(sql);
		}
		if(sqlList.isEmpty()||sqlList.size()<1) {
			return 0;
		} else {
			int []result = BookBaseModel.dao.addExcelData(sqlList);
			return getResultNum(result);
		}
	}
	
	private int getResultNum(int [] result) {
		int r = 0;
		for(int i = 0; i< result.length; i++) {
			if(result[i]==1) {
				r++;
			}
		}
		return r;
	}
	
	public String exportDataByBookName(String searchCondition, String searchVal, int userId, String excelTempPath) throws EncryptedDocumentException, InvalidFormatException, IOException {
		String fileName = "";
		String sql = "select b.*, u.nick_name from booknum b, wz_user u where b.uid = u.user_id and b.book_status= 1";
		if(!"".equals(searchCondition)) {
			sql += " and b." + searchCondition+" like '%"+searchVal+"%' ";
		}
		if(userId!=0) {
			sql += " and b.uid = "+userId;
		}
		sql += " order by b.id desc";
		List<BookBaseModel> bookList = BookBaseModel.dao.getBookListByBookName(sql);
		if(bookList!=null&&!bookList.isEmpty()) {
			Workbook wb = POITools.getWorkbook(excelTempPath);
			Sheet sheet = POITools.getSheet(0, wb);
			for(int i = 0; i<bookList.size(); i++) {
				Row row = POITools.createRow(sheet, (short)(i+1));
				String book_name = bookList.get(i).getStr("book_name");
				String book_num = bookList.get(i).getStr("book_num");
				String add_time = StringUtil.dataFormat(bookList.get(i).getDate("add_time"), "yyyy-MM-ddd hh:mm:ss");
				POITools.setCellValue(row.createCell(0), book_name, null);
				POITools.setCellValue(row.createCell(1), book_num, null);
				POITools.setCellValue(row.createCell(2), add_time, null);
			}
			fileName = new Date().getTime()+".xlsx";
			POITools.saveAsWorkbook(wb, PathKit.getWebRootPath()+File.separator+"out"+File.separator+fileName);
		}
		return fileName;
	}
	
	
	public BookBaseModel getBookInfo(int id) {
		return BookBaseModel.dao.getBookInfo(id);
	}
}
