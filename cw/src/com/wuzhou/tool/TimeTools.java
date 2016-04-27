package com.wuzhou.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author wanghonghui
 * 
 */
public class TimeTools {
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @param format
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String timeFormat(long time, String format) {
		if ("".equals(format))
			format = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date(time));
	}

	/**
	 * 获取当前“年”
	 * 
	 * @return
	 */
	public static String getNowYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(new Date());
	}

	/**
	 * 获取当前“月”
	 * 
	 * @return
	 */
	public static String getNowMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		return formatter.format(new Date());
	}

	/**
	 * 获取当前“日”
	 * 
	 * @return
	 */
	public static String getNowDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		return formatter.format(new Date());
	}

	public static long getStartTime(int day) {
		Date date = new Date();
		date.setDate(date.getDate() + day);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date.getTime();
	}

	public static long getStartTime(Date date, int day) {
		date.setDate(date.getDate() + day);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date.getTime();
	}

	public static long getEndTime(int day) {
		Date date = new Date();
		date.setDate(date.getDate() + day);
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date.getTime();
	}

	public static long getEndTime(Date date, int day) {
		date.setDate(date.getDate() + day);
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date.getTime();
	}

	/**
	 * 获取开始时间
	 * 
	 * @param timeStr
	 *            “yyyy-MM-dd”
	 * @return 返回yyyy-MM-dd 00:00:00的long型数据
	 */
	public static long getStartTime(String timeStr) {
		Date date;
		try {
			synchronized(timeFormat) {
				date = timeFormat.parse(timeStr);
			}
			return getStartTime(date, 0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取结束时间
	 * 
	 * @param timeStr
	 *            “yyyy-MM-dd”
	 * @return 返回yyyy-MM-dd 23:59:59的long型数据
	 */
	public static long getEndTime(String timeStr) {
		Date date;
		try {
			synchronized(timeFormat) {
				date = timeFormat.parse(timeStr);
			}
			return getEndTime(date, 0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取过去几天
	 * 
	 * @param time
	 * @param day
	 *            1 代表返回前一天
	 * @return
	 */
	public static long getBeforeDay(long time, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int myDay = c.get(Calendar.DATE);
		c.set(Calendar.DATE, myDay - day);
		return c.getTimeInMillis();
	}

	/**
	 * 获取过去几天
	 * 
	 * @param time
	 * @param day
	 * @return yyyy-MM-dd
	 */
	public static String getBeforeDay(String time, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getStartTime(time));
		int myDay = c.get(Calendar.DATE);
		c.set(Calendar.DATE, myDay - day);
		synchronized(timeFormat) {
			return timeFormat.format(c.getTime());
		}
		
	}

	/**
	 * 未来几天
	 * 
	 * @param time
	 * @param day
	 *            2未来2天
	 * @return
	 */
	public static long getAfterDay(long time, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int myDay = c.get(Calendar.DATE);
		c.set(Calendar.DATE, myDay + day);
		return c.getTimeInMillis();
	}

	/**
	 * 未来几天
	 * 
	 * @param time
	 * @param day
	 * @return yyyy-MM-dd
	 */
	public static String getAfterDay(String time, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getStartTime(time));
		int myDay = c.get(Calendar.DATE);
		c.set(Calendar.DATE, myDay + day);
		synchronized(timeFormat) {
			return timeFormat.format(c.getTime());
		}
	}
	
	/**
	 * 获取指定日期的毫秒值
	 * @param date
	 * @return
	 */
	public static long getTimestamp(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date parse = dateFormat.parse(date);
			long time = parse.getTime();
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getFormatDate(Date date, String strFormat) {

		String strRet = null;
		if (date == null) {
			return strRet = "";
		}
		SimpleDateFormat simple = new SimpleDateFormat(strFormat);
		if (simple != null)
			strRet = simple.format(date);
		return strRet;
	}
	
	/**
	 * 前几分钟
	 * @param hourCount
	 * @param date
	 * @return
	 */
	public static long getBeforeMinute(int hourCount, long date) {
		return date-hourCount*60*1000;
	}
	
	/**
	 * 获取时间差
	 * @param str1
	 * @param str2
	 * @return
	 * @throws Exception
	 */
	public static long getDistanceDays(String str1, String str2) throws Exception{  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        Date one;  
        Date two;  
        long days=0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            days = diff / (1000 * 60 * 60 * 24);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return days;  
    }
}
