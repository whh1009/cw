package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 亚马逊美国
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
		return Db.paginate(pageNumber, 20, "select *", "from book_sale where 1=1 " + mySearchSql);
	}

	public List<BookSaleModel> getDistinctSaleTime() {
		return this.find("select DISTINCT(sale_time) from book_sale");
	}
	
	public List<BookSaleModel> getDistinctPlatform() {
		return this.find("select distinct(platform) from book_sale");
	}
	
	public Record getBookPriceCount(String mySearchSql) {
		return Db.findFirst("select truncate(sum(sale_total_price), 2) as price, sum(sale_total_count) as count from book_sale where 1=1 ");
	}
	
	public void deleteByServerName(String serverName) {
		Db.update("delete from book_sale where server_excel_name = ?", serverName);
	}
}
