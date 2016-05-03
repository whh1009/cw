package com.wuzhou.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.wuzhou.model.BookBaseModel;
import com.wuzhou.model.BookSaleModel;
import com.wuzhou.tool.FileUtil;
import com.wuzhou.tool.POITools;
import com.wuzhou.tool.StringUtil;
import com.wuzhou.tool.TimeTools;
import com.wuzhou.tool.ZipUtils;

public class ExcelImportService {
	Logger log = Logger.getLogger(ExcelImportService.class);
	
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
	
	/**
	 * 解析亚马逊美国的excel
	 * @param filePath
	 */
	public int parserAmazonUSExcel(String excelPath) throws Exception{
		Workbook wb = POITools.getWorkbook(excelPath);
		String name = new File(excelPath).getName().replace(".xlsx", "");
		Sheet sheet = POITools.getSheet(0, wb); //第一个sheet
		Row row = null;
		List<String> sqlList = new ArrayList<String>();
		String isbn = "";
		String bookName = "";
		String bookAuthor = "";
		String saleTime = getDate(name.substring(name.lastIndexOf("_")+1));
		String saleCount = "";
		String salePrice = "";
		String platform = "1";
		for(int rowNumber = 1; rowNumber<POITools.getRowCount(sheet);rowNumber++) { //第一行标题行，从第二行开始
			row = POITools.getRow(sheet, rowNumber);
			if("".equals(POITools.getCellValue(row.getCell(0)))) break; //如果读到空行就返回
			isbn = POITools.getCellValue(row.getCell(3));
			bookName = POITools.getCellValue(row.getCell(5)).replace("'", "''");
			bookAuthor = POITools.getCellValue(row.getCell(6)).replace("'", "''");
			saleCount = "".equals(POITools.getCellValue(row.getCell(12)))?"0.0":POITools.getCellValue(row.getCell(12));
			salePrice = "".equals(POITools.getCellValue(row.getCell(21)))?"0.0":POITools.getCellValue(row.getCell(21));
			sqlList.add("insert ignore into book_sale (sale_time, book_isbn, book_name, book_author, sale_total_count, sale_total_price, platform) values "
					+ "('"+saleTime+"', '"+isbn+"', '"+bookName+"', '"+bookAuthor+"', "+saleCount+", "+salePrice+", '"+platform+"')");
		}
		for(String str : sqlList) {
			log.warn(str);
		}
		return BookSaleModel.dao.batchImport(sqlList).length;
	}
	
	/**
	 * 
	 * @param zipPath
	 * @param unzipPath
	 * @throws Exception
	 */
	public void parserAppStoreZip(String zipPath, String unzipPath) throws Exception {
		ZipUtils.extractFolder(zipPath, unzipPath);
		String txtPath = "";
		for(File file : new File(unzipPath).listFiles()) {
			String filePath = file.getAbsolutePath();
			if(filePath.endsWith(".txt")) {
				txtPath = filePath;
				break;
			}
		}
		if("".equals(txtPath)) return;
		
		
	}
	
	private Map<String, Integer> parserTxt(String txtPath) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		StringBuffer sb = new StringBuffer();
		File file = new File(txtPath);
		try {
			FileInputStream stream = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(stream, "UTF8");
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			while ((temp = br.readLine()) != null) {
				map.put(temp.split("\t")[12], )
			}
			br.close();
			isr.close();
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 20130131-20130228
	 * 计算月份
	 * @param str
	 * @return
	 */
	private String getDate(String str) {
		str = str.substring(str.indexOf("-")+1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = formatter.parse(str);
			return TimeTools.timeFormat(TimeTools.getBeforeDay(date.getTime(), 15),"yyyyMM");
		} catch (ParseException e) {
			log.error(e);
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 85515735_0116_CN.txt.gz
	 * 计算月份
	 * @param str
	 * @return
	 */
	private String getAppStoreFileDate(String str) {
		str = str.substring(str.indexOf("_")+1, str.lastIndexOf("_"));
		str = str.substring(2,3)+str.substring(0,1);
		return str;
	}
	
}
