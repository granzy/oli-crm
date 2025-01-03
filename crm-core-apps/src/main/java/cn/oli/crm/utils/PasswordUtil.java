package cn.oli.crm.utils;

import java.util.Random;

public class PasswordUtil {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

    public static String genPassword() {
        int passwordLength = 10;
        StringBuilder password = new StringBuilder(passwordLength);
        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            if(i <= 2){
                password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
            }else if(i <= 5){
                password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
            }else  if(i == 6){
                password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));
            }else{
                password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
            }
        }
        return password.toString();
    }
}
