package com.wuzhou.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jfinal.kit.PathKit;
import com.wuzhou.bean.BookSaleEntity;
import com.wuzhou.config.Constraint;
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
	
	public List<BookSaleEntity> parserAmazonUSExcelToList(String excelPath) throws Exception{
		Workbook wb = POITools.getWorkbook(excelPath);
		String name = wb.getSheetName(0);
		Sheet sheet = POITools.getSheet(0, wb); //第一个sheet
		Row row = null;
		String saleTime = getDate(name.substring(name.lastIndexOf("_")+1));
		List<BookSaleEntity> list = new ArrayList<BookSaleEntity>();
		int count = 0;
		for(int rowNumber = 1; rowNumber<POITools.getRowCount(sheet);rowNumber++) { //第一行标题行，从第二行开始
			row = POITools.getRow(sheet, rowNumber);
			if("".equals(POITools.getCellValue(row.getCell(0)))) break; //如果读到空行就返回
			count++;
			BookSaleEntity aus = new BookSaleEntity();
			aus.setId(count);
			aus.setIsbn(POITools.getCellValue(row.getCell(3)));
			aus.setBookName(POITools.getCellValue(row.getCell(5)));
			aus.setBookAuthor(POITools.getCellValue(row.getCell(6)));
			aus.setSaleCount("".equals(POITools.getCellValue(row.getCell(12)))?"0.0":POITools.getCellValue(row.getCell(12)));
			aus.setSalePrice("".equals(POITools.getCellValue(row.getCell(19)))?"0.0":POITools.getCellValue(row.getCell(19)));
			aus.setListPrice("".equals(POITools.getCellValue(row.getCell(14)))?"0.0":POITools.getCellValue(row.getCell(14)));
			aus.setPlatform("亚马逊美国");
			aus.setSaleTime(saleTime);
			list.add(aus);
		}
		return list;
	}
	
	public List<BookSaleEntity> parserAmazonCNExcelToList(String excelPath) throws Exception{
		Workbook wb = POITools.getWorkbook(excelPath);
		String name = wb.getSheetName(0);
		Sheet sheet = POITools.getSheet(0, wb); //第一个sheet
		Row row = null;
		String saleTime = getDate(name.substring(name.lastIndexOf("_")+1));
		List<BookSaleEntity> list = new ArrayList<BookSaleEntity>();
		int count = 0;
		for(int rowNumber = 1; rowNumber<POITools.getRowCount(sheet);rowNumber++) { //第一行标题行，从第二行开始
			row = POITools.getRow(sheet, rowNumber);
			if("".equals(POITools.getCellValue(row.getCell(0)))) break; //如果读到空行就返回
			count++;
			BookSaleEntity acn = new BookSaleEntity();
			acn.setId(count);
			acn.setIsbn(POITools.getCellValue(row.getCell(3)));
			acn.setBookName(POITools.getCellValue(row.getCell(5)));
			acn.setBookAuthor(POITools.getCellValue(row.getCell(6)));
			acn.setSaleCount("".equals(POITools.getCellValue(row.getCell(12)))?"0.0":POITools.getCellValue(row.getCell(12)));
			acn.setSalePrice("".equals(POITools.getCellValue(row.getCell(21)))?"0.0":POITools.getCellValue(row.getCell(21)));
			acn.setListPrice("".equals(POITools.getCellValue(row.getCell(14)))?"0.0":POITools.getCellValue(row.getCell(14)));
			acn.setPlatform("亚马逊中国");
			acn.setSaleTime(saleTime);
			list.add(acn);
		}
		return list;
	}
	
	public List<BookSaleEntity> parserAppStoreZipToList(String zipPath) throws Exception {
		String unzipPath = PathKit.getWebRootPath()+File.separator+Constraint.UNZIP_FOLD;
		ZipUtils.extractFolder(zipPath, unzipPath);
		String txtPath = "";
		for (File file : new File(unzipPath).listFiles()) {
			String filePath = file.getAbsolutePath();
			if (filePath.endsWith(".txt")) {
				txtPath = filePath;
				break;
			}
		}
		if ("".equals(txtPath))
			return null;
		Map<String, Info> map = parserTxt(txtPath);
		if (map == null || map.isEmpty())
			return null;
		String saleTime = getAppStoreFileDate(txtPath);
		List<BookSaleEntity> list = new ArrayList<BookSaleEntity>();
		Set<String> set = map.keySet();
		int id=0;
		for (String s : set) {
			id++;
			BookSaleEntity bse = new BookSaleEntity();
			Info info = map.get(s);
			bse.setId(id);
			bse.setIsbn(info.getIsbn());
			bse.setBookName(info.getTile());
			bse.setBookAuthor("");
			bse.setSaleCount(info.getCount()+"");
			bse.setSalePrice(info.getPrice());
			bse.setPlatform("App Store");
			bse.setSaleTime(saleTime);
			list.add(bse);
		}
		FileUtil.deleteSubFiles(new File(unzipPath));
		return list;
	}
	
	public String saveExcel(String xml) {
		try {
			Document doc = Jsoup.parse(xml);
			String excelName = doc.select("root").first().attr("name");
//			String excelName = doc.attr("name");
			Elements eles = doc.select("item");
			List<String> sqlList = new ArrayList<String>();
			for(Element ele : eles) {
				sqlList.add("insert into book_sale (server_excel_name, sale_time, book_isbn, book_name, book_author, sale_total_count, sale_total_price, platform) values "
						+ "('"+excelName+"', '"+ele.attr("saleTime")+"', '"+ele.attr("isbn")+"', '"+StringUtil.formatMySqlChar(ele.attr("bookName"))+"', '"+StringUtil.formatMySqlChar(ele.attr("bookAuthor"))+"', "+ele.attr("saleCount")+", "+ele.attr("salePrice")+", '"+getPlatform(ele.attr("platform"))+"')");
			}
			int [] out = BookSaleModel.dao.batchImport(sqlList);
			return intArrayToString(out);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return null;
		}
	}
	
	private String intArrayToString(int[] out) {
		if(out==null||out.length==0) return "";
		else {
			String str = "";
			for(int i : out) {
				str+=i+",";
			}
			return StringUtil.ignoreComma(str);
		}
	}
	
	private String getPlatform(String platform) {
		switch(platform){
		case "亚马逊美国": return "2";
		case "亚马逊中国": return "3";
		case "App Store": return "4";
		case "Over Drive": return "5";
		case "That's Books": return "6";
		default:return "-1";
		}
	}
	/**
	 * 解析亚马逊美国的excel
	 * @param filePath
	 */
//	public int parserAmazonUSExcel(String excelPath) throws Exception{
//		Workbook wb = POITools.getWorkbook(excelPath);
//		String name = wb.getSheetName(0);
//		Sheet sheet = POITools.getSheet(0, wb); //第一个sheet
//		Row row = null;
//		List<String> sqlList = new ArrayList<String>();
//		String isbn = "";
//		String bookName = "";
//		String bookAuthor = "";
//		String saleTime = getDate(name.substring(name.lastIndexOf("_")+1));
//		String saleCount = "";
//		String salePrice = "";
//		String platform = "1";
//		for(int rowNumber = 1; rowNumber<POITools.getRowCount(sheet);rowNumber++) { //第一行标题行，从第二行开始
//			row = POITools.getRow(sheet, rowNumber);
//			if("".equals(POITools.getCellValue(row.getCell(0)))) break; //如果读到空行就返回
//			isbn = POITools.getCellValue(row.getCell(3));
//			bookName = POITools.getCellValue(row.getCell(5)).replace("'", "''");
//			bookAuthor = POITools.getCellValue(row.getCell(6)).replace("'", "''");
//			saleCount = "".equals(POITools.getCellValue(row.getCell(12)))?"0.0":POITools.getCellValue(row.getCell(12));
//			salePrice = "".equals(POITools.getCellValue(row.getCell(21)))?"0.0":POITools.getCellValue(row.getCell(21));
//			sqlList.add("insert ignore into book_sale (sale_time, book_isbn, book_name, book_author, sale_total_count, sale_total_price, platform) values "
//					+ "('"+saleTime+"', '"+isbn+"', '"+bookName+"', '"+bookAuthor+"', "+saleCount+", "+salePrice+", '"+platform+"')");
//		}
//		for(String str : sqlList) {
//			log.warn(str);
//		}
//		return BookSaleModel.dao.batchImport(sqlList).length;
//	}
	
	/**
	 * 
	 * @param zipPath
	 * @param unzipPath
	 * @throws Exception
	 */
//	public int parserAppStoreZip(String zipPath, String unzipPath) throws Exception {
//		ZipUtils.extractFolder(zipPath, unzipPath);
//		String txtPath = "";
//		for (File file : new File(unzipPath).listFiles()) {
//			String filePath = file.getAbsolutePath();
//			if (filePath.endsWith(".txt")) {
//				txtPath = filePath;
//				break;
//			}
//		}
//		if ("".equals(txtPath))
//			return 0;
//		Map<String, Info> map = parserTxt(txtPath);
//		if (map == null || map.isEmpty())
//			return 0;
//		String saleTime = getAppStoreFileDate(txtPath);
//		List<String> sqlList = new ArrayList<String>();
//		Set<String> set = map.keySet();
//		for (String s : set) {
//			String sql = "";
//			Info info = map.get(s);
//			sql = "insert ignore into book_sale (book_isbn, sale_time, sale_total_price, sale_total_count, book_name, platform) "
//					+ "values ('"+info.getIsbn()+"', '"+saleTime+"', "+info.getPrice()+", "+info.getCount()+", '"+info.getTile()+"', 3)";
//			sqlList.add(sql);
//		}
//		
//		for(String sql : sqlList) {
//			System.out.println(sql);
//		}
//		FileUtil.deleteSubFiles(new File(unzipPath));
//		return BookSaleModel.dao.batchImport(sqlList).length;
//	}
	
	/**
	 * 解析appstore txt的正文内容
	 * @param txtPath
	 * @return
	 */
	private static Map<String, Info> parserTxt(String txtPath) {
		Map<String, Info> map = new HashMap<String, Info>();
		File file = new File(txtPath);
		DecimalFormat decimalFormat=new DecimalFormat(".00");
		try {
			FileInputStream stream = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(stream, "UTF8");
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			String title = "";
			int lineNumber = 0;
			while ((temp = br.readLine()) != null) {
				lineNumber++;
				if(lineNumber==1) continue;
				if(temp.split("\t").length<11) break;
				title = temp.split("\t")[12];
				if(title==null||"".equals(title)) break;
				Info info = map.get(title);
				if (info == null) {
					info = new Info();
					info.setIsbn(temp.split("\t")[3]);
					info.setCount(Integer.parseInt(temp.split("\t")[5]));
					info.setPrice(decimalFormat.format(Float.parseFloat(temp.split("\t")[7])));
					info.setTile(title);
					map.put(title, info);
				} else {
					info.setCount(info.getCount() + Integer.parseInt(temp.split("\t")[5]));
					info.setPrice(decimalFormat.format(Float.parseFloat(info.getPrice()) + Float.parseFloat(temp.split("\t")[7])));
					map.put(title, info);
				}
			}
			br.close();
			isr.close();
			stream.close();
		} catch (IOException e) {
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
		str = str.replaceAll("[a-zA-Z ]+", "");
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
	private static String getAppStoreFileDate(String str) {
		str = str.substring(str.indexOf("_")+1, str.lastIndexOf("_"));
		str = "20"+str.substring(2,4)+str.substring(0,2);
		return str;
	}
	
	public void deleteByServerName(String serverName) {
		BookSaleModel.dao.deleteByServerName(serverName);
	}
	
}
class Info {
	private String isbn;
	private int count;
	private String price;
	private String tile;
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTile() {
		return tile;
	}

	public void setTile(String tile) {
		this.tile = tile;
	}

}