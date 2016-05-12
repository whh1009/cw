package com.wuzhou.model;

import java.util.List;

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
	 * 
	 * @return
	 */
	public List<ExcelMapModel> getExcelList() {
		return find("select * from excel_map where type = 1 order by id desc");
	}
}
