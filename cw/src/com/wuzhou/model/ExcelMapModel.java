package com.wuzhou.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
/**
 * excel上传后的名称映射关系
 * @author wanghonghui
 *
 */
public class ExcelMapModel extends Model<ExcelMapModel> {
	public static ExcelMapModel dao = new ExcelMapModel();
	/**
	 * 添加映射关系
	 * @param model
	 * @return
	 */
	public boolean addExcelMap(ExcelMapModel model) {
		return model.save();
	}
	/**
	 * type=0 查询全部
	 * @return
	 */
	public List<ExcelMapModel> getExcelList(int type) {
		if(type==0) {
			return find("select * from excel_map order by id desc");
		} else {
			return find("select * from excel_map where type = ? order by id desc", type);
		}
	}
	
	/**
	 * 删除
	 * @param serverName
	 * @return
	 */
	public int deleteExcel(String serverName) {
		return Db.update("delete from excel_map where server_excel_name  = ?", serverName);
	}
}
