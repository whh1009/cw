package com.wuzhou.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Test {

	public static void main(String[] args) throws Exception {
		parserAppStoreZip("j:/342e74e9-f10a-4bdb-bfc6-889b3606a1e7.zip",
				"j:/test");
	}

	/**
	 * 
	 * @param zipPath
	 * @param unzipPath
	 * @throws Exception
	 */
	public static void parserAppStoreZip(String zipPath, String unzipPath)
			throws Exception {
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
			return;
		Map<String, Info> map = parserTxt(txtPath);
		if (map == null || map.isEmpty())
			return;
		String saleTime = getAppStoreFileDate(txtPath);
		List<String> sqlList = new ArrayList<String>();
		Set<String> set = map.keySet();
		for (String s : set) {
			String sql = "";
			Info info = map.get(s);
			sql = "insert into book_sale (book_isbn, sale_time, sale_total_price, sale_total_count, book_name, platform) "
					+ "values ('"+info.getIsbn()+"', '"+saleTime+"', "+info.getPrice()+", "+info.getCount()+", '"+info.getTile()+"', 3)";
			sqlList.add(sql);
		}
		
		for(String sql : sqlList) {
			System.out.println(sql);
		}
		FileUtil.deleteSubFiles(new File(unzipPath));
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
					info.setCount(info.getCount()
							+ Integer.parseInt(temp.split("\t")[5]));
					info.setPrice(decimalFormat.format(Float.parseFloat(info.getPrice())
							+ Float.parseFloat(temp.split("\t")[7])));
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