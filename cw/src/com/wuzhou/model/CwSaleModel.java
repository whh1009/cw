package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 财务收入
 * @author wanghonghui
 *
 */
public class CwSaleModel extends Model<CwSaleModel> {
	
	public static CwSaleModel dao = new CwSaleModel();
	
	/**
	 * 批量导入
	 * @param sqlList
	 * @return
	 */
	public int[] batchImport(List<String> sqlList) {
		return Db.batch(sqlList, 100);
	}
	
	/**
	 * 
	 * @param pageNumber
	 * @param mySearchSql
	 * @return
	 */
	public Page<CwSaleModel> getBookSaleList(int pageNumber, String mySearchSql) {
		return paginate(pageNumber, 20, "SELECT *", "FROM cw_sale WHERE 1=1 " + mySearchSql + " ORDER BY id DESC");
	}
	
	public List<CwSaleModel> getDistinctSaleTime() {
		return this.find("SELECT DISTINCT(sale_time) FROM cw_sale");
	}
	
	public List<CwSaleModel> getDistinctPlatform() {
		return this.find("SELECT DISTINCT(platform) FROM cw_sale");
	}
	
	public List<CwSaleModel> getDistinctBookLan() {
		return this.find("SELECT DISTINCT(book_lan) FROM cw_sale");
	}
	
	public Record getBookPriceCount(String mySearchSql) {
		return Db.findFirst("SELECT TRUNCATE(sum(sale_rmb), 2) AS rmb, TRUNCATE(SUM(sale_dollar), 2) AS dollar, SUM(sale_count) AS count, TRUNCATE(SUM(discount), 2) AS discount FROM cw_sale WHERE 1=1 ");
	}
	
	public void deleteByServerName(String serverName) {
		Db.update("DELETE FROM cw_sale WHERE server_excel_name = ?", serverName);
	}
	
	public List<CwSaleModel> getAll(String mySearchSql) {
		return find("SELECT * FROM cw_sale WHERE 1 = 1 " + mySearchSql + " ORDER BY id DESC");
	}
}
