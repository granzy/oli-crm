package cn.oli.crm.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间工具类
 *
 * @author sjk
 */
@Slf4j
public class DateUtil {

    private static final int[] SECOND_ARRAY = new int[]{31104000, 2592000, 86400, 3600, 60, 1};
    private static final String[] DATE_STR = new String[]{"y", "M", "d", "h", "m", "s"};
    private static final String[] DATE_CHINESE_STR = new String[]{"年", "个月", "天", "小时", "分钟", "秒"};

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间戳转持续时间
     *
     * @param second 毫秒
     * @return 持续时间
     */
    public static String getDuration(long second) {
        if (second == 0) {
            return "0s";
        }
        return getDurationByType(second / 1000, DATE_STR);
    }

    public static String getChineseDuration(long second) {
        if (second == 0) {
            return "0秒";
        }
        return getDurationByType(second / 1000, DATE_CHINESE_STR);
    }

    private static String getDurationByType(long second, String[] dateStrArray) {
        StringBuilder duration = new StringBuilder();
        int limit = 0;
        int loopTime = 5;
        int limitTime = 3;
        boolean startFlag = false;
        for (int i = 0; i <= loopTime && limit < limitTime; i++) {
            int tempTime = (int) (second) / SECOND_ARRAY[i];
            if (tempTime != 0 || startFlag) {
                duration.append(tempTime).append(dateStrArray[i]);
                startFlag = true;
                limit++;
            }
            second = second % SECOND_ARRAY[i];
        }
        return duration.toString();
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param timeStamp 时间戳
     * @return String
     */
    public static String timeStamp2TimeString(long timeStamp, String formatter) {
        if (String.valueOf(timeStamp).length() == 10) {
            timeStamp *= 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        Timestamp ts = new Timestamp(timeStamp);
        return sdf.format(ts);
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param timeStamp 时间戳
     * @return String
     */
    public static String timeStamp2TimeString(long timeStamp) {
        return timeStamp2TimeString(timeStamp, FULL_TIME_SPLIT_PATTERN);
    }

    /**
     * 获取当天开始的时间戳
     *
     * @return Long
     */
    public static Long getDayStartTimestamp(int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, dayOffset);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获取本周开始的时间戳
     *
     * @param weekOffset weekOffset
     * @return Long
     */
    public static Long getWeekStartTimestamp(int weekOffset) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        // 设置为本周的第一天（星期日）
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 清除时、分、秒、毫秒，只保留日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 获取本周开始的时间戳（毫秒）
        return calendar.getTimeInMillis();
    }

    /**
     * 获取本月开始的时间戳
     *
     * @param monthOffset monthOffset
     * @return Long
     */
    public static Long getMonthStartTimestamp(int monthOffset) {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthOffset);
        // 设置为本月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 清除时、分、秒、毫秒，只保留日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 获取本月开始的时间戳（毫秒）
        return calendar.getTimeInMillis();
    }

    /**
     * 判断当天是否为本月最后一天
     */
    public static boolean isLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day == lastDay;
    }
}
