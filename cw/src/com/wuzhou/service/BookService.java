package com.wuzhou.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wuzhou.model.BookBaseModel;
import com.wuzhou.model.BookSaleModel;
import com.wuzhou.tool.POITools;

public class BookService {

	public BookBaseModel getBookInfo(int id) {
		return BookBaseModel.dao.getBookInfo(id);
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
	
	/**
	 * 计算求和
	 * @param mySearchSql
	 * @return
	 */
	public Object getSummary(String mySearchSql) {
		String sql = "select sum(epub_price) epub, sum(pdf_price) pdf, sum(ad_price) ad, sum(yz_price) yz,sum(ext_price1) ext1,sum(ext_price2) ext2,sum(ext_price3) ext3,sum(ext_price4) ext4,sum(ext_price5) ext5,sum(epub_price)+sum(pdf_price)+sum(ad_price)+sum(yz_price)+sum(ext_price1)+sum(ext_price2)+sum(ext_price3)+sum(ext_price4)+sum(ext_price5) as mytotal from book_base where "+mySearchSql;
		return BookBaseModel.dao.getSumary(sql);
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

	public Page<Record> getBookPiceList(int pageNumber, String mySearchSql) {
		mySearchSql += " order by id desc";
		return BookSaleModel.dao.getBookSaleList(pageNumber, mySearchSql);
	}
	
	public List<BookSaleModel> getDistinctSaleTime() {
		return BookSaleModel.dao.getDistinctSaleTime();
	}
	
	public List<BookSaleModel> getDistinctPlatform() {
		return BookSaleModel.dao.getDistinctPlatform();
	}
	
	public Record getBookPriceCount(String mySearchSql) {
		mySearchSql = mySearchSql.replace("-", "");
		return BookSaleModel.dao.getBookPriceCount(mySearchSql);
	}
	
	///////////////////////////////////////////////////////////////
	public Page<Record> getBookSaleByPlatform(int pageNumber, String mySearchSql) {
		return BookBaseModel.dao.getBookSaleByPlatform(pageNumber, mySearchSql);
	}
	
	
	public String createBookPriceSummary(String mySearchSql) throws Exception {
		List<Record> list = BookBaseModel.dao.getBookSaleByPlatform(mySearchSql);
		if(list==null||list.isEmpty()) {
			return "";
		} else {
			String excelName = PathKit.getWebRootPath()+File.separator+"temp"+File.separator + "bookPriceSummaryTemp.xlsx";
			Workbook wb = POITools.getWorkbook(excelName);
			Sheet sheet= POITools.getSheet(0, wb);
			short rowNumber = 2;
			for(Record record : list) {
				Row row = POITools.createRow(sheet, rowNumber);
				POITools.setCellValue(row.createCell(0), record.getStr("book_isbn"), null);
				POITools.setCellValue(row.createCell(1), record.getStr("book_name"), null);
				POITools.setCellValue(row.createCell(2), record.getStr("book_author"), null);
				POITools.setCellValue(row.createCell(3), record.getStr("book_lang"), null);
				POITools.setCellValue(row.createCell(4), record.getStr("trans_company"), null);
				POITools.setCellValue(row.createCell(5), record.getStr("is_self"), null);
				POITools.setCellValue(row.createCell(6), record.getStr("stime"), null);
				POITools.setCellValue(row.createCell(7), record.getFloat("author_royalty")/100.00 * record.getBigDecimal("srmb").doubleValue(), null);
				POITools.setCellValue(row.createCell(8), record.getBigDecimal("pdf_price").add(record.getBigDecimal("epub_price")).add(record.getBigDecimal("ad_price")).add(record.getBigDecimal("yz_price").add(record.getBigDecimal("ext_price1")).add(record.getBigDecimal("ext_price2")).add(record.getBigDecimal("ext_price3")).add(record.getBigDecimal("ext_price4")).add(record.getBigDecimal("ext_price5"))), null);
				POITools.setCellValue(row.createCell(9), record.getBigDecimal("srmb").doubleValue(), null);
				POITools.setCellValue(row.createCell(10), record.getBigDecimal("sdollar").doubleValue(), null);
				POITools.setCellValue(row.createCell(11), record.getBigDecimal("scount").doubleValue(), null);
				POITools.setCellValue(row.createCell(12), 
						record.getBigDecimal("srmb").doubleValue()-
						record.getFloat("author_royalty")/100.00 * record.getBigDecimal("srmb").doubleValue() - 
						record.getBigDecimal("pdf_price").add(record.getBigDecimal("epub_price")).add(record.getBigDecimal("ad_price")).add(record.getBigDecimal("yz_price").add(record.getBigDecimal("ext_price1")).add(record.getBigDecimal("ext_price2")).add(record.getBigDecimal("ext_price3")).add(record.getBigDecimal("ext_price4")).add(record.getBigDecimal("ext_price5"))).doubleValue(),  
								null);
				rowNumber++;
			}
			excelName =  UUID.randomUUID().toString().replace("-", "")+".xlsx";
			POITools.saveAsWorkbook(wb, PathKit.getWebRootPath()+File.separator+"out"+File.separator + excelName);
			return excelName;
		}
	}
}
