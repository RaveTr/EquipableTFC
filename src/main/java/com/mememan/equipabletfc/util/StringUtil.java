package com.mememan.equipabletfc.util;

import java.util.Locale;

public class StringUtil {
	
	private StringUtil() {
		throw new IllegalAccessError("Attempted to instantiate a Utility Class!");
	}
	
	/**
	 * Returns a version of the input String in which the first letter is capitalized.
	 * @param targetString The String to capitalize the first letter of.
	 * @return A version of the input String in which the first letter is capitalized.
	 */
	public static String capitalizeFirstLetter(String targetString) {
		return targetString.replaceAll(targetString.substring(0, 1), targetString.substring(0, 1).toUpperCase(Locale.ROOT));
	}
}
