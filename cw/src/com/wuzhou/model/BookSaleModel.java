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

	public List<BookSaleModel> getDisTinctSaleTime() {
		return this.find("select DISTINCT(sale_time) from book_sale");
	}
}
