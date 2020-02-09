package com.scriptexecutor.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationHelper {

	private static Properties prop = null;

	static {
		InputStream propFile = null;
		try {
			propFile = new FileInputStream(System.getProperty("user.dir") + "/src/com/scriptexecutor/properties/config.properties");
			//prop.load(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getSleepTime() {
		return (prop != null) ? Integer.valueOf(prop.getProperty("sleep_time")) : 0;
	}
}
