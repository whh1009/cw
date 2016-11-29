package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

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
		return this.paginate(pageNumber, 20, "select * ", "from book_base where "+condition);
	}
	
//	public Page<Record> getBookPriceList(int pageNumber) {
//		return Db.paginate(pageNumber, 15, "select b.book_name,s.sale_time, s.platform, s.sale_total_price, s.sale_total_count", "from book_base b, book_sale s where b.book_status = 1 and b.book_isbn=s.book_isbn");
//	}
	
	/**
	 * 更新图书状态
	 * @param bookId
	 * @return
	 */
	public boolean updateBookById(int bookId, int bookStatus) {
		int flag = Db.update("update book_base set book_status = ? where id = ?", bookStatus, bookId);
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
	
	//////////////////////////////////////////////////////
	
	public Page<Record> getBookSaleByPlatform(int pageNumber, String condition) {
		String sql = " ";
		sql+= " FROM book_base b, book_sale s WHERE b.book_isbn = s.book_isbn AND b.book_isbn IS NOT NULL AND b.book_isbn != '' AND s.book_isbn IS NOT NULL AND b.book_status = 1 ";
		sql+=" "+condition+" ";
		sql+= " GROUP BY s.book_isbn, s.book_num";
		return Db.paginate(pageNumber, 15, "SELECT s.platform,b.*, SUM(b.author_royalty) royalty,SUM(b.ad_price) adPrice,SUM(b.epub_price) epubPrice,SUM(b.pdf_price) pdfPrice,SUM(b.yz_price) yzPrice,SUM(b.ext_price1) ext1Price,SUM(b.ext_price2) ext2Price,SUM(b.ext_price3) ext3Price,SUM(b.ext_price4) ext4Price,SUM(b.ext_price5) ext5Price,s.zkl,SUM(s.sale_count) scount,SUM(s.discount) sdis,SUM(s.sale_rmb) srmb, SUM(s.sale_dollar) sdollar,s.sale_time stime ", sql);
	}
}
