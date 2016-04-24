package com.wuzhou.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class StringUtil {
	public static int StringToInt(String str) {
		if ((str == null) || ("".equals(str))) {
			return 0;
		}
		return Integer.parseInt(str);
	}
	
	public static double StringToDouble(Object str) {
		if ((str == null) || ("".equals(str))) {
			return 0.00;
		}
		return Double.parseDouble((String)str);
	}

	public static long StringToLong(String str) {
		if ((str == null) || ("".equals(str))) {
			return 0L;
		}
		return Long.parseLong(str);
	}

	public static String ObjectToString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString().trim();
	}

	public static String dateToString(String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}
	
	public static String dataFormat(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static boolean isBlank(String str) {
		if (str == null)
			return true;
		if (str.trim().length() < 1) {
			return true;
		}
		return false;
	}

	public static String ignoreComma(String str) {
		if ((str == null) || ("".equals(str))) {
			return "";
		}
		return str.replaceAll("(^[,]*|[,]*$|(?<=[,])[,]+)", "");
	}

	public static void print(Map<String, String> map) {
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			String key1 = (String) ((Map.Entry) it.next()).getKey();
			System.out.println(key1 + "的value是：" + (String) map.get(key1));
		}
	}
	
	public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
	
	public static String getNowYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(new Date());
	}

	public static String getNowMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		return formatter.format(new Date());
	}

	public static String getNowDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		return formatter.format(new Date());
	}
	
	public static boolean intToBoolean(int i) {
		if(i==0) return false;
		else return true;
	}
}
