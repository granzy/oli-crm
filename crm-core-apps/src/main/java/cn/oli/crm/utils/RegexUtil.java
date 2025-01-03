package cn.oli.crm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 根据提供的内容和正则表达式返回所有匹配的结果。
     *
     * @param content 需要搜索的文本内容
     * @param regex   正则表达式
     * @return 匹配结果的列表
     */
    public static List<String> getMatches(String content, String regex) {
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }

}
