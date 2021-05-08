package com.pholema.tool.utils.timer;

import org.apache.log4j.Logger;

import com.pholema.tool.utils.common.DateUtils;

import java.util.*;

public class TimerTracker {

    private static Logger logger = Logger.getLogger(TimerTracker.class);

    class TimerPlan {
        Timer timer = new Timer();
        int hr;
        int min;
        int sec;

        TimerPlan(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            this.hr = calendar.get(Calendar.HOUR_OF_DAY);
            this.min = calendar.get(Calendar.MINUTE);
            this.sec = calendar.get(Calendar.SECOND);
        }
    }

    private static Map<String,TimerPlan> timerMap = new HashMap<>();

    public void runCrossDay(int min, TimerTask timerTask) {
        timerMap.put(timerTask.getClass().getName(), new TimerPlan(DateUtils.getDateStart_min(min)));
        runCrossDay(timerTask);
    }

    public void runCrossDay(Date date, TimerTask timerTask) {
        timerMap.put(timerTask.getClass().getName(), new TimerPlan(date));
        runCrossDay(timerTask);
    }

    private static Date getDateStart(int hr, int min, int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        Date date = calendar.getTime();
        Date d = new Date();
        if (date.equals(d) || date.before(d)) {
            calendar.add(Calendar.DATE, 1); // add one day more
            date = calendar.getTime();
        }
        return date;
    }

    private static void runCrossDay(TimerTask timerTask) {
        TimerPlan timerPlan = timerMap.get(timerTask.getClass().getName());
        if (timerPlan == null) {
            logger.error("timer not found " + timerTask.getClass().getName());
            return;
        }
        timerPlan.timer.schedule(timerTask, getDateStart(timerPlan.hr, timerPlan.min, timerPlan.sec));
    }

    public static void reSchedule(TimerTask timerTask) {
        runCrossDay(timerTask);
    }
}

