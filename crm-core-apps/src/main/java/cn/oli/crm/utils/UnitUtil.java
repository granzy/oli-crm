package cn.oli.crm.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 单位转换工具类
 */
public class UnitUtil {

    public static String carry(double value, String[] units, int hex, boolean withUnit) {
        int i = value == 0 ? 0 : (int) Math.floor(Math.log(value) / Math.log(hex));
        double scaledValue = round(value / Math.pow(hex, i));
        if (withUnit) {
            return scaledValue + " " + units[Math.max(i, 0)];
        }
        return String.valueOf(scaledValue);
    }

    private static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String formatUnixTime(long unixTime) {
        Instant instant = Instant.ofEpochSecond(unixTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    public static String uptimeFormatter(int seconds, boolean withUnit) {
        int days = seconds / 86400;
        seconds %= 86400;
        int hours = seconds / 3600;
        seconds %= 3600;
        int minutes = seconds / 60;
        seconds %= 60;

        if (withUnit) {
            return days + "天, " + String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d, %02d:%02d:%02d", days, hours, minutes, seconds);
        }
    }

    private static String formatNumber(double number, String unit) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number) + unit;
    }


    public static String formatTime(double value, String unit, boolean withUnit) {
        double totalSeconds;
        if ("ms".equals(unit)) {
            totalSeconds = value / 1000.0;
        } else if ("s".equals(unit)) {
            totalSeconds = value;
            if (totalSeconds < 1) {
                return formatNumber(totalSeconds * 1000, withUnit ? "毫秒" : "");
            }
        } else {
            throw new IllegalArgumentException("Unsupported unit. Only \"s\" and \"ms\" are allowed.");
        }

        if (totalSeconds < 60) {
            return formatNumber(totalSeconds, withUnit ? "秒" : "");
        } else if (totalSeconds < 3600) {
            int minutes = (int) (totalSeconds / 60);
            double seconds = totalSeconds % 60;
            return withUnit
                    ? String.format("%d分钟%s秒", minutes, formatNumber(seconds, ""))
                    : String.format("%d:%s", minutes, formatNumber(seconds, ""));
        } else if (totalSeconds < 86400) {
            int hours = (int) (totalSeconds / 3600);
            int minutes = (int) ((totalSeconds % 3600) / 60);
            double seconds = totalSeconds % 60;
            return withUnit
                    ? String.format("%d小时%d分钟%s秒", hours, minutes, formatNumber(seconds, ""))
                    : String.format("%d:%02d:%s", hours, minutes, formatNumber(seconds, ""));
        } else {
            int days = (int) (totalSeconds / 86400);
            int hours = (int) ((totalSeconds % 86400) / 3600);
            return withUnit
                    ? String.format("%d天%d小时", days, hours)
                    : String.format("%d:%02d", days, hours);
        }
    }

    public static String unitFormatter(String baseUnit, double value, boolean withUnit) {
        switch (baseUnit) {
            case "bps":
                return carry(value, new String[]{"bps", "Kbps", "Mbps", "Gbps", "Tbps", "Pbps"}, 1000, withUnit);
            case "Bps":
                return carry(value, new String[]{"Bps", "KBps", "MBps", "GBps", "TBps", "PBps"}, 1000, withUnit);
            case "HZ":
                return carry(value, new String[]{"HZ", "KHZ", "MHZ", "GHZ", "THZ", "PHZ"}, 1000, withUnit);
            case "b":
                return carry(value, new String[]{"b", "Kb", "Mb", "Gb", "Tb", "Pb", "Eb", "Zb", "Yb"}, 1000, withUnit);
            case "B":
                return carry(value, new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"}, 1024, withUnit);
            case "b/s":
                return carry(value, new String[]{"b/s", "Kb/s", "Mb/s", "Gb/s", "Tb/s", "Pb/s", "Eb/s", "Zb/s", "Yb/s"}, 1000, withUnit);
            case "B/s":
                return carry(value, new String[]{"B/s", "KB/s", "MB/s", "GB/s", "TB/s", "PB/s", "EB/s", "ZB/s", "YB/s"}, 1024, withUnit);
            case "ms":
                return formatTime(value, "ms", withUnit);
            case "s":
                return formatTime(value, "s", withUnit);
            case "uptime":
                return uptimeFormatter((int) value, withUnit);
            case "unixtime":
                return formatUnixTime((long) value);
            case "%":
                return withUnit ? round(value) + "%" : String.valueOf(round(value));
            default:
                return withUnit ? round(value) + baseUnit : String.valueOf(round(value));
            // throw new IllegalArgumentException("Unsupported base unit: " + baseUnit);
        }
    }
}
