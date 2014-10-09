package com.alzatezabala.libreria;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckRgx {
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static Matcher matcher;
	
	public static boolean isEmail(String email){
		Pattern pattern=Pattern.compile(EMAIL_PATTERN);
		matcher=pattern.matcher(email);
		return matcher.matches();
	}
	
	public static boolean isTel(String tel){
		Pattern pattern=Pattern.compile("");
		matcher=pattern.matcher(tel);
		return matcher.matches();
	}
}
