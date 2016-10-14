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
		return paginate(pageNumber, 20, "select *", "from cw_sale where 1=1 " + mySearchSql + " order by id desc");
	}
	
	public List<CwSaleModel> getDistinctSaleTime() {
		return this.find("select DISTINCT(sale_time) from cw_sale");
	}
	
	public List<CwSaleModel> getDistinctPlatform() {
		return this.find("select distinct(platform) from cw_sale");
	}
	
	public List<CwSaleModel> getDistinctBookLan() {
		return this.find("select distinct(book_lan) from cw_sale");
	}
	
	public Record getBookPriceCount(String mySearchSql) {
		return Db.findFirst("select truncate(sum(sale_rmb), 2) as rmb, truncate(sum(sale_dollar), 2) as dollar, sum(sale_count) as count, truncate(sum(discount), 2) as discount from cw_sale where 1=1 ");
	}
	
	public void deleteByServerName(String serverName) {
		Db.update("delete from cw_sale where server_excel_name = ?", serverName);
	}
}
