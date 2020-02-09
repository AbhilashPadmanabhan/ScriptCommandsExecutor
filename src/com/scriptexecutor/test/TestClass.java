package com.scriptexecutor.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import com.scriptexecutor.utils.JSONUtil;

public class TestClass {

	public static void main(String[] args) throws Exception {
		File file = new File(System.getProperty("user.dir") + "/src/apivision.json");
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String str;
		StringBuffer contents = new StringBuffer();
		
		while((str = bf.readLine()) != null) {
			contents.append(str);
		};
		
		Map<String, Object> map = JSONUtil.jsonToMap(contents.toString());
		bf.close();
		
		JSONUtil.getTextAnnotations(map);
	}
}
