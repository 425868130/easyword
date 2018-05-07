package com.nchu.easyword.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2018-3-6 11:19:02
 *
 * @author xujw
 * unicode编码工具类
 */
public class UnicodeUtils {
    /**
     * unicode转中文
     *
     * @param dataStr 包含unicode编码的字符串
     * @return 解码后的中文字符串
     */
    public static String decodeUnicode(final String dataStr) {
        String str = dataStr;
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
