package com.mijia.util;

import java.text.ParseException;  
import java.text.ParsePosition;
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Date;  
import java.util.Map;

import com.mijia.util.exception.MijiaBusinessException;
  
public class DateUtil{  
      
    static final String yyyy_mm_dd = "yyyy-MM-dd";  
      
    static final String yyyyMMdd = "yyyyMMdd";  
    
    static SimpleDateFormat yyyy_mm_ddFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static SimpleDateFormat YYYYMMDDMMHHSSSSS = new SimpleDateFormat("yyyyMMddHHmmssss");
    
    static SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-01 00:00:00");
    static SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      
    public static String dateFormat(Date date, String formatStr){
    	 SimpleDateFormat format = new SimpleDateFormat(formatStr);        
         return format.format(date); 
    }
    /** 
     * 获取当前日期 yyyy-MM-dd
     * @return 
     */  
    public static String getCurrentDate(){  
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd);        
        return format.format(new Date());  
    }  
    /**
     * 
     * @param date
     * @param plus
     * @return
     */
    public static boolean compareCurrentDate(Date date,long plus){
    	long nowTime = System.currentTimeMillis();
    	long time = date.getTime()+plus;
    	return (time>nowTime);
    }
    
    public static int subCurrentDate(String dateStr){
    	long result = 0;
    	SimpleDateFormat format = new SimpleDateFormat(yyyy_mm_dd); 
    	try {
			Date date1 = format.parse(dateStr);
			result = date1.getTime() - System.currentTimeMillis();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return (int)result/(1000*60*60*24);
    }
    public static int subDate(String dateStr1,String dateStr2){
    	long result = 0;
    	SimpleDateFormat format = new SimpleDateFormat(yyyy_mm_dd); 
    	try {
			Date date1 = format.parse(dateStr1);
			result = date1.getTime() - format.parse(dateStr2).getTime();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return (int)result/(1000*60*60*24);
    }
    
    public static String calculateRemainTime(Date nowDate,String endTime){
		String result = "";
		int dayCount = 0;
		int monthCount = 0;
		try {
			Date endDate = yyyy_mm_ddFormat.parse(endTime);
			Calendar calendar = Calendar.getInstance();
			Calendar calendarNow = Calendar.getInstance();
			calendarNow.setTime(nowDate);
			calendar.setTime(endDate);
			int month = calendar.get(Calendar.MONTH);
			int calendarNowMonth = calendarNow.get(Calendar.MONTH);
			
			int year = calendar.get(Calendar.YEAR);
			int calendarNowYear = calendarNow.get(Calendar.YEAR);
			monthCount = (year - calendarNowYear)*12;
			if(month-calendarNowMonth>=1||month-calendarNowMonth<0||monthCount>0){
				monthCount = month - calendarNowMonth-1+monthCount;
				dayCount = calendarNow.getMaximum(Calendar.DAY_OF_MONTH) - calendarNow.get(Calendar.DAY_OF_MONTH) ;
				dayCount = calendar.get(Calendar.DAY_OF_MONTH) - calendar.getMinimum(Calendar.DAY_OF_MONTH) + dayCount + 2;
			}else if(month - calendarNowMonth==0){
				dayCount = calendar.get(Calendar.DAY_OF_MONTH) - calendarNow.get(Calendar.DAY_OF_MONTH);
			}
			if(dayCount<0){
				monthCount = monthCount - 1;
				dayCount = 30 + dayCount%30;
			}else{
				monthCount = monthCount + dayCount/30;
				dayCount = dayCount%30;
			}
			
			
			if(monthCount==0){
				result = dayCount+"日";
				if(dayCount<0){
					result = "0日";
				}
			}else if(monthCount<0){
				result = "0日";
			}
			else{
				if(dayCount!=0){
					result = monthCount + "月" + dayCount+"日";
				}else{
					result = monthCount + "月";
				}
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
    public static void initFirstAndLastDay(String yearMonthDay,Map<String,Object> paraMap)throws MijiaBusinessException{
		Date date;
		Date startTime = null;
		 Date endTime = null;
		try {
			if (yearMonthDay != null && !yearMonthDay.equals("")) {
				date = sdf.parse(yearMonthDay + " 00:00:00");
				startTime = sdf.parse(sdfStart.format(date));
				Calendar cal = Calendar.getInstance();
				cal.setTime(sdf.parse(sdfEnd.format(date)));
				int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				cal.set(Calendar.DAY_OF_MONTH, lastDay);
				endTime = cal.getTime();
				paraMap.put("startTime", startTime);
				paraMap.put("endTime", endTime);
			}
		} catch (java.text.ParseException e) {
			throw new MijiaBusinessException("000003", "请求参数解析出错!");
		}
	}
	/**
	* 计算时间差（天）
	*
	* @param beginTime
	*            开始时间
	* @param endTime
	*            结束时间
	* @return 从开始时间到结束时间之间的时间差（天）
	*/
	public static int getTimeBetween(String format,String beginTime, String endTime) {
			Calendar calendar1=Calendar.getInstance();;
		    Calendar calendar2=Calendar.getInstance();;
		    SimpleDateFormat formatter1 = new SimpleDateFormat(format);//格式很重要：是20081031，还是2008-10-31格式呢？
		    if (beginTime.equals("0")) {System.out.println("sBirthDate.equals====0");   return 0;   }
		    try {
		    calendar1.setTime(formatter1.parse(beginTime));
		    calendar2.setTime(formatter1.parse(endTime));
		    } catch (ParseException e) {
			    e.printStackTrace();
			   }
		    System.out.println((int)( (calendar2.getTimeInMillis()-calendar1.getTimeInMillis())/1000/60/60/24 ));

		    return  (int)( (calendar2.getTimeInMillis()-calendar1.getTimeInMillis())/1000/60/60/24 );//获取天数的差值。

	}
	/**
	 * 获取当前时间的N天 N月  N年
	 * @param format  格式化
	 * @param StrDate 当前时间
	 * @param year  增加或减少年数
	 * @param month 增加或减少月数
	 * @param day 增加或减少天数
	 * @return
	 */
	public static String dateAddYMD(String format, String StrDate, int year,
			int month, int day) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sFmt = new SimpleDateFormat(format);
		cal.setTime(sFmt.parse((StrDate), new ParsePosition(0)));

		if (day != 0) {
			cal.add(cal.DATE, day);
		}
		if (month != 0) {
			cal.add(cal.MONTH, month);
		}
		if (year != 0) {
			cal.add(cal.YEAR, year);

		}
		return sFmt.format(cal.getTime());
	}

    public static void main(String[] args) {
    	System.out.println(new DateUtil().calculateRemainTime(new Date(),"2016-8-26"));
	}
}  