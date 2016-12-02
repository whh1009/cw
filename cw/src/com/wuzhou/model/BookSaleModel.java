package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 电子书收入
 * 
 * @author wanghonghui
 * 
 */
public class BookSaleModel extends Model<BookSaleModel> {
	public static final BookSaleModel dao = new BookSaleModel();

	/**
	 * 批量导入
	 * 
	 * @param sqlList
	 * @return
	 */
	public int[] batchImport(List<String> sqlList) {
		return Db.batch(sqlList, 100);
	}

	public Page<Record> getBookSaleList(int pageNumber, String mySearchSql) {
		return Db.paginate(pageNumber, 20, "SELECT *", "FROM book_sale WHERE 1=1 " + mySearchSql);
	}

	public List<BookSaleModel> getDistinctSaleTime() {
		return this.find("SELECT DISTINCT(sale_time) from book_sale");
	}
	
	public List<BookSaleModel> getDistinctPlatform() {
		return this.find("SELECT DISTINCT(platform) from book_sale");
	}
	
	public Record getBookPriceCount(String mySearchSql) {
		return Db.findFirst("SELECT TRUNCATE(SUM((b.ad_price+b.epub_price+b.pdf_price+b.yz_price+b.ext_price1+b.ext_price2+b.ext_price3+b.ext_price4+b.ext_price5)/s.sale_count),2) AS cb, TRUNCATE(SUM(s.sale_rmb * b.author_royalty / 100), 2) AS royalty, SUM(s.sale_count) AS scount, TRUNCATE(SUM(s.sale_rmb), 2) AS srmb FROM book_base b, cw_sale s WHERE b.book_isbn = s.book_num AND b.book_isbn IS NOT NULL AND b.book_isbn != '' AND s.book_num IS NOT NULL AND b.book_status = 1" + mySearchSql+" ");
	}
	
	public void deleteByServerName(String serverName) {
		Db.update("DELETE FROM book_sale WHERE server_excel_name = ?", serverName);
	}
}
