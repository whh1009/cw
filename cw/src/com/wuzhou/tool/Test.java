package com.wuzhou.tool;

import java.io.File;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Test {

	public static void read() throws Exception{
		Workbook wb = POITools.getWorkbook("C:\\Users\\wanghonghui\\Desktop\\书号模板.xlsx");
		Sheet sheet = POITools.getSheet(0, wb);
		for(int rowNumber = 0; rowNumber<POITools.getRowCount(sheet);rowNumber++) {
			Row row = POITools.getRow(sheet, rowNumber);
			for(int columnCount = 0; columnCount < POITools.getColumnCount(row); columnCount++) {
				System.out.print(POITools.getCellValue(row.getCell(columnCount))+"\t");
			}
			System.out.println();
		}
		wb.close();
	}
	
	public static void write() throws Exception {
		
	}
	
	/**
	 * 校验码计算方法：
	 * 1. sum1 = 奇数求和
	 * 2. sum2 = 偶数*3并求和
	 * 3. sum = sum1+sum2
	 * 4. out = 10 - sum % 10（若out==10， 则返回0）
	 * @param code
	 * @return
	 */
	private static String gen(String code) {
		int c=0;
        for(int i=0;i<12;i+=2)  c+=Integer.parseInt(code.charAt(i)+"");
        for(int i=1;i<12;i+=2)  c+=Integer.parseInt(code.charAt(i)+"")*3;
        c = 10-c%10;
        if(c==10) return code+"0";
        else return code + c;
	}
	
	public static void test() {
		String path = "C:\\apache-tomcat-7.0.65\\webapps\\booknum\\uploadFiles\\";
		FileUtil.deleteSubFiles(new File(path));
	}

	public static void main(String[] args) throws Exception {
//		String xml = "<xml><item bname='aa' bnum='bb' /></xml>";
//		Document doc = Jsoup.parse(xml);
//		Elements eles = doc.select("item");
//		System.out.println(eles.get(0).attr("bname"));
		System.out.println(new Date().getTime());
	}

}
