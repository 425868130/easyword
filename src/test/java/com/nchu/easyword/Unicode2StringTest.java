package com.nchu.easyword;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unicode2StringTest {
    public static void main(String[] args) {
        System.out.println("\u62ff\uff0c\u53d6,\u91c7\u53d6,\u63a5\u53d7\uff08\u793c\u7269\u7b49\uff09,\u8017\u8d39\uff08\u65f6\u95f4\u7b49\uff09");
        System.out.println(decodeUnicode("National Council for Civic Responsibility u56fdu5bb6u516cu6c11u804cu8d23u59d4u5458u4f1a"));
    }
    /**
     * unicode 转字符串
     *,\\u91c7\\u53d6, requats
     */
    public static String decodeUnicode(String str) {

        Pattern pattern = Pattern.compile("(u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            //group 6728
            String group = matcher.group(2);
            //ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            //group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }
}
