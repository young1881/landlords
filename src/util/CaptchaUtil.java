package util;

import java.util.ArrayList;
import java.util.Random;

public class CaptchaUtil {

    // generates a captcha code with 5 random characters,
    // (letters & numbers), where numbers could not appear
    // consecutively.
    public static String getCaptcha() {
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char) ('a' + i));
            list.add((char) ('A' + i));
        }
        for (int i = 0; i < 9; i++) {
            list.add((char) ('0' + i));
        }

        char[] chars = new char[5];

        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            int randomIndex;
            if (i != 0 && isNumber(chars[i - 1])) {
                randomIndex = r.nextInt(list.size() - 10);
            } else {
                randomIndex = r.nextInt(list.size());
            }
            chars[i] = list.get(randomIndex);
        }

        return new String(chars);
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
