package com.nchu.easyword.utils;

import java.util.UUID;

public class UUIDUtils {
	public static String getUUID() {
		/*返回32位随机字符串*/
		return UUID.randomUUID().toString().replace("-", "");
	}
}
