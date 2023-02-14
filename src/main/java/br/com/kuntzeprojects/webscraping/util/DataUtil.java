package br.com.kuntzeprojects.webscraping.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DataUtil {
	
	public static String formatDate(Date date, String mask) {
		DateFormat formatter = new SimpleDateFormat(mask);
		return formatter.format(date);
	}
}
