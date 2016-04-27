package com.wuzhou.tool;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Test {

	public static void test2() throws ParseException {
		String unZipFileName = "j:\\6620fcac-a1e4-41a2-be1c-eaf5f6fc55ab.zip";
		String outputPath = "j:\\test";
		AntZip zip = new AntZip(600);
		zip.unZip(unZipFileName, outputPath);

	}

	public static void main(String[] args) throws Exception {
		// String xml = "<xml><item bname='aa' bnum='bb' /></xml>";
		// Document doc = Jsoup.parse(xml);
		// Elements eles = doc.select("item");
		// System.out.println(eles.get(0).attr("bname"));
		// System.out.println(new Date().getTime());
//		test2();
		
		String str = "〖HT8.25,8.75SS〗〖JP3〗〖DS(2。5*2W〗〖HT19.75,19.25SS〗〖CX-1〗〖CT〗NB778〖CX〗〖HT4〗(賨)〖CX〗〖HT〗〖DS)〗〖JP〗〖WT8.25,8.75F2〗〓〖CT(〗ｃóｎｇ〖CT)〗〖KG*2〗〖WT8.25,8.75XT〗〖JP5〗秦汉间今四川、湖南一带少数民族交纳的赋税名称，交的钱币叫NB778钱，交的布匹叫NB778布。这一部分民族也因此叫NB778人。";
		str = str.replace("@", "");
		if(str.indexOf("")!=-1) {
		}
		
	}
	
	
}
