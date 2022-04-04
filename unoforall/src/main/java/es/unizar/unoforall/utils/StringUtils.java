package es.unizar.unoforall.utils;

import java.nio.charset.Charset;

public class StringUtils {
	private static Charset UTF8 = Charset.forName("UTF-8");
	
	public static String parseString(String src) {
		return new String(src.getBytes(), UTF8);
	}
}
