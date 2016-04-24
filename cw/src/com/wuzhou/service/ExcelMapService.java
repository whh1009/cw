package com.wuzhou.service;

import java.util.List;

import com.wuzhou.model.ExcelMapModel;

public class ExcelMapService {

	/**
	 * 添加映射关系
	 * @param uploadExcelName
	 * @param serverExcelName
	 * @return
	 */
	public boolean addExcelMap(String uploadExcelName, String serverExcelName) {
		ExcelMapModel model = new ExcelMapModel();
		model.set("upload_excel_name", uploadExcelName);
		model.set("server_excel_name", serverExcelName);
		return ExcelMapModel.dao.addExcelMap(model);
	}
	
	public List<ExcelMapModel> getExcelList() {
		return ExcelMapModel.dao.getExcelList();
	}
}
