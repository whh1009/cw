package com.wuzhou.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileUtil {
	
	/**
	 * 删除文件
	 * @param file
	 */
	public static void deleteFile(File file) {
		file.delete();
	}
	
	/**
	 * 删除目录下所有文件，保留目录
	 * @param path
	 */
	public static void deleteSubFiles(File file) {
		file.mkdirs();
		for(File fi:file.listFiles()){
	        if(fi.isDirectory()){
	        	deleteSubFiles(fi);
	        }
	        else{
	            fi.delete();
	        }
	    }
	}
	
	/**
	 * 全角转半角
	 * 
	 * @param
	 * @return 半角字符串
	 */
	public static String toDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}


	public static String fileInput(String srcPath, String charset) {
		StringBuffer sb = new StringBuffer();
		File file = new File(srcPath);
		try {
			FileInputStream stream = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(stream, charset);
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			
			while ((temp = br.readLine()) != null) {
				sb.append(temp).append("\r\n");
			}
			br.close();
			isr.close();
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static void fileOutput(String filename, String charset, String result) throws IOException {
		File file = new File(filename);
		FileOutputStream fos = new FileOutputStream(file);
		Writer out = new OutputStreamWriter(fos, charset);
		out.write(result);
		if (out != null) {
			out.close();
		}
		if (fos != null) {
			fos.close();
		}
	}
	
	
}
