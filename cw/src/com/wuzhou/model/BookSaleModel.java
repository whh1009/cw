package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * 亚马逊美国
 * @author wanghonghui
 *
 */
public class BookSaleModel extends Model<BookSaleModel> {
	public static final BookSaleModel dao = new BookSaleModel();
	
	/**
	 * 批量导入
	 * @param sqlList
	 * @return
	 */
	public int [] batchImport(List<String> sqlList) {
		return Db.batch(sqlList, 100);
	}
}
