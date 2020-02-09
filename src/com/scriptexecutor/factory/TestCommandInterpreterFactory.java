package com.scriptexecutor.factory;

import com.scriptexecutor.interpreter.TestCommandInterpreter;

public class TestCommandInterpreterFactory {

	private static String PREFIX = "TestCommandInterpreter";

	public static TestCommandInterpreter create(String interpreterType)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		if (!interpreterType.contains(PREFIX)) {
			interpreterType = new StringBuffer(interpreterType).append(PREFIX).toString();
		}

		Class<?> classUsed = Class.forName(TestCommandInterpreter.class.getPackage().getName() + "." + interpreterType);

		TestCommandInterpreter obj = (TestCommandInterpreter) classUsed.newInstance();

		return obj;
	}
}
