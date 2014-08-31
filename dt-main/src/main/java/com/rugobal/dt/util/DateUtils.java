package com.rugobal.dt.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	
	public static Date startOfDay(Date date)  {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public static Date endOfDay(Date date)  {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
        
    }
    
    public static Date firstDayOfMonth(Date date)  {
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	return cal.getTime();
    	
    }

}
