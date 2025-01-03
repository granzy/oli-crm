package cn.oli.crm.utils;

public class MailUtil {
    public static String hiddenEmailAddress(String mailAddress){
        String hiddenEmail;
        if (mailAddress.indexOf("@") <= 3) {
            hiddenEmail = mailAddress.replaceAll("\\w(?=\\w*?@)", "*");
        } else {
            hiddenEmail = mailAddress.replaceAll("(?<=\\w{3})\\w(?=\\w*?@)", "*");
        }
        return hiddenEmail;
    }
}
