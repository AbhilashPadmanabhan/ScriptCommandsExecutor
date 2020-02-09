package com.scriptexecutor;

import com.scriptexecutor.factory.TestCommandInterpreterFactory;
import com.scriptexecutor.interpreter.TestCommandInterpreter;

public class TestScriptExecutor {

	public static void main(String[] args) throws Exception {

		TestCommandInterpreter interpreter = TestCommandInterpreterFactory.create("Sikuli");
		interpreter.execute(System.getProperty("user.dir") + "/src/FBLoginTestScript.txt");
	}
}
