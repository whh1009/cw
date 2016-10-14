package com.wuzhou.service;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.wuzhou.model.ExcelMapModel;
import com.wuzhou.tool.TimeTools;

/**
 * 导入的excel映射关系
 * @author wanghonghui
 *
 */
public class ExcelMapService {

	/**
	 * 添加映射关系
	 * @param uploadExcelName
	 * @param serverExcelName
	 * @return
	 */
	public boolean addExcelMap(String uploadExcelName, String serverExcelName, int type) {
		ExcelMapModel model = new ExcelMapModel();
		model.set("upload_excel_name", uploadExcelName);
		model.set("server_excel_name", serverExcelName);
		model.set("type", type);
		model.set("add_time", TimeTools.timeFormat(new Date().getTime(), ""));
		return ExcelMapModel.dao.addExcelMap(model);
	}
	
	/**
	 * 查询所有的excel
	 * type==0 查询全部
	 * 否则根据type查询
	 * @param type
	 * @return
	 */
	public List<ExcelMapModel> getExcelList(int type) {
		return ExcelMapModel.dao.getExcelList(type);
	}
	
	public Page<ExcelMapModel> getExcelByPage(int page) {
		return ExcelMapModel.dao.getExcelByPage(page);
	}
	
	/**
	 * 删除excel
	 * @param serverName
	 * @return
	 */
	public int deleteExcel(String serverName) {
		return ExcelMapModel.dao.deleteExcel(serverName);
	}
}
