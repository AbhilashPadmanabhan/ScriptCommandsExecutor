package com.scriptexecutor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordHelper {

	public static String getBrowserName(String command) {

		String browserName = "Google Chrome";

		String plainCmd = command.toLowerCase();
		if (plainCmd.contains("chrome")) {
			browserName = "Google Chrome";
		}
		if (plainCmd.contains("firefox")) {
			browserName = "Mozilla Firefox";
		}
		if (plainCmd.contains("safari")) {
			browserName = "Safari";
		}
		if (command.contains("IE") || plainCmd.contains("internet explorer")) {
			browserName = "Internet Explorer";
		}
		return browserName;
	}

	public static String getURL(String command) {
		String url = "";
		List<String> commandTokens = tokenizeCommand(command);
		for (String token : commandTokens) {
			token = token.replaceAll("[\\u201c\\u201d\"]", "");
			if (isValidURL(token)) {
				url = token;
			}
		}
		return url;
	}

	public static String getTextToLookUp(String command) {
		List<String> values = tokenizeCommand(command, true);
		String value = null;
		if (values != null && values.size() > 0) {
			value = values.get(0).replaceAll("\"", "");
		}
		return value;
	}

	public static String[] getFieldNameAndValue(String command) {
		int fieldNameNearestIndex = 0, fieldValueNearestIndex = 0;

		List<Integer> quotedIndices = new ArrayList<>();

		List<String> commandTokens = tokenizeCommand(command);
		for (int i = 0; i < commandTokens.size(); i++) {
			String token = commandTokens.get(i);
			if (token.toLowerCase().equals("enter") || token.toLowerCase().equals("type"))
				fieldNameNearestIndex = i;
			if (token.toLowerCase().equals("field"))
				fieldValueNearestIndex = i;
			if (token.contains("\"")) {
				quotedIndices.add(i);
			}
		}
		String[] returnVal = new String[2];
		returnVal[0] = commandTokens.get(findNearestIndex(quotedIndices, fieldValueNearestIndex)).replaceAll("\"", "");
		returnVal[1] = commandTokens.get(findNearestIndex(quotedIndices, fieldNameNearestIndex)).replaceAll("\"", "");

		return returnVal;
	}

	private static int findNearestIndex(List<Integer> quotedIndices, int index) {
		int distance = Math.abs(quotedIndices.get(0) - index);
		int idx = 0;
		for (int c = 1; c < quotedIndices.size(); c++) {
			int cdistance = Math.abs(quotedIndices.get(c) - index);
			if (cdistance < distance) {
				idx = c;
				distance = cdistance;
			}
		}
		int theIndex = quotedIndices.get(idx);
		return theIndex;
	}

	private static boolean isValidURL(String url) {
		return Pattern.matches("^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+)\\.[a-zA-Z0-9]*\\.[a-z]{3}\\.?([a-z]+)?$",
				url);
	}

	private static List<String> tokenizeCommand(String command) {
		return tokenizeCommand(command, false);
	}

	private static List<String> tokenizeCommand(String command, boolean quotedTextOnly) {
		command = command.replaceAll("(\\u201c|\\u201d)", "\"");
		
		List<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
		while (m.find()) {
			String token = m.group(1);
			if (token.contains("\"") || !quotedTextOnly) {
				list.add(m.group(1));
			}
		}
		return list;
	}
}
