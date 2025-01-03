package cn.oli.crm.utils;


/**
 * @author gzy
 * @author csj
 */
public class StrUtil {

    /**
     * 判断传入的字符串是否为空串
     */
    public static boolean isEmpty(String str) {
        return str == null || ("".equals(str.trim()));
    }

    /**
     * 判断传入的字符串是否为空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 替换字符串
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (isNotEmpty(inString) && isNotEmpty(oldPattern) && newPattern != null) {
            int index = inString.indexOf(oldPattern);
            if (index == -1) {
                return inString;
            } else {
                int capacity = inString.length();
                if (newPattern.length() > oldPattern.length()) {
                    capacity += 16;
                }
                StringBuilder sb = new StringBuilder(capacity);
                int pos = 0;
                for (int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                    sb.append(inString, pos, index);
                    sb.append(newPattern);
                    pos = index + patLen;
                }
                sb.append(inString.substring(pos));
                return sb.toString();
            }
        } else {
            return inString;
        }
    }

    /**
     * 格式化字符串（替换符为%s）
     */
    public static String formatIfArgs(String format, Object... args) {
        if (isEmpty(format)) {
            return format;
        }
        return (args == null || args.length == 0) ? format.replaceAll("%([^n])", "%%$1") : String.format(format, args);
    }

    /**
     * 格式化字符串(替换符自己指定)
     */
    public static String formatIfArgs(String format, String replaceOperator, Object... args) {
        if (isEmpty(format) || isEmpty(replaceOperator)) {
            return format;
        }
        format = replace(format, replaceOperator, "%s");
        return formatIfArgs(format, args);
    }


    /**
     * 对SQL查询语句中的特殊字符进行转义
     *
     * @param query 需要转义的查询语句
     * @return 转义后的查询语句
     */
    public static String sqlSpecialCharactersEscape(String query) {
        return query.replace("!", "!!").replace("_", "!_").replace("%", "!%");
    }

    /**
     * 获取合法的zabbix主机名
     * 对字符串中除 数字,字母,空格,点号,短中划线,下划线线以外的字符进行转义,转义为 -
     */
    public static String getLegalZabbixHostStr(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return removeZabbixHostDomainInfo(str).replaceAll("[^0-9a-zA-Z\\s.\\-_]", "-");
    }

    public static String removeZabbixHostDomainInfo(String hostName) {
        String regex = "(\\.Ceair\\.com|\\.ceair\\.com|\\.ceaair\\.com|\\.eal\\.com|\\.ckair\\.com|\\.yourdomain\\.com)$";
        return hostName.replaceAll(regex, "");
    }

}
