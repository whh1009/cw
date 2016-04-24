package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class BookBaseModel extends Model<BookBaseModel> {
	public static final BookBaseModel dao = new BookBaseModel();
	
	public int[] saveBookNum(List<String> sqlList) {
		return Db.batch(sqlList, 500);
	}
	
	/**
	 * 图书列表
	 * @param pageNumber
	 * @param bookName
	 * @return
	 */
	public Page<BookBaseModel> getBookList(int pageNumber, String condition) {
		System.out.println("select * from book_base where "+condition);
		return this.paginate(pageNumber, 15, "select * ", "from book_base where "+condition);
	}
	
	/**
	 * 更新图书状态
	 * @param bookId
	 * @return
	 */
	public boolean updateBookById(int bookId, int bookStatus) {
		int flag = Db.update("update booknum set book_status = ? where id = ?", bookStatus, bookId);
		if(flag == 1) return true;
		else return false;
	}
	
	/**
	 * 真删除图书
	 * @param bookId
	 * @return
	 */
	public boolean deleteBookById(int bookId) {
		return deleteById(bookId);
	}
	
	/**
	 * 批量入库
	 * @param sqlList
	 * @return
	 */
	public int[] addExcelData(List<String> sqlList) {
		return Db.batch(sqlList, 50);
	}
	
	
	public List<BookBaseModel> getBookListByBookName(String sql) {
		return find(sql);
	}
	
	
	public BookBaseModel getBookInfo(int id) {
		return findFirst("select * from book_base where id = ?", id);
	}
	
	public Object getSumary(String sql) {
		return Db.queryFirst(sql);
	}
}
