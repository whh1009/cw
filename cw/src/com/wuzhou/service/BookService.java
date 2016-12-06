package com.wuzhou.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wuzhou.model.BookBaseModel;
import com.wuzhou.model.BookSaleModel;

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
}
