package com.wuzhou.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.wuzhou.model.BookBaseModel;
import com.wuzhou.tool.FileUtil;
import com.wuzhou.tool.POITools;
import com.wuzhou.tool.StringUtil;

public class ExcelImportService {

	
	/**
	 * 解析excel
	 * @param excelPath
	 * @return
	 * @throws Exception
	 */
	public int parserBookBaseExcel(String excelPath) throws Exception {
		String name = new File(excelPath).getName().replace(".xlsx", "");
		List<String> sqlList = new ArrayList<String>();
		Workbook wb = POITools.getWorkbook(excelPath);
		Sheet sheet = POITools.getSheet("Sheet1", wb); //读Sheet1表
		String transTime = ""; //
		String transCompany = "";
		String isSelf = "";
		String isbn = "";
		String bookName = "";
		String wz = "";
		String zb = "";
		String epubPrice ="0.0";
		String pdfPrice = "0.0";
		String adPrice = "0.0";
		String yzPrice = "0.0";
		String extPrice1="0.0";
		String extPrice2="0.0";
		String extPrice3="0.0";
		String extPrice4="0.0";
		String extPrice5="0.0";
		Date date= new Date();
		for(int rowNumber = 1; rowNumber<POITools.getRowCount(sheet);rowNumber++) { //第一行标题行，从第二行开始
			Row row = POITools.getRow(sheet, rowNumber);
			transTime = POITools.getCellValue(row.getCell(0)); //转码时间
			transCompany = POITools.getCellValue(row.getCell(1)); //转码公司
			isSelf = "".equals(POITools.getCellValue(row.getCell(2)))?"五洲":POITools.getCellValue(row.getCell(2)); //本社或第三方
			isbn = POITools.getCellValue(row.getCell(3)).replace("-", "").replace(" ","").replace(".00","").trim(); //ISBN
			bookName = POITools.getCellValue(row.getCell(4)).replace("'", "’").replaceAll("[\r\n]+", "").trim(); //书名
			wz = POITools.getCellValue(row.getCell(5));
			zb = POITools.getCellValue(row.getCell(6));
			epubPrice = formatFloat(POITools.getCellValue(row.getCell(7)));
			pdfPrice = formatFloat(POITools.getCellValue(row.getCell(8)));
			adPrice = formatFloat(POITools.getCellValue(row.getCell(9)));
			yzPrice = formatFloat(POITools.getCellValue(row.getCell(10)));
			extPrice1 = formatFloat(POITools.getCellValue(row.getCell(11)));
			extPrice2 = formatFloat(POITools.getCellValue(row.getCell(12)));
			extPrice3 = formatFloat(POITools.getCellValue(row.getCell(13)));
			extPrice4 = formatFloat(POITools.getCellValue(row.getCell(14)));
			extPrice5 = formatFloat(POITools.getCellValue(row.getCell(15)));
			sqlList.add("insert into book_base (server_excel_name, is_self, book_isbn, book_name, book_lang, book_author, epub_price, pdf_price, ad_price, author_royalty, insert_time, trans_time, trans_company, yz_price, ext_price1, ext_price2, ext_price3, ext_price4, ext_price5) values" +
					"('"+name+"', '"+isSelf+"', '"+isbn+"', '"+bookName+"', '"+wz+"', '"+zb+"', "+epubPrice+", "+pdfPrice+", "+adPrice+", "+0.0+", '"+StringUtil.dataFormat(date, "yyyy-MM-dd")+"', '"+transTime+"', '"+transCompany+"', "+yzPrice+", "+extPrice1+", "+extPrice2+", "+extPrice3+", "+extPrice4+", "+extPrice5+")");
		}
		wb.close();
		FileUtil.deleteSubFiles(new File(excelPath).getParentFile());
		if(sqlList==null||sqlList.isEmpty()) return 0;
		return BookBaseModel.dao.addExcelData(sqlList).length;
	}
	
	private String formatFloat(String str) {
		if("".equals(str)||null==str) {
			return "0.0";
		} else {
			return str;
		}
	}
}
