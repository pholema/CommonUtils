package com.pholema.tool.utils.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateUtils {

    private final static String dateFormatStr = "yyyy-MM-dd HH:mm:ss.SSS";
    private final static String dateFormatStrYYYYMMDD = "yyyyMMdd";
    private final static String dateFormatStrHH = "HH";
    private final static String dateFormatStrmm = "mm";

    public static final DateFormat format = new SimpleDateFormat(dateFormatStr);
    public static final DateFormat formatYYYYMMDD = new SimpleDateFormat(dateFormatStrYYYYMMDD);
    public static final DateFormat formatHH = new SimpleDateFormat(dateFormatStrHH);
    public static final DateFormat formatmm = new SimpleDateFormat(dateFormatStrmm);

    public static String toDateStr(long date) {
        return toDateStr(new Date(date));
    }

    public static String toDateStr(Date date) {
        return format.format(date);
    }
    
    public static String toDateStrYYYYMMDD(long date) {
        return toDateStrYYYYMMDD(new Date(date));
    }

    public static String toDateStrYYYYMMDD(Date date) {
        return formatYYYYMMDD.format(date);
    }
    
    public static String toDateStrHH(long date) {
        return toDateStrHH(new Date(date));
    }

    public static String toDateStrHH(Date date) {
        return formatHH.format(date);
    }
    
    public static String toDateStrmm(long date) {
        return toDateStrmm(new Date(date));
    }

    public static String toDateStrmm(Date date) {
        return formatmm.format(date);
    }

	public static String getDateTime(String fmtString) {
		SimpleDateFormat fmt = new SimpleDateFormat(fmtString);
		fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		return fmt.format(new Date().getTime()).trim();
	}

	public static String getDateTime(Date date, String fmtString) {
		SimpleDateFormat fmt = new SimpleDateFormat(fmtString);
		fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		return fmt.format(date.getTime()).trim();
	}

	/***
	 * design relatively day
	 * @param day added amount of day, yesterday = -1
	 */
	public static String getDateTime_day(int day, String fmtString) {
		SimpleDateFormat fmt = new SimpleDateFormat(fmtString);
		fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

		return fmt.format(addDay(new Date(), day)).trim();
	}

	/**
	 * design relatively minute
	 * @param min added amount of minute, 5 min earlier = -5
	 */
	public static String getDateTime_min(int min, String fmtString) {
		SimpleDateFormat fmt = new SimpleDateFormat(fmtString);
		fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

		return fmt.format(new Date().getTime() + (min * 60 * 1000)).trim();
	}

	/**
	 * map to current time on specified day
	 * "yyyy-MM-dd" -> "yyyy-MM-dd:HH:mm:ss"
	 * @param dateday specified day
	 */
	public static String getDateTime_designate(String dateday, String fmtString) throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat(fmtString);
		fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

		Date d = getDate(dateday, "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		cal.set(Calendar.YEAR, Integer.parseInt(new SimpleDateFormat("yyyy").format(d)));
		cal.set(Calendar.MONTH, Integer.parseInt(new SimpleDateFormat("MM").format(d)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(new SimpleDateFormat("dd").format(d)));
		return fmt.format(cal.getTime()).trim();
	}

	public static Date getDate(String datetime, String fmtString) throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat(fmtString);
		fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		return fmt.parse(datetime);
	}

	public static int getCurrentIndex() {
		return getCurrentIndex(new Date());
	}

	public static int getCurrentIndex(Date now) {
		return Integer.parseInt(toDateStrHH(now))*60+Integer.parseInt(toDateStrmm(now));
	}

	public static int getAlldayIndex() {
		return 1439;
	}

	public static Date getDateStart(Integer offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, offset);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getDateStart_min(int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
		if (date.before(new Date())) {
			date = addDay(date, 1);
		}
		return date;
	}

	private static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

    public static Date GMT2Local(Long dateLng) {
		SimpleDateFormat gmt_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		gmt_fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat loc_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		loc_fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		Date r=null;
		try {
			r=loc_fmt.parse(gmt_fmt.format(new Date(dateLng)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}

	public static Date toAmericaDate(Long dateLng) {
		SimpleDateFormat loc_fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		loc_fmt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		Date r=null;
		try {
			r=loc_fmt.parse(loc_fmt.format(new Date(dateLng)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}

	// getMonth(-13) ,return -13 month~ this month
	public static List<String> getMonths(Integer offset) {
		List<String> list = new ArrayList<String>();
		for(int i=0;i<Math.abs(offset);i++){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH,i+offset+1);
			list.add(getDateTime(cal.getTime(),"yyyy-MM"));
		}
        return list;
    }
	
	public static List<String> getDayTimes(Integer offset) {
		String fmtString = "yyyy-MM-dd";
		List<String> list = new ArrayList<String>();
		for(int i=0;i<Math.abs(offset);i++){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,i+offset+1);
			
			//time string
			try {
				String day = getDateTime(cal.getTime(),fmtString);
				list.add(String.valueOf(getDate(day,fmtString).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
        return list;
    }
}
